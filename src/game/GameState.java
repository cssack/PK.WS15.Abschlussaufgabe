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
    private Territory mouseOverTerritory;
    private boolean mouseTargetClickable;
    private int occupiedTerritories;
    private boolean repaintRequired;
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
        if (phase == Phases.Landerwerb) {
            setPlayerState(data.getHumanPlayer(), PlayerStates.Reinforcing);
        } else if (phase == Phases.Reinforcement) {
            reassign_Reinforcements(data.getHumanPlayer());
            reassign_Reinforcements(data.getCompPlayer());

            setPlayerState(data.getHumanPlayer(), PlayerStates.Reinforcing);
            setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
        } else if (phase == Phases.AttackOrMove) {
            reassign_Reinforcements(data.getHumanPlayer());
            reassign_Reinforcements(data.getCompPlayer());

            setPlayerState(data.getHumanPlayer(), PlayerStates.FirstTerritorySelection);
            setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
        }

        reload_MouseTargetClickable();
        repaintRequired = true;
    }

    /**
     * @return the currently occupied territories. This field make only sense in the 'Landerwerb' phase
     */
    public int getOccupiedTerritories() {
        return occupiedTerritories;
    }

    /**
     * @return the territory where currently the mouse is over.
     */
    public Territory getMouseOverTerritory() {
        return mouseOverTerritory;
    }

    /**
     * Sets the current territory where the mouse is over.
     */
    public void setMouseOverTerritory(Territory territory) {
        if (this.mouseOverTerritory == territory)
            return;
        this.mouseOverTerritory = territory;
        reload_MouseTargetClickable();
        repaintRequired = true;
    }

    /**
     * @return true if the under laying object of the mouse is click able.
     */
    public boolean isMouseTargetClickable() {
        return mouseTargetClickable;
    }

    /**
     * Sets the repaint required field to false.
     */
    public void resetRepaintRequired() {
        this.repaintRequired = false;
    }

    /**
     * @return true if some operation between resetRepaintRequired and isRepaintRequired needs an repaint of the form
     */
    public boolean isRepaintRequired() {
        return repaintRequired;
    }

    /**
     * set the current active player state. Take a look at PlayerStates to gather more information's.
     */
    public void setPlayerState(Player player, PlayerStates playerState) {
        player.setState(playerState);
        if (playerState == PlayerStates.FirstTerritorySelection)
            player.setSelectedTerritory(null);
        reload_MouseTargetClickable();
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
        repaintRequired = true;
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
        reload_MouseTargetClickable();
    }

    /**
     * Sets the territory's occupant state
     */
    public void reinforceTerritory(Territory territory) {
        Player occupant = territory.getOccupant();
        territory.setArmyCount(territory.getArmyCount() + 1);
        occupant.setReinforcements(occupant.getReinforcements() - 1);


        repaintRequired = true;
    }

    /**
     * Check if the current territory is clickable by the mouse.
     */
    private void reload_MouseTargetClickable() {
        boolean newVal = false;
        Player human = data.getHumanPlayer();
        PlayerStates humanPhase = human.getState();

        if (humanPhase == PlayerStates.Waiting) {
            newVal = false;
        } else {
            if (gamePhase == Phases.Landerwerb) {
                newVal = mouseOverTerritory != null && mouseOverTerritory.getOccupant() == null;
            } else if (humanPhase == PlayerStates.Reinforcing) {
                newVal = mouseOverTerritory != null && mouseOverTerritory.getOccupant() == human;
            } else if (humanPhase == PlayerStates.FirstTerritorySelection) {
                if (mouseOverTerritory == null)
                    newVal = false;
                else {
                    boolean isAttacker = human.getAttackMovement() != null && human
                            .getAttackMovement().from == mouseOverTerritory;
                    boolean isTransportTarget = human.getTransferMovement() != null && human
                            .getTransferMovement().to == mouseOverTerritory;
                    boolean isValidFirstSelection = mouseOverTerritory.getOccupant() == human && mouseOverTerritory
                            .getArmyCount() > 1;

                    newVal = isAttacker || isTransportTarget || isValidFirstSelection;
                }
            } else if (humanPhase == PlayerStates.FirstTerritorySelected) {
                if (mouseOverTerritory == null)
                    newVal = false;
                else {
                    Territory selectedTerritory = human.getSelectedTerritory();

                    boolean isNeighbor = selectedTerritory.getNeighbors().contains(mouseOverTerritory);
                    boolean isHuman = mouseOverTerritory.getOccupant() == human;
                    boolean hasMoreThenOneArmy = mouseOverTerritory.getArmyCount() > 1;
                    boolean isValidTransportTarget = human.getTransferMovement() == null || human
                            .getTransferMovement()
                            .consitsOf(data.getHumanPlayer().getSelectedTerritory(), mouseOverTerritory);

                    newVal = (isHuman && hasMoreThenOneArmy) || //Used for left mouse button reselection
                            (isNeighbor && isHuman && isValidTransportTarget) || //Used for right mouse button available
                            (isNeighbor && !isHuman); //used for attackable territories
                }
            }
        }


        if (newVal == mouseTargetClickable)
            return;

        mouseTargetClickable = newVal;
        repaintRequired = true;
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
     * Assigns a transfer movement to the player.
     */
    public void assignTransferMovement(Player player, Territory to) {
        Territory from = player.getSelectedTerritory();
        TacticalMovement move = player.getTransferMovement();

        if (move == null) {
            move = new TacticalMovement(from, to, 1);
            player.setTransferMovement(move);
            from.decreaseArmyCount();
            if (from.getArmyCount() < 2) {
                setSelectedTerritory(data.getHumanPlayer(), null);
            }
            repaintRequired = true;
        } else if (move.consitsOf(from, to)) {
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
            repaintRequired = true;
        }
    }

    /**
     * handles an attack from a players territory to an enemy's territory.
     */
    public void assignAttackMovement(Player p, Territory to) {
        Territory from = p.getSelectedTerritory();
        TacticalMovement attackMovement = p.getAttackMovement();

        if (attackMovement != null) {
            if (attackMovement.from == from && attackMovement.to == to) {
                unassignAttackMovement(p);
                setSelectedTerritory(data.getHumanPlayer(), null);
                return; // Same attack move was select again so this is interpreted as cancle attack move.
            }
            unassignAttackMovement(p);
        }

        attackMovement = new TacticalMovement(from, to, from.getArmyCount() > 3 ? 3 : from.getArmyCount() - 1);
        from.setArmyCount(from.getArmyCount() - attackMovement.getArmyCount());
        p.setAttackMovement(attackMovement);

        setSelectedTerritory(data.getHumanPlayer(), null);
        repaintRequired = true;
    }


    /**
     * reverts the attack movement by reassigning the moving army back to the source territory.
     */
    private void unassignAttackMovement(Player p) {
        TacticalMovement attackMovement = p.getAttackMovement();
        Territory from = attackMovement.from;
        from.setArmyCount(from.getArmyCount() + attackMovement.getArmyCount());
        p.setAttackMovement(null);

        repaintRequired = true;
    }
}
