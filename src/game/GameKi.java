/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;

import java.util.ArrayList;
import java.util.Random;

/**
 * The engine used for the pc player. This is the place where the KI can be configured.
 */
public class GameKi extends GameBase {
    private final Random rand = new Random();

    public GameKi() {
    }

    public void ChooseSomeTerritory() {
        state.setTerritoryOccupant(data.getRandomUnassignedTerritory(), data.getCompPlayer());
    }

    public void ReinforceTerritorys() {
        while (data.getCompPlayer().getReinforcements() > 0) {
            state.reinforceTerritory(data.getRandomTerritory(data.getCompPlayer().getOwnedTerritories()));
        }
    }

    private Territory getRandomTerritory(ArrayList<Territory> from) {
        return from.get(rand.nextInt(from.size()));
    }
}
