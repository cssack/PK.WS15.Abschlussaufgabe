/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import bases.TacticalMovement;
import dataObjects.enums.PlayerStates;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A player data object stores all relevant data for a player.
 */
public class Player {
    private ArrayList<Continent> ownedContinents = new ArrayList<>();
    private ArrayList<Territory> ownedTerritories = new ArrayList<>();
    private int reinforcementGain;
    private int reinforcements;
    private PlayerStates state = PlayerStates.Waiting;
    private Territory selectedTerritory;
    private TacticalMovement transferMovement;
    private ArrayList<TacticalMovement> attackMovements = new ArrayList<>();


    /**
     * @return the players active state. Further details in PlayerStates.
     */
    public PlayerStates getState() {
        return state;
    }

    /**
     * Sets the current state of the player.
     */
    public void setState(PlayerStates state) {
        this.state = state;
    }

    /**
     * @return The amount of reinforcements the player gains on the next round.
     */
    public int getReinforcementGain() {
        return reinforcementGain;
    }

    /**
     * Updates the reinforcement gain for the player. It is calculated by following equation: ([owned Territories]/3) +
     * [Reinforcement bonus of all continents owned by the player]
     */
    public void updateReinforcementGain() {
        this.reinforcementGain = (this.getOwnedTerritories().size()/3)
                + this.getOwnedContinents().stream().collect(Collectors
                .summingInt(x -> x.getReinforcementBonus()));
    }

    /**
     * @return The amount of reinforcements the player can actually distribute on his territories.
     */
    public int getReinforcements() {
        return reinforcements;
    }

    /**
     * Sets the amount of reinforcements the player can distribute in reinforcement phase.
     */
    public void setReinforcements(int reinforcements) {
        this.reinforcements = reinforcements;
    }

    /**
     * @return a list of owned continents.
     */
    public ArrayList<Continent> getOwnedContinents() {
        return ownedContinents;
    }

    /**
     * @return a list of owned territories.
     */
    public ArrayList<Territory> getOwnedTerritories() {
        return ownedTerritories;
    }


    /**
     * Sets the ownership of the continent to the player or removes it
     *
     * @param continent   the targeting continent.
     * @param belongsToMe declares whether the continent belongs to the user or not
     */
    public void setContinentOwnerShip(Continent continent, boolean belongsToMe) {
        if (belongsToMe && !ownedContinents.contains(continent)) {
            ownedContinents.add(continent);
        } else if (!belongsToMe && ownedContinents.contains(continent)) {
            ownedContinents.remove(continent);
        }
    }

    /**
     * Set or unset the ownership for the territory.
     *
     * @param belongsToMe declares whether the territory belongs to the user or not
     */
    public void setTerritoryOwnership(Territory territory, boolean belongsToMe) {
        if (belongsToMe && !ownedTerritories.contains(territory)) {
            ownedTerritories.add(territory);
            territory.setOccupant(this);
        } else if (!belongsToMe && ownedTerritories.contains(territory)) {
            ownedTerritories.remove(territory);
        }
    }

    /**
     * @return the current selected territory.
     */
    public Territory getSelectedTerritory() {
        return selectedTerritory;
    }

    /**
     * Set the selected territory.
     */
    public void setSelectedTerritory(Territory selectedTerritory) {
        this.selectedTerritory = selectedTerritory;
    }

    /**
     * @return the current attack movements for the user (assumed not be null). An attack movement
     * describes the attack of current players army from one territory to an enemy territory.
     */
    public ArrayList<TacticalMovement> getAttackMovements() {
        return attackMovements;
    }

    /**
     * Adds another attack movement to the player.
     *
     * @param attackMovement assumed not to be null.
     */
    public void addAttackMovement(TacticalMovement attackMovement) {
        assert attackMovement != null;
        this.attackMovements.add(attackMovement);
    }

    /**
     * Removes an attack move from the player.
     *
     * @param attackMovement can be null if no attack is selected by the user.
     */
    public void removeAttackMovement(TacticalMovement attackMovement) {
        assert attackMovement != null;
        this.attackMovements.remove(attackMovement);
    }

    /**
     * Clear attack moves from the player.
     */
    public void clearAttackMovements() {
        this.attackMovements.clear();
    }

    /**
     * @return the current transport movement for the user (can be null if no transport is selected). A transfer
     * movement describes the transfer of army's from one territory to another.
     */
    public TacticalMovement getTransferMovement() {
        return transferMovement;
    }

    /**
     * Sets the current transfer move for the player.
     *
     * @param transfer can be null if no transfer is selected by the user.
     */
    public void setTransferMovement(TacticalMovement transfer) {
        this.transferMovement = transfer;
    }

    /**
     * checks if the given continent belongs to the player and add/removes the continent
     * from the list of owned continents if necessary.
     */
    public void evaluateContinentOwnership(Continent continent) {
        if (this.getOwnedTerritories().containsAll(continent.getTerritories())) {
            this.setContinentOwnerShip(continent, true);
        } else {
            this.setContinentOwnerShip(continent, false);
        }
        this.updateReinforcementGain();
    }
}
