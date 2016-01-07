package dataObjects.game;

import dataObjects.Territory;
import dataObjects.enums.Occupants;
import dataObjects.enums.Phases;

/**
 * Created by chris on 07.01.2016.
 */
public class GameState {
    private Game game;
    private Territory mouseOverTerritory;
    private boolean mouseTargetClickable;
    private Phases gamePhase = Phases.Landerwerb;
    private int occupantedTerritories;
    private int remainingReinforcements;
    private int remainingReinforcementsPc;
    private boolean repaintRequired;

    public GameState(Game game) {
        this.game = game;
    }

    public Phases getGamePhase() {
        return gamePhase;
    }

    /**
     * @return the territory where currently the mouse is over.
     */
    public Territory getMouseOverTerritory() {
        return mouseOverTerritory;
    }

    /**
     * @return true if the underlaying object of the mouse is click able.
     */
    public boolean isMouseTargetClickable() {
        return mouseTargetClickable;
    }

    public int getRemainingReinforcements() {
        return remainingReinforcements;
    }

    public int getRemainingReinforcementsPc() {
        return remainingReinforcementsPc;
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

    public void setRemainingReinforcements(int remainingReinforcements) {
        this.remainingReinforcements = remainingReinforcements;
    }

    public void setRemainingReinforcementsPc(int remainingReinforcementsPc) {
        this.remainingReinforcementsPc = remainingReinforcementsPc;
    }

    /**
     * Sets the territory's occupant state
     */
    public void setTerritoryOccupant(Territory territory, Occupants occupant) {
        territory.setOccupant(occupant);
        occupantedTerritories++;
        reload_GamePhase();
        reload_MouseTargetClickable();
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

    private void reload_GamePhase() {
        if (occupantedTerritories == game.getData().getAllTerritories().size())
            gamePhase = Phases.Eroberungen;
        else
            gamePhase = Phases.Landerwerb;
    }

    private void reload_MouseTargetClickable() {
        boolean newVal;
        newVal = gamePhase == Phases.Landerwerb && mouseOverTerritory != null
                && mouseOverTerritory.getOccupant() == Occupants.NotDef;

        if (newVal == mouseTargetClickable)
            return;

        mouseTargetClickable = newVal;
        repaintRequired = true;
    }
}
