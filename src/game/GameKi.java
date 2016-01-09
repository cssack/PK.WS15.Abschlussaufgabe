/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;
import dataObjects.enums.PlayerStates;

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

        state.setPlayerState(data.getCompPlayer(), PlayerStates.Reinforcing);

        Territory chosen = getRandomTerritory(game.getData().getAllTerritories());
        while (chosen.getOccupant() != null) {
            chosen = getRandomTerritory(game.getData().getAllTerritories());
        }
        state.setTerritoryOccupant(chosen, data.getCompPlayer());

        state.setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
    }

    public void ReinforceTerritorys() {
        state.setPlayerState(data.getCompPlayer(), PlayerStates.Reinforcing);

        while (data.getCompPlayer().getReinforcements() > 0) {
            state.reinforceTerritory(getRandomTerritory(data.getCompPlayer().getOwnedTerritories()));
        }
        state.setPlayerState(data.getCompPlayer(), PlayerStates.Waiting);
    }

    private Territory getRandomTerritory(ArrayList<Territory> from) {
        return from.get(rand.nextInt(from.size()));
    }
}
