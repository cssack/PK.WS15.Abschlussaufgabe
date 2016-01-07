/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package engine;

import bases.GameBase;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import dataObjects.game.Game;

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
            pcPlayerEngine.ChooseSomeTerritory();
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
