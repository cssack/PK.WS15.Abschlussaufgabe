/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;

import java.util.Random;

/**
 * Created by chris on 07.01.2016.
 * The engine used for the pc player.
 * This is the place where the KI can be configured.
 */
public class GameKi extends GameBase {
    private final Random rand = new Random();

    public GameKi() {
    }

    public void ChooseSomeTerritory() {
        Territory chosen = getRandomTerritory();
        while (chosen.getOccupant() != null) {
            chosen = getRandomTerritory();
        }
        state.setTerritoryOccupant(chosen, data.getCompPlayer());
    }

    private Territory getRandomTerritory() {
        return game.getData().getAllTerritories().get(rand.nextInt(data.getAllTerritories().size()));
    }
}
