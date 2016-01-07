/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.game;

import bases.GameBase;
import dataObjects.Player;
import dataObjects.Territory;
import dataObjects.enums.EroberungsPhases;
import dataObjects.enums.Phases;

/**
 * Created by chris on 07.01.2016.
 * The game state is the main vector to populate
 */
public class GameState extends GameBase {
    private Territory mouseOverTerritory;
    private boolean mouseTargetClickable;
    private int occupantedTerritories;
    private boolean repaintRequired;
    private Phases gamePhase = Phases.Landerwerb;
    private EroberungsPhases eroberungsPhase = EroberungsPhases.VerstaerkungVerteilen;


    /**
     * @return the current active game phase. Take a look at Phases to gather more information's.
     */
    public Phases getGamePhase() {
        return gamePhase;
    }

    /**
     * @return the current active eroberungs phase. Take a look at EroberungsPhases to gather more information's.
     */
    public EroberungsPhases getEroberungsPhase() {
        return eroberungsPhase;
    }

    /**
     * @return the territory where currently the mouse is over.
     */
    public Territory getMouseOverTerritory() {
        return mouseOverTerritory;
    }

    /**
     * Sets the current territory where the mouse is over.
     */
    public void setMouseOverTerritory(Territory territory) {
        if (this.mouseOverTerritory == territory)
            return;
        this.mouseOverTerritory = territory;
        reload_MouseTargetClickable();
        repaintRequired = true;
    }

    /**
     * @return true if the under laying object of the mouse is click able.
     */
    public boolean isMouseTargetClickable() {
        return mouseTargetClickable;
    }

    /**
     * Sets the repaint required field to false.
     */
    public void resetRepaintRequired() {
        this.repaintRequired = false;
    }

    /**
     * @return true if some operation between resetRepaintRequired and isRepaintRequired needs an repaint of the form
     */
    public boolean isRepaintRequired() {
        return repaintRequired;
    }

    /**
     * Sets the territory's occupant state
     */
    public void setTerritoryOccupant(Territory territory, Player occupant) {
        territory.setOccupant(occupant);
        occupantedTerritories++;
        reload_GamePhase();
        reload_MouseTargetClickable();
    }

    private void reload_GamePhase() {
        if (occupantedTerritories == data.getAllTerritories().size())
            gamePhase = Phases.Eroberungen;
        else
            gamePhase = Phases.Landerwerb;
    }

    private void reload_MouseTargetClickable() {
        boolean newVal;
        newVal = gamePhase == Phases.Landerwerb && mouseOverTerritory != null
                && mouseOverTerritory.getOccupant() == null;

        if (newVal == mouseTargetClickable)
            return;

        mouseTargetClickable = newVal;
        repaintRequired = true;
    }
}
