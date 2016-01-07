/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.game;

import bases.GameBase;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerActions;

/**
 * Created by chris on 07.01.2016.
 * Used to give the user a descriptive feedback over the current state.
 */
public class GameMessages extends GameBase {
    private PlayerActions humanPlayerAction() {
        return data.getHumanPlayer().getAction();
    }

    private PlayerActions compPlayerAction() {
        return data.getCompPlayer().getAction();
    }


    public String getCurrentPhase() {
        if (state.getGamePhase() == Phases.Landerwerb)
            return "Wählen Sie ein Land aus. (" + (data.getAllTerritories().size() - state
                    .getOccupiedTerritories()) + " verfügbar)";

        if (humanPlayerAction() == PlayerActions.ArmyReinforcement)
            return "Verteilen Sie noch " + data.getHumanPlayer().getReinforcementsAvailable() + " Armeen.";
        if (humanPlayerAction() == PlayerActions.ArmySelected)
            return "Wählen Sie ein benachbartes Ziel aus.";
        if (humanPlayerAction() == PlayerActions.ArmyAttacked)
            return "";
        return "";
    }
}
