/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerPhases;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

/**
 * Created by chris on 07.01.2016. The game engine handles events and changes the game state accordingly.
 */
public class GameEngine extends GameBase implements MouseMotionListener, MouseListener {
    Random rand = new Random();

    @Override
    public void init(Game game) {
        super.init(game);

        drawingBoard.addMouseMotionListener(this);
        drawingBoard.addMouseListener(this);
        state.setGamePhase(Phases.Landerwerb);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        state.resetRepaintRequired();

        state.setMouseOverTerritory(GetTerritoryAtPos(e.getPoint()));

        if (state.isRepaintRequired()) drawingBoard.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!state.isMouseTargetClickable())
            return;

        state.resetRepaintRequired();

        Territory mouseOverTerritory = state.getMouseOverTerritory();

        if (state.getGamePhase() == Phases.Landerwerb) {
            state.setTerritoryOccupant(mouseOverTerritory, data.getHumanPlayer());
            ki.ChooseSomeTerritory();

            if (state.getOccupiedTerritories() == data.getAllTerritories().size())
                state.setGamePhase(Phases.Reinforcement);

        } else if (state.getGamePhase() == Phases.Reinforcement) {
            state.reinforceTerritory(mouseOverTerritory);
            if (data.getHumanPlayer().getReinforcements() == 0) {
                ki.ReinforceTerritorys();
                state.setGamePhase(Phases.AttackOrMove);
            }
        } else if (state.getGamePhase() == Phases.AttackOrMove) {
            if (data.getHumanPlayer().getPhase() == PlayerPhases.FirstTerritorySelection) {
                state.setSelectedTerritory(data.getHumanPlayer(), mouseOverTerritory);
            } else if (data.getHumanPlayer().getPhase() == PlayerPhases.FirstTerritorySelected) {
                if (e.getButton() == MouseEvent.BUTTON1 && mouseOverTerritory.getOccupant() == data.getHumanPlayer()) {
                    if (mouseOverTerritory.getArmyCount() > 1)
                        state.setSelectedTerritory(data.getHumanPlayer(), mouseOverTerritory);
                } else if (e.getButton() == MouseEvent.BUTTON1 && mouseOverTerritory.getOccupant() == data
                        .getCompPlayer()) {
                    handleUserAttack(mouseOverTerritory);
                    state.setPlayerPhase(data
                            .getHumanPlayer(), PlayerPhases.FirstTerritorySelection); // just for testing
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    //handling group transfer;
                }
                //TODO start attacking or start movement.
            } else if (data.getHumanPlayer().getPhase() == PlayerPhases.AttackedWin) {
                //TODO Check if player wants to move from source territory to attacked territory
            }
        }

        if (state.isRepaintRequired())
            drawingBoard.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void handleUserAttack(Territory defensingTerritory) {
        Territory attackerTerritory = data.getHumanPlayer().getSelectedTerritory();

        state.setPlayerPhase(data.getHumanPlayer(), PlayerPhases.Attacking);
        int defenseArmy = defensingTerritory.getArmyCount();
        int attackingArmy = attackerTerritory.getArmyCount();

        if (attackingArmy > 3)
            attackingArmy = 3;
        else
            attackingArmy = attackingArmy - 1;

        attackerTerritory.setArmyCount(attackerTerritory.getArmyCount() - attackingArmy);

        while (attackingArmy != 0) {
            int defenseRand = rand.nextInt();
            int attackingRand = rand.nextInt();

            if (attackingRand > defenseRand)
                defenseArmy--;
            else
                attackingArmy--;

            if (defenseArmy == 0)
                break;
        }


        if (attackingArmy == 0) {
            defensingTerritory.setArmyCount(defenseArmy);
            state.setPlayerPhase(data.getHumanPlayer(), PlayerPhases.AttackedLost);
        } else {
            state.setTerritoryOccupant(defensingTerritory, data.getHumanPlayer());
            defensingTerritory.setArmyCount(attackingArmy);
            state.setPlayerPhase(data.getHumanPlayer(), PlayerPhases.AttackedWin);
        }


    }

    private Territory GetTerritoryAtPos(Point p) {
        for (Territory territory : data.getAllTerritories()) {
            if (territory.contains(p))
                return territory;
        }
        return null;
    }
}
