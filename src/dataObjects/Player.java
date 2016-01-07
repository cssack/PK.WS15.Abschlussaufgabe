/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import dataObjects.enums.PlayerActions;

import java.util.ArrayList;

/**
 * Created by chris on 07.01.2016.
 * A player data object stores all relevant data for a player.
 */
public class Player {


    private ArrayList<Continent> ownedContinents = new ArrayList<>();
    private ArrayList<Territory> ownedTerritories = new ArrayList<>();
    private int reinforcementGain;
    private int reinforcements;
    private PlayerActions action = PlayerActions.Reinforcement;

    /**
     * @return the players active state. Further details in PlayerActions.
     */
    public PlayerActions getAction() {
        return action;
    }

    public void setAction(PlayerActions action) {
        this.action = action;
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

}
