/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Player;
import dataObjects.enums.PlayerStates;

/**
 * Used to provide messages to the user.
 */
@SuppressWarnings("ALL")
public class GameMessages extends GameBase {

    private PlayerStates compPlayerAction() {
        return data.getCompPlayer().getState();
    }

    public String getCurrentToolbarText() {
        switch (state.getGamePhase()) {
            case Landerwerb:
                return "Wählen Sie ein Land aus. (" + ((data.getAllTerritories().size() - state
                        .getOccupiedTerritories()) / 2) + " verfügbar). Oder Leertaste für zufällige Verteilung.";
            case QuickOverViewBefore:
                return "Taktische movements aller Spieler.";
            case QuickOverViewAfter:
                return "Ergebnisse der Angriffs- und Transferphase.";
            case End:
                return data.getCompPlayer().getOwnedTerritories().size() == 0 ? "GEWONNEN!" : "VERLOREN!";
            default:
                break;
        }

        Player human = data.getHumanPlayer();

        switch (human.getState()) {
            case Waiting:
                return "Bitte warten! Der Computer denkt gerade";
            case Reinforcing:
                return "Verteilen Sie noch " + human
                        .getReinforcements() + " Armeen. Oder Leertaste für zufällige Verteilung.";
            case FirstTerritorySelection:
                return "Wählen Sie ein Territorium aus von dem weg Sie einen Angriff oder Transport starten wollen.";
            case FirstTerritorySelected:
                return "Wählen Sie ein benachbartes Ziel aus (Truppentransport(rechte Maustaste) oder Angriff).";
            default:
                return "";
        }
    }

    public String getCurrentButtonText() {
        switch (state.getGamePhase()) {
            case AttackOrMove:
                return "Runde beenden";
            case QuickOverViewBefore:
                return "OK LETS FIGHT!";
            case QuickOverViewAfter:
                return "Lets reinforce!";
            default:
                return "";
        }
    }
}
