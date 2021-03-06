/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import bases.TacticalMovement;
import dataObjects.Player;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerStates;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**
 * The game state is accountable for all changes made to the data objects. Each change to the data objects should be
 * done over the GameState class. This helps to keep track of actual changes and populating repaint requests to the
 * engine.
 */
public class GameState extends GameBase {
    Random rand = new Random();
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
    public void setGamePhase(Phases phase) {
        gamePhase = phase;
        switch (gamePhase) {
            case Landerwerb:
                setPlayerState(data.getHumanPlayer(), PlayerStates.Reinforcing);
                break;
            case Reinforcement:
                data.getHumanPlayer().clearAttackMovements();
                data.getCompPlayer().clearAttackMovements();
                data.getHumanPlayer().setTransferMovement(null);
                data.getCompPlayer().setTransferMovement(null);

                reassign_Reinforcements(data.getHumanPlayer());
                reassign_Reinforcements(data.getCompPlayer());

                if (data.getHumanPlayer().getReinforcements() != 0) {
                    setPlayerState(data.getHumanPlayer(), PlayerStates.Reinforcing);
                    setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
                } else if (data.getCompPlayer().getReinforcements() != 0) {
                    setPlayerState(data.getHumanPlayer(), PlayerStates.Waiting);
                    setPlayerState(data.getCompPlayer(), PlayerStates.Reinforcing);
                } else {
                    //this could be an error no reinforcements @ each player
                    setGamePhase(Phases.AttackOrMove);
                }
                break;
            case AttackOrMove:
                setPlayerState(data.getHumanPlayer(), PlayerStates.FirstTerritorySelection);
                setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
                break;
            case QuickOverViewBefore:
                setSelectedTerritory(data.getHumanPlayer(), null);
                setSelectedTerritory(data.getCompPlayer(), null);
                setPlayerState(data.getHumanPlayer(), PlayerStates.Waiting);
                setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
                break;
            case Fortifying:
                setPlayerState(data.getHumanPlayer(), PlayerStates.Fortifying);
                setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
                break;
            default:
                break;
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
            previousOccupant.evaluateContinentOwnership(territory.getContinent());
        }

        newOccupant.setTerritoryOwnership(territory, true);
        newOccupant.evaluateContinentOwnership(territory.getContinent());

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
            if (occupant == data.getHumanPlayer() && data.getCompPlayer().getReinforcements() != 0) {
                setPlayerState(data.getHumanPlayer(), PlayerStates.Waiting);
                setPlayerState(data.getCompPlayer(), PlayerStates.Reinforcing);
            } else
                setGamePhase(Phases.AttackOrMove);
        }

        engine.requestRepaint();
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
        boolean editCurrentMove = move != null && move.consistsOf(from, to);

        if (!editCurrentMove) { // new move will be created
            if (move != null)
                unassignMovement(move);

            move = new TacticalMovement(player, from, to, 1);
            player.setTransferMovement(move);
            from.decreaseArmyCount();
            if (from.getArmyCount() < 2) {
                setSelectedTerritory(data.getHumanPlayer(), null);
            }
            messages.newTacticalMovementMessage(move);

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
        ArrayList<TacticalMovement> moves = p.getAttackMovements();

        Optional<TacticalMovement> first = moves.stream().filter(x -> x.from == from && x.to == to).findFirst();


        if (first.isPresent()) {
            unassignMovement(first.get());
            setSelectedTerritory(data.getHumanPlayer(), null);
            return;
        }

        TacticalMovement tacticalMovement = new TacticalMovement(p, from, to, from.getArmyCount() > 3 ? 3 : from
                .getArmyCount() - 1);
        from.setArmyCount(from.getArmyCount() - tacticalMovement.getArmyCount());
        p.addAttackMovement(tacticalMovement);

        setSelectedTerritory(data.getHumanPlayer(), null);

        messages.newTacticalMovementMessage(tacticalMovement);

        engine.requestRepaint();


    }

