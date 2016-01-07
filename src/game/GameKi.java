/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;

import java.util.ArrayList;
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
        Territory chosen = getRandomTerritory(game.getData().getAllTerritories());
        while (chosen.getOccupant() != null) {
            chosen = getRandomTerritory(game.getData().getAllTerritories());
        }
        state.setTerritoryOccupant(chosen, data.getCompPlayer());
    }

    public void ReinforceTerritorys() {
        while (data.getCompPlayer().getReinforcements() > 0) {
            state.reinforceTerritory(getRandomTerritory(data.getCompPlayer().getOwnedTerritories()));
        }
    }

    private Territory getRandomTerritory(ArrayList<Territory> from) {
        return from.get(rand.nextInt(from.size()));
    }
}
