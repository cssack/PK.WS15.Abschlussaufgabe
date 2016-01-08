/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;
import dataObjects.enums.PlayerPhases;

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

        state.setPlayerPhase(data.getCompPlayer(), PlayerPhases.Reinforcing);

        Territory chosen = getRandomTerritory(game.getData().getAllTerritories());
        while (chosen.getOccupant() != null) {
            chosen = getRandomTerritory(game.getData().getAllTerritories());
        }
        state.setTerritoryOccupant(chosen, data.getCompPlayer());

        state.setPlayerPhase(data.getCompPlayer(), PlayerPhases.Waiting);
    }

    public void ReinforceTerritorys() {
        state.setPlayerPhase(data.getCompPlayer(), PlayerPhases.Reinforcing);

        while (data.getCompPlayer().getReinforcements() > 0) {
            state.reinforceTerritory(getRandomTerritory(data.getCompPlayer().getOwnedTerritories()));
        }
        state.setPlayerPhase(data.getCompPlayer(), PlayerPhases.Waiting);
    }

    private Territory getRandomTerritory(ArrayList<Territory> from) {
        return from.get(rand.nextInt(from.size()));
    }
}
