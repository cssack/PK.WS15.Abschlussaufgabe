/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import dataObjects.enums.PlayerStates;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The game engine handles events and calls state methods accordingly on the GameState class.
 */
public class GameEngine extends GameBase implements MouseMotionListener, MouseListener {

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
        if (mouseOverTerritory == data.getHumanPlayer().getSelectedTerritory()) {
            state.setSelectedTerritory(data.getHumanPlayer(), null);
        } else if (state.getGamePhase() == Phases.Landerwerb) {
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
            if (data.getHumanPlayer().getState() == PlayerStates.FirstTerritorySelection) {
                state.setSelectedTerritory(data.getHumanPlayer(), mouseOverTerritory);
            } else if (data.getHumanPlayer().getState() == PlayerStates.FirstTerritorySelected) {
                if (e.getButton() == MouseEvent.BUTTON1 && mouseOverTerritory.getOccupant() == data.getHumanPlayer()) {
                    if (mouseOverTerritory.getArmyCount() > 1)
                        state.setSelectedTerritory(data.getHumanPlayer(), mouseOverTerritory);
                } else if (e.getButton() == MouseEvent.BUTTON1 && mouseOverTerritory.getOccupant() == data
                        .getCompPlayer()) {
                    state.assignAttackMovement(data.getHumanPlayer(), mouseOverTerritory);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    state.assignTransferMovement(data.getHumanPlayer(), mouseOverTerritory);
                }
                //TODO start attacking or start movement.
            } else if (data.getHumanPlayer().getState() == PlayerStates.RoundFinished) {
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


    private Territory GetTerritoryAtPos(Point p) {
        for (Territory territory : data.getAllTerritories()) {
            if (territory.contains(p))
                return territory;
        }
        return null;
    }
}
