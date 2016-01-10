/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import bases.TacticalMovement;
import dataObjects.Continent;
import dataObjects.Player;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerStates;

/**
 * The game state is accountable for all changes made to the data objects. Each change to the data objects should be
 * done over the GameState class. This helps to keep track of actual changes and populating repaint requests to the
 * engine.
 */
public class GameState extends GameBase {
    private boolean mouseTargetClickable;
    private int occupiedTerritories;
    private Phases gamePhase = Phases.NotStarted;


    /**
     * @return the current active game phase. Take a look at Phases to gather more information's.
     */
    public Phases getGamePhase() {
        return gamePhase;
    }

    /**
     * set the current active game phase. Take a look at Phases to gather more information's.
     */
    private void setGamePhase(Phases phase) {
        gamePhase = phase;
        if (phase == Phases.Landerwerb) {
            setPlayerState(data.getHumanPlayer(), PlayerStates.Reinforcing);
        } else if (phase == Phases.Reinforcement) {
            reassign_Reinforcements(data.getHumanPlayer());
            reassign_Reinforcements(data.getCompPlayer());

            setPlayerState(data.getHumanPlayer(), PlayerStates.Reinforcing);
            setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
        } else if (phase == Phases.AttackOrMove) {
            setPlayerState(data.getHumanPlayer(), PlayerStates.FirstTerritorySelection);
            setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
        }

        engine.validateMouseButtons();
        engine.requestRepaint();
    }

    /**
     * @return the currently occupied territories. This field make only sense in the 'Landerwerb' phase
     */
    public int getOccupiedTerritories() {
        return occupiedTerritories;
    }

    @Override
    public void init(Game game) {
        super.init(game);
        setGamePhase(Phases.Landerwerb);
    }

    /**
     * set the current active player state. Take a look at PlayerStates to gather more information's.
     */
    public void setPlayerState(Player player, PlayerStates playerState) {
        player.setState(playerState);
        if (playerState == PlayerStates.FirstTerritorySelection)
            player.setSelectedTerritory(null);
        engine.validateMouseButtons();
    }


    /**
     * Sets the territory's occupant state
     */
    public void setTerritoryOccupant(Territory territory, Player newOccupant) {
        Player previousOccupant = territory.getOccupant();
        assert previousOccupant != newOccupant;

        if (previousOccupant == null) {
            occupiedTerritories++;
            territory.setArmyCount(1);
        } else {
            previousOccupant.setTerritoryOwnership(territory, false);
        }

        newOccupant.setTerritoryOwnership(territory, true);
        territory.setOccupant(newOccupant);

        reload_ContinentOwners();
        recalculate_ReinforcementGains(newOccupant);

        if (gamePhase == Phases.Landerwerb && occupiedTerritories == data.getAllTerritories().size())
            setGamePhase(Phases.Reinforcement);

        engine.validateMouseButtons();
    }

    /**
     * Sets the territory's occupant state
     */
    public void reinforceTerritory(Territory territory) {
        Player occupant = territory.getOccupant();
        territory.setArmyCount(territory.getArmyCount() + 1);
        occupant.setReinforcements(occupant.getReinforcements() - 1);

        if (occupant.getReinforcements() == 0) {
            if (occupant == data.getHumanPlayer()) {
                setPlayerState(data.getHumanPlayer(), PlayerStates.Waiting);
                setPlayerState(data.getCompPlayer(), PlayerStates.Reinforcing);
            } else
                setGamePhase(Phases.AttackOrMove);
        }

        engine.requestRepaint();
    }


