/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.util.ArrayList;

/**
 * Created by chris on 07.01.2016.
 * A player data object stores all relevant data for a player.
 */
public class Player {
    private ArrayList<Continent> ownedContinents = new ArrayList<>();
    private int reinforcementGain;
    private int reinforcementsAvailable;


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
}
