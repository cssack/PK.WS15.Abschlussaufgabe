/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;

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

    public void AttackAndMove() {
        while (data.getCompPlayer().getAttackMovement() == null || (data.getCompPlayer()
                .getTransferMovement() == null && data.getCompPlayer().getOwnedTerritories().size() > 1)) {
            Territory first = data.getRandomTerritory(data.getCompPlayer().getOwnedTerritories());
            if (first.getArmyCount() < 2)
                continue;
            state.setSelectedTerritory(data.getCompPlayer(), first);
            Territory second = data.getRandomTerritory(first.getNeighbors());

            if (data.getCompPlayer().getTransferMovement() == null && first.getOccupant() == second.getOccupant())
                state.assignTransferMovement(data.getCompPlayer(), second);
            else if (data.getCompPlayer().getAttackMovement() == null && first.getOccupant() != second.getOccupant())
                state.assignAttackMovement(data.getCompPlayer(), second);

        }
    }
}
