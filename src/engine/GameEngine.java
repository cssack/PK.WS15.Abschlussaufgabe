/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package engine;

import dataObjects.Territory;
import dataObjects.enums.Occupants;
import dataObjects.enums.Phases;
import dataObjects.game.Game;
import dataObjects.game.GameData;
import dataObjects.game.GameState;
import drawing.GameDrawingBoard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by chris on 07.01.2016.
 * The game engine handles events and changes the game state accordingly.
 */
public class GameEngine implements MouseMotionListener, MouseListener {
    private Game game;
    private GameState state;
    private GameData data;
    private GameDrawingBoard canvas;
    private PcPlayerEngine pcPlayer;

    public GameEngine(Game owner) {
        game = owner;
        state = game.getState();
        data = game.getData();
        pcPlayer = game.getPcPlayer();
        canvas = game.getDrawingBoard();
        canvas.addMouseMotionListener(this);
        canvas.addMouseListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        state.resetRepaintRequired();

        state.setMouseOverTerritory(GetTerritoryAtPos(e.getPoint()));

        if (state.isRepaintRequired()) canvas.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!state.isMouseTargetClickable())
            return;

        state.resetRepaintRequired();

        Territory mouseOverTerritory = state.getMouseOverTerritory();

        if (state.getGamePhase() == Phases.Landerwerb) {
            state.setTerritoryOccupant(mouseOverTerritory, Occupants.Human);
            pcPlayer.ChooseSomeTerritory();
        }

        if (state.isRepaintRequired())
            canvas.repaint();
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
