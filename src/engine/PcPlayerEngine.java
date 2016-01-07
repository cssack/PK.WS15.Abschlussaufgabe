/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package engine;

import dataObjects.Territory;
import dataObjects.enums.Occupants;
import dataObjects.game.Game;

import java.util.Random;

/**
 * Created by chris on 07.01.2016.
 * The engine used for the pc player.
 * This is the place where the KI can be configured.
 */
public class PcPlayerEngine {
    private Game game;
    private Random rand = new Random();

    public PcPlayerEngine(Game game) {
        this.game = game;
    }

    public void ChooseSomeTerritory() {
        Territory chosen = getRandomTerritory();
        while (chosen.getOccupant() != Occupants.NotDef) {
            chosen = getRandomTerritory();
        }
        game.getState().setTerritoryOccupant(chosen, Occupants.Pc);
    }

    private Territory getRandomTerritory() {
        return game.getData().getAllTerritories().get(rand.nextInt(game.getData().getAllTerritories().size()));
    }
}
