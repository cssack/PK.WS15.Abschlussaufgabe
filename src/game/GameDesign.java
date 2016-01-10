/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import bases.TacticalMovement;
import dataObjects.Territory;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The game design implements the logic for the colors, images,... for the drawing routines. This is the place where
 * everything should be stored which have something to do with the design of the game.
 */
public class GameDesign extends GameBase {
    private BufferedImage backgroundImage;
    private BufferedImage capitalImage;
    private BasicStroke boundaryStroke = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);


    /**
     * @return the color for the sea connections between two territory's.
     */
    public Color getCapitalLineColor() {
        return Color.WHITE;
    }

    /**
     * @return the stroke for the sea connections between two territory's.
     */
    public Stroke getCapitalLineStroke() {
        return new BasicStroke(2f);
    }

    /**
     * @return the current valid background color for a territory.
     */
    public Color getTerritoryBackgroundColor(Territory t) {
        boolean highlighted = (engine.getIsMouseLeftButtonValid() || engine.getIsMouseRightButtonValid()) && engine
                .getHoverTerritory() == t;
        if (t == data.getHumanPlayer().getSelectedTerritory())
            if (t.getOccupant() == data.getHumanPlayer())
                return Color.decode("#107C0F");
            else
                return Color.decode("#E81123");
        if (t.getOccupant() == null)
            if (highlighted)
                return Color.YELLOW;
            else
                return Color.WHITE;
        else if (t.getOccupant() == data.getHumanPlayer())
            if (highlighted)
                return Color.decode("#008A00");
            else
                return Color.decode("#60A917");
        else if (t.getOccupant() == data.getCompPlayer())
            if (highlighted)
                return Color.decode("#BF1400");
            else
                return Color.decode("#E51400");


        return Color.BLACK;
    }

    /**
     * @return the current valid boundary color for a territory.
     */
    public Color getTerritoryBoundaryColor(Territory t) {
        if (t == data.getHumanPlayer().getSelectedTerritory())
            return Color.BLACK;
        return Color.decode("#1C1C1C");
    }

    private boolean isTerritoryAttacked(Territory t) {
        TacticalMovement compMove = data.getCompPlayer().getAttackMovement();
        TacticalMovement humanMove = data.getHumanPlayer().getAttackMovement();
        return (compMove != null && compMove.to == t) || (humanMove != null && humanMove.to == t);
    }

    /**
     * @return the current valid stroke for a territory.
     */
    public Stroke getTerritoryBoundaryStroke(Territory t) {
        return boundaryStroke;
    }

    /**
     * @return the endless and mystic see image.
     */
    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * @return the image placed at the capital position. DEPRECATED
     */
    public BufferedImage getCapitalImage() {
        return capitalImage;
    }

    public void setCapitalImage(BufferedImage capitalImage) {
        this.capitalImage = capitalImage;
    }
}
