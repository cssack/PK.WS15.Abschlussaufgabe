/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The engine used for the pc player. This is the place where the KI can be configured.
 */
public class GameKi extends GameBase {

    public GameKi() {
    }

    public void ChooseSomeTerritory() {
        state.setTerritoryOccupant(data.getRandomUnassignedTerritory(), data.getCompPlayer());
    }

    public void ReinforceTerritories() {
        while (data.getCompPlayer().getReinforcements() > 0) {
            state.reinforceTerritory(data.getRandomTerritory(data.getCompPlayer().getOwnedTerritories()));
        }
    }

    public void AttackAndMove() {
        List<Territory> possibleSourceTerritories = data.getCompPlayer().getOwnedTerritories().stream()
                .filter(x -> x.getArmyCount() > 1).collect(Collectors.toList());

        if (possibleSourceTerritories.size() == 0)
            return; // there is no territory from which an action could be started.

        for (int i = 0; i < possibleSourceTerritories.size(); i++) {
            Territory first = data.getRandomTerritory(data.getCompPlayer().getOwnedTerritories());
            if (first.getArmyCount() < 2)
                continue;
            state.setSelectedTerritory(data.getCompPlayer(), first);
            Territory second = data.getRandomTerritory(first.getNeighbors());

            if (data.getCompPlayer().getTransferMovement() == null && first.getOccupant() == second.getOccupant())
                state.assignTransferMovement(data.getCompPlayer(), second);
            else if (first.getOccupant() != second.getOccupant())
                state.assignAttackMovement(data.getCompPlayer(), second);
        }
    }
}