    /**
     * reverts the movement by reassigning the moving army back to the source territory.
     */
    private void unassignMovement(TacticalMovement move) {
        Territory from = move.from;
        from.setArmyCount(from.getArmyCount() + move.getArmyCount());
        if (move.owner.getTransferMovement() == move)
            move.owner.setTransferMovement(null);
        else
            move.owner.removeAttackMovement(move);
        engine.requestRepaint();
    }

    /**
     * Executes the tactical moves.
     */
    public void executePlayerMovements() {
        executeTransferMove(data.getCompPlayer().getTransferMovement());
        executeTransferMove(data.getHumanPlayer().getTransferMovement());

        for (TacticalMovement movement : data.getCompPlayer().getAttackMovements()) {
            executeAttackMove(movement);
        }
        for (TacticalMovement movement : data.getHumanPlayer().getAttackMovements()) {
            executeAttackMove(movement);
        }

    }

    private void executeAttackMove(TacticalMovement tm) {
        if (tm == null)
            return;

        int attackingArmy = tm.getArmyCount();
        int originalDefensingArmy = tm.to.getArmyCount() > 2 ? 2 : tm.to.getArmyCount();
        int defensingArmy = originalDefensingArmy;

        while (attackingArmy != 0 && defensingArmy != 0) {
            int defenseCube = rand.nextInt(7);
            int attackCube = rand.nextInt(7);
            if (defenseCube < attackCube)
                defensingArmy--;
            else if (attackCube < defenseCube)
                attackingArmy--;
        }

        if (attackingArmy > 0) {
            setTerritoryOccupant(tm.to, tm.from.getOccupant());
            tm.to.setArmyCount(attackingArmy);
        } else {
            tm.to.setArmyCount(tm.to.getArmyCount() - (originalDefensingArmy - defensingArmy));
        }
        engine.requestRepaint();
    }

    private void executeTransferMove(TacticalMovement tm) {
        if (tm == null)
            return;

        tm.to.setArmyCount(tm.to.getArmyCount() + tm.getArmyCount());
        engine.requestRepaint();
    }

    /**
     * Returns true if any of the tactical moves contains the territory.
     */
    public boolean belongsToTactialMove(Territory t) {
        boolean compAttackContained = data.getCompPlayer().getAttackMovements().stream().anyMatch(x -> x.contains(t));
        boolean humanAttackContained = data.getHumanPlayer().getAttackMovements().stream().anyMatch(x -> x.contains(t));
        TacticalMovement compTransfer = data.getCompPlayer().getTransferMovement();
        TacticalMovement humanTransfer = data.getHumanPlayer().getTransferMovement();

        return compAttackContained ||
                humanAttackContained ||
                (compTransfer != null && compTransfer.contains(t)) ||
                (humanTransfer != null && humanTransfer.contains(t));
    }

    /**
     * Returns true if any of the tactical moves contains the territory.
     */
    public boolean isTactialMoveTarget(Territory t) {
        boolean compAttackTarget = data.getCompPlayer().getAttackMovements().stream().anyMatch(x -> x.to == t);
        boolean humanAttackTarget = data.getHumanPlayer().getAttackMovements().stream().anyMatch(x -> x.to == t);
        TacticalMovement compTransfer = data.getCompPlayer().getTransferMovement();
        TacticalMovement humanTransfer = data.getHumanPlayer().getTransferMovement();

        return compAttackTarget ||
                humanAttackTarget ||
                (compTransfer != null && compTransfer.to == t) ||
                (humanTransfer != null && humanTransfer.to == t);
    }

    /**
     * Returns true if any of the attack movements of the human player contains the territory as a target, and the attack was a victory (to == from);
     */
    public boolean isHumanVictoryTarget(Territory t) {
        return data.getHumanPlayer().getAttackMovements().stream().anyMatch(x -> x.to == t && x.to.getOccupant() == x.from.getOccupant() && x.to.getOccupant() == data.getHumanPlayer());
    }
}
