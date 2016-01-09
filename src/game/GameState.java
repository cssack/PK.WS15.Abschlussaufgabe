/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Continent;
import dataObjects.Player;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerPhases;

/**
 * Created by chris on 07.01.2016. The game state is the main vector to populate
 */
public class GameState extends GameBase {
    private Territory mouseOverTerritory;
    private boolean mouseTargetClickable;


    private int occupiedTerritories;
    private boolean repaintRequired;
    private Phases gamePhase = Phases.NotStarted;


    /**
     * @return the current active game phase. Take a look at Phases to gather more information's.
     */
    public Phases getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(Phases phase) {
        gamePhase = phase;
        if (phase == Phases.Landerwerb) {
            setPlayerPhase(data.getHumanPlayer(), PlayerPhases.Reinforcing);
        } else if (phase == Phases.Reinforcement) {
            reload_Reinforcements(data.getHumanPlayer());
            reload_Reinforcements(data.getCompPlayer());

            setPlayerPhase(data.getHumanPlayer(), PlayerPhases.Reinforcing);
            setPlayerPhase(data.getCompPlayer(), PlayerPhases.Waiting);
        } else if (phase == Phases.AttackOrMove) {
            reload_Reinforcements(data.getHumanPlayer());
            reload_Reinforcements(data.getCompPlayer());

            setPlayerPhase(data.getHumanPlayer(), PlayerPhases.FirstTerritorySelection);
            setPlayerPhase(data.getCompPlayer(), PlayerPhases.Waiting);
        }

        reload_MouseTargetClickable();
        repaintRequired = true;
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

    public void setPlayerPhase(Player player, PlayerPhases playersPhase) {
        player.setPhase(playersPhase);
        if (playersPhase == PlayerPhases.FirstTerritorySelection)
            player.setSelectedTerritory(null);
        reload_MouseTargetClickable();
    }

    public void setSelectedTerritory(Player player, Territory territory) {
        player.setSelectedTerritory(territory);
        if (territory == null) {
            setPlayerPhase(player, PlayerPhases.FirstTerritorySelection);
        } else {
            setPlayerPhase(player, PlayerPhases.FirstTerritorySelected);
        }
        repaintRequired = true;
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

        if (data.getHumanPlayer().getPhase() == PlayerPhases.Waiting) {
            newVal = false;
        } else {
            if (gamePhase == Phases.Landerwerb) {
                newVal = mouseOverTerritory != null && mouseOverTerritory.getOccupant() == null;
            } else if (data.getHumanPlayer().getPhase() == PlayerPhases.Reinforcing) {
                newVal = mouseOverTerritory != null && mouseOverTerritory.getOccupant() == data.getHumanPlayer();
            } else if (data.getHumanPlayer().getPhase() == PlayerPhases.FirstTerritorySelection) {
                newVal = mouseOverTerritory != null && mouseOverTerritory.getOccupant() == data
                        .getHumanPlayer() && mouseOverTerritory.getArmyCount() > 1;
            } else if (data.getHumanPlayer().getPhase() == PlayerPhases.FirstTerritorySelected) {
                newVal = mouseOverTerritory != null && (data.getHumanPlayer().getSelectedTerritory().getNeighbors()
                        .contains(mouseOverTerritory) || (mouseOverTerritory.getOccupant() == data
                        .getHumanPlayer() && mouseOverTerritory.getArmyCount() > 1));
            } else if (data.getHumanPlayer().getPhase() == PlayerPhases.AttackedWin) {
                //TODO
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


}
