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

/**
 * Created by chris on 07.01.2016.
 * The game engine handles events and changes the game state accordingly.
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
        if (e.getButton() == MouseEvent.BUTTON3) {
            if (data.getHumanPlayer().getPhase() == PlayerPhases.FirstTerritorySelected) {
                state.setSelectedTerritory(data.getHumanPlayer(), null);
            }

            if (state.isRepaintRequired())
                drawingBoard.repaint();
            return;
        }

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
                //TODO start attacking or start movement.
            } else if (data.getHumanPlayer().getPhase() == PlayerPhases.Attacked) {
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
