/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Continent;
import dataObjects.Player;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerActions;

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
    private boolean waitingForUserInput = true; //TODO implemented for future use like the bot takes his time to think or something else.


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
    public void setTerritoryOccupant(Territory territory, Player newOccupant) {
        Player previousOccupant = territory.getOccupant();
        assert previousOccupant != newOccupant;

        if (previousOccupant == null) {
            occupiedTerritories++;
            territory.setArmyCount(1);
        } else {
            previousOccupant.setTerritoryOwnership(territory, false);
        }

        newOccupant.setTerritoryOwnership(territory, true);
        territory.setOccupant(newOccupant);

        reload_ContinentOwners();
        reload_ReinforcementGains(newOccupant);
        reload_GamePhase();
        reload_MouseTargetClickable();
    }

    /**
     * Sets the territory's occupant state
     */
    public void reinforceTerritory(Territory territory) {
        Player occupant = territory.getOccupant();
        territory.setArmyCount(territory.getArmyCount() + 1);
        occupant.setReinforcements(occupant.getReinforcements() - 1);


        repaintRequired = true;
    }


    private void reload_MouseTargetClickable() {
        boolean newVal = false;

        if (!waitingForUserInput) {
            newVal = false;
        } else {
            if (gamePhase == Phases.Landerwerb) {
                newVal = mouseOverTerritory != null && mouseOverTerritory.getOccupant() == null;
            } else if (data.getHumanPlayer().getAction() == PlayerActions.ArmyReinforcement) {
                newVal = mouseOverTerritory != null && mouseOverTerritory.getOccupant() == data.getHumanPlayer();
            }
        }


        if (newVal == mouseTargetClickable)
            return;

        mouseTargetClickable = newVal;
        repaintRequired = true;
    }

    private void reload_ContinentOwners() {
        for (Continent continent : data.getAllContinents()) {
            Player owner = continent.getTerritories().get(0).getOccupant();
            if (owner == null) { // this continent belongs to nobody
                data.getCompPlayer().setContinentOwnerShip(continent, false);
                data.getHumanPlayer().setContinentOwnerShip(continent, false);
                continue;
            }
            boolean consistentOwnerShip = true;
            for (int i = 1; i < continent.getTerritories().size(); i++) {
                Territory territory = continent.getTerritories().get(i);
                if (owner != territory
                        .getOccupant()) { // if there are two different occupants the continent belongs to nobody
                    data.getHumanPlayer().setContinentOwnerShip(continent, false);
                    data.getCompPlayer().setContinentOwnerShip(continent, false);
                    consistentOwnerShip = false;
                    break;
                }
            }
            if (!consistentOwnerShip)
                continue;

            owner.setContinentOwnerShip(continent, true);
        }
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
        int gain = player.getOwnedTerritories().size() / 3;
        for (Continent continent : player.getOwnedContinents()) {
            gain += continent.getReinforcementBonus();
        }
        player.setReinforcementGain(gain);
    }

    private void reload_Reinforcements(Player player) {
        player.setReinforcements(player.getReinforcementGain());
    }

    public boolean getWaitingForUserInput() {
        return waitingForUserInput;
    }

}
