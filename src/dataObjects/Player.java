/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import bases.TacticalMovement;
import dataObjects.enums.PlayerPhases;

import java.util.ArrayList;

/**
 * A player data object stores all relevant data for a player.
 */
public class Player {


    private ArrayList<Continent> ownedContinents = new ArrayList<>();
    private ArrayList<Territory> ownedTerritories = new ArrayList<>();
    private int reinforcementGain;
    private int reinforcements;
    private PlayerPhases phase = PlayerPhases.Waiting;
    private Territory selectedTerritory;
    private TacticalMovement transportMovement;
    private TacticalMovement attackMovement;

    public TacticalMovement getTransportMovement() {
        return transportMovement;
    }

    public void setTransportMovement(TacticalMovement transport) {
        this.transportMovement = transport;
    }

    /**
     * @return the players active state. Further details in PlayerActions.
     */
    public PlayerPhases getPhase() {
        return phase;
    }

    public void setPhase(PlayerPhases phase) {
        this.phase = phase;
    }

    /**
     * @return The amount of reinforcements the player gains on the next round.
     */
    public int getReinforcementGain() {
        return reinforcementGain;
    }

    public void setReinforcementGain(int reinforcementGain) {
        this.reinforcementGain = reinforcementGain;
    }

    /**
     * @return The amount of reinforcements the player can distribute on his territory.
     */
    public int getReinforcements() {
        return reinforcements;
    }

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

    public void setContinentOwnerShip(Continent continent, boolean belongsToMe) {
        if (belongsToMe && !ownedContinents.contains(continent)) {
            ownedContinents.add(continent);
        } else if (!belongsToMe && ownedContinents.contains(continent)) {
            ownedContinents.remove(continent);
        }
    }

    public void setTerritoryOwnership(Territory territory, boolean belongsToMe) {
        if (belongsToMe && !ownedTerritories.contains(territory)) {
            ownedTerritories.add(territory);
        } else if (!belongsToMe && ownedTerritories.contains(territory)) {
            ownedTerritories.remove(territory);
        }
    }

    public Territory getSelectedTerritory() {
        return selectedTerritory;
    }

    public void setSelectedTerritory(Territory selectedTerritory) {
        this.selectedTerritory = selectedTerritory;
    }

    public TacticalMovement getAttackMovement() {
        return attackMovement;
    }

    public void setAttackMovement(TacticalMovement attackMovement) {
        this.attackMovement = attackMovement;
    }
}
