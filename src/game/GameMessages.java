/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Player;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerStates;

/**
 * Used to provide messages to the user.
 */
@SuppressWarnings("ALL")
public class GameMessages extends GameBase {

    private PlayerStates compPlayerAction() {
        return data.getCompPlayer().getState();
    }


    public String getCurrentPhase() {


        if (state.getGamePhase() == Phases.Landerwerb)
            return "Wählen Sie ein Land aus. (" + ((data.getAllTerritories().size() - state
                    .getOccupiedTerritories()) / 2) + " verfügbar). Order Leertaste für zufällige Verteilung";


        Player human = data.getHumanPlayer();
        PlayerStates humanPhase = human.getState();

        if (humanPhase == PlayerStates.Reinforcing)
            return "Verteilen Sie noch " + human.getReinforcements() + " Armeen.";
        if (humanPhase == PlayerStates.FirstTerritorySelection)
            return "Wählen Sie ein Territorium aus von dem weg Sie einen Angriff oder Transport starten wollen.";
        if (humanPhase == PlayerStates.FirstTerritorySelected)
            return "Wählen Sie ein benachbartes Ziel aus (Truppentransport(rechte Maustaste) oder Angriff).";
        if (humanPhase == PlayerStates.Waiting)
            return "Bitte warten! Der Computer denkt gerade";
        return "";
    }
}
