/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerPhases;

/**
 * Created by chris on 07.01.2016. Used to give the user a descriptive feedback over the current state.
 */
@SuppressWarnings("ALL")
public class GameMessages extends GameBase {
    private PlayerPhases humanPlayerAction() {
        return data.getHumanPlayer().getPhase();
    }

    private PlayerPhases compPlayerAction() {
        return data.getCompPlayer().getPhase();
    }


    public String getCurrentPhase() {
        if (state.getGamePhase() == Phases.Landerwerb)
            return "Wählen Sie ein Land aus. (" + (data.getAllTerritories().size() - state
                    .getOccupiedTerritories()) + " verfügbar)";

        if (humanPlayerAction() == PlayerPhases.Reinforcing)
            return "Verteilen Sie noch " + data.getHumanPlayer().getReinforcements() + " Armeen.";
        if (humanPlayerAction() == PlayerPhases.FirstTerritorySelection)
            return "Wählen Sie ein Territorium aus von dem weg Sie einen Angriff oder Transport starten wollen.";
        if (humanPlayerAction() == PlayerPhases.FirstTerritorySelected)
            return "Wählen Sie ein benachbartes Ziel aus (Truppentransport(rechte Maustaste) oder Angriff).";
        if (humanPlayerAction() == PlayerPhases.AttackedWin)
            return "";
        if (humanPlayerAction() == PlayerPhases.Waiting)
            return "Bitte warten! Der Computer denkt gerade";
        return "";
    }
}