    /**
     * Rechecks which continent belongs to which user.
     */
    private void reload_ContinentOwners() {
        for (Continent continent : data.getAllContinents()) {
            Player owner = continent.getTerritories().get(0).getOccupant();
            if (owner == null) { // this continent belongs to nobody
                data.getCompPlayer().setContinentOwnerShip(continent, false);
                data.getHumanPlayer().setContinentOwnerShip(continent, false);
                continue;
            }
            boolean consistentOwnerShip = true;
            for (int i = 1; i < continent.getTerritories().size(); i++) {
                Territory territory = continent.getTerritories().get(i);
                if (owner != territory
                        .getOccupant()) { // if there are two different occupants the continent belongs to nobody
                    data.getHumanPlayer().setContinentOwnerShip(continent, false);
                    data.getCompPlayer().setContinentOwnerShip(continent, false);
                    consistentOwnerShip = false;
                    break;
                }
            }
            if (!consistentOwnerShip)
                continue;

            owner.setContinentOwnerShip(continent, true);
        }
    }


    /**
     * Recalculates the reinforcement the user should get in the next reinforcement phase.
     */
    private void recalculate_ReinforcementGains(Player player) {
        int gain = player.getOwnedTerritories().size() / 3;
        for (Continent continent : player.getOwnedContinents()) {
            gain += continent.getReinforcementBonus();
        }
        player.setReinforcementGain(gain);
    }

    /**
     * Gives the player the the reinforcement he actually should gain.
     */
    private void reassign_Reinforcements(Player player) {
        player.setReinforcements(player.getReinforcementGain());
    }


    /**
     * set the current selected territory and changes the player state accordingly.
     */
    public void setSelectedTerritory(Player player, Territory territory) {
        player.setSelectedTerritory(territory);
        if (territory == null) {
            setPlayerState(player, PlayerStates.FirstTerritorySelection);
        } else {
            setPlayerState(player, PlayerStates.FirstTerritorySelected);
        }
        engine.requestRepaint();
    }


    /**
     * Assigns a transfer movement to the player.
     */
    public void assignTransferMovement(Player player, Territory to) {
        Territory from = player.getSelectedTerritory();
        TacticalMovement move = player.getTransferMovement();
        boolean editCurrentMove = move != null && move.consitsOf(from, to);

        if (!editCurrentMove) { // new move will be created
            if (move != null)
                unassignMovement(move);

            move = new TacticalMovement(player, from, to, 1);
            player.setTransferMovement(move);
            from.decreaseArmyCount();
            if (from.getArmyCount() < 2) {
                setSelectedTerritory(data.getHumanPlayer(), null);
            }
            engine.requestRepaint();
            return;
        }

        //edit existing transfer

        if (from == move.from) {
            from.decreaseArmyCount();
            move.increaseArmys();

            if (from.getArmyCount() < 2) {
                setSelectedTerritory(data.getHumanPlayer(), null);
            }
        } else {
            to.increaseArmyCount();
            move.decreaseArmys();

            if (move.getArmyCount() == 0) {
                player.setTransferMovement(null);
                setSelectedTerritory(data.getHumanPlayer(), null);
            }
        }
        engine.requestRepaint();
    }

    /**
     * handles an attack from a players territory to an enemy's territory.
     */
    public void assignAttackMovement(Player p, Territory to) {
        Territory from = p.getSelectedTerritory();
        TacticalMovement move = p.getAttackMovement();


        if (move != null) {
            if (move.from == from && move.to == to) { // Same attack move was select again so this is interpreted as cancel attack move.
                unassignMovement(move);
                setSelectedTerritory(data.getHumanPlayer(), null);
                return;
            }
            unassignMovement(move);
        }

        move = new TacticalMovement(p, from, to, from.getArmyCount() > 3 ? 3 : from.getArmyCount() - 1);
        from.setArmyCount(from.getArmyCount() - move.getArmyCount());
        p.setAttackMovement(move);

        setSelectedTerritory(data.getHumanPlayer(), null);
        engine.requestRepaint();
    }


    /**
     * reverts the movement by reassigning the moving army back to the source territory.
     */
    private void unassignMovement(TacticalMovement move) {
        Territory from = move.from;
        from.setArmyCount(from.getArmyCount() + move.getArmyCount());
        if (move.owner.getAttackMovement() == move)
            move.owner.setAttackMovement(null);
        else
            move.owner.setTransferMovement(null);
        engine.requestRepaint();
    }
}
