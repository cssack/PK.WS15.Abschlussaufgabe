/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import bases.TacticalMovement;
import dataObjects.Territory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

        List<Territory> possibleAttackSources = possibleSourceTerritories.stream()
                .filter(sr -> sr.getNeighbors().stream().anyMatch(x -> x.getOccupant() == data.getHumanPlayer()))
                .collect(Collectors.toList());

        for (int i = 0; i < possibleAttackSources.size(); i++) {
            Territory src = data.getRandomTerritory(possibleAttackSources);
            Territory dst = data.getRandomTerritory(src.getNeighbors().stream()
                    .filter(x -> x.getOccupant() == data.getHumanPlayer()).collect(Collectors.toList()));

            if (data.getCompPlayer().getAttackMovements().stream()
                    .anyMatch(x -> x.to == dst || x.from == src)) // already attacked or same source
                continue;

            state.setSelectedTerritory(data.getCompPlayer(), src);
            state.assignAttackMovement(data.getCompPlayer(), dst);
        }


        List<Territory> possibleTransferSources = data.getCompPlayer().getOwnedTerritories().stream()
                .filter(sr -> sr.getArmyCount() > 1 && sr.getNeighbors().stream()
                        .anyMatch(x -> x.getOccupant() == data.getCompPlayer())).collect(Collectors.toList());

        if (possibleTransferSources.size() != 0) {
            Territory src = data.getRandomTerritory(possibleTransferSources);
            Territory dst = data.getRandomTerritory(src.getNeighbors().stream()
                    .filter(x -> x.getOccupant() == data.getCompPlayer()).collect(Collectors.toList()));
            state.setSelectedTerritory(data.getCompPlayer(), src);
            state.assignTransferMovement(data.getCompPlayer(), dst);
        }
    }

    public void FortifyTerritories() {
        for (TacticalMovement move: data.getCompPlayer().getAttackMovements()) {
            if (move != null && move.to.getOccupant() == data.getCompPlayer() && move.from.getOccupant() == data.getCompPlayer()) {
                int nFortification = rand.nextInt(move.from.getArmyCount());
                move.to.increaseArmyCountByN(nFortification);
                move.from.decreaseArmyCountByN(nFortification);
            }
        }
    }
}
