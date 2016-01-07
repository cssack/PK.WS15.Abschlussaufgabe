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
    private int reinforcementGain;
    private int reinforcementsAvailable;
    private int territoriesCount;
    private PlayerActions action = PlayerActions.ArmyReinforcement;

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
     * @return the current amount of territories owned by this player.
     */
    public int getTerritoriesCount() {
        return territoriesCount;
    }

    public void increaseTerritoriesCount() {
        this.territoriesCount++;
    }

    public void decreaseTerritoriesCount() {
        this.territoriesCount--;
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
    public int getReinforcementsAvailable() {
        return reinforcementsAvailable;
    }

    public void setReinforcementsAvailable(int reinforcementsAvailable) {
        this.reinforcementsAvailable = reinforcementsAvailable;
    }

    /**
     * @return a list of owned continents.
     */
    public ArrayList<Continent> getOwnedContinents() {
        return ownedContinents;
    }


    public void setContinentOwnerShip(Continent continent, boolean belongsToMe) {
        assert ownedContinents.contains(continent);
        if (belongsToMe && !ownedContinents.contains(continent)) {
            ownedContinents.add(continent);
        } else if (!belongsToMe && ownedContinents.contains(continent)) {
            ownedContinents.remove(continent);
        }
    }
}
