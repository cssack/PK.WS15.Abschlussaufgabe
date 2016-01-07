/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Continent;
import dataObjects.Player;
import dataObjects.Territory;
import dataObjects.enums.Phases;

/**
 * Created by chris on 07.01.2016.
 * The game state is the main vector to populate
 */
public class GameState extends GameBase {
    private Territory mouseOverTerritory;
    private boolean mouseTargetClickable;


    private int occupiedTerritories;
    private boolean repaintRequired;
    private Phases gamePhase = Phases.Landerwerb;


    /**
     * @return the current active game phase. Take a look at Phases to gather more information's.
     */
    public Phases getGamePhase() {
        return gamePhase;
    }


    /**
     * @return the currently occupied territories. This field make only sense in the 'Landerwerb' phase
     */
    public int getOccupiedTerritories() {
        return occupiedTerritories;
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
        occupiedTerritories++;
        occupant.setTerritoriesCount(occupant.getTerritoriesCount() + 1);
        territory.setOccupant(occupant);

        reload_ReinforcementGains(occupant);
        reload_GamePhase();
        reload_MouseTargetClickable();
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

    private void reload_GamePhase() {
        if (occupiedTerritories == data.getAllTerritories().size()) {
            gamePhase = Phases.Eroberungen;
            reload_Reinforcements(data.getCompPlayer());
            reload_Reinforcements(data.getHumanPlayer());
        } else
            gamePhase = Phases.Landerwerb;
    }

    private void reload_ReinforcementGains(Player player) {
        int gain = player.getTerritoriesCount() / 3;
        for (Continent continent : player.getOwnedContinents()) {
            gain += continent.getReinforcementBonus();
        }
        player.setReinforcementGain(gain);
    }

    private void reload_Reinforcements(Player player) {
        player.setReinforcementsAvailable(player.getReinforcementGain());
    }
}
