/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package engine;

import bases.GameBase;
import dataObjects.Territory;
import dataObjects.enums.Occupants;

import java.util.Random;

/**
 * Created by chris on 07.01.2016.
 * The engine used for the pc player.
 * This is the place where the KI can be configured.
 */
public class PcPlayerEngine extends GameBase {
    private final Random rand = new Random();

    public PcPlayerEngine() {
    }

    public void ChooseSomeTerritory() {
        Territory chosen = getRandomTerritory();
        while (chosen.getOccupant() != Occupants.NotDef) {
            chosen = getRandomTerritory();
        }
        state.setTerritoryOccupant(chosen, Occupants.Pc);
    }

    private Territory getRandomTerritory() {
        return game.getData().getAllTerritories().get(rand.nextInt(data.getAllTerritories().size()));
    }
}
