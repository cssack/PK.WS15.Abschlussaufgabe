/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.game;

import bases.GameBase;
import dataObjects.Territory;
import dataObjects.enums.Occupants;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by chris on 07.01.2016.
 * The game design implements the logic for the colors, images,... for the drawing routines
 */
public class GameDesign extends GameBase {
    private BufferedImage backgroundImage;
    private BufferedImage capitalImage;


    /**
     * @return the color for the sea connections between two territory's.
     */
    public Color getCapitalLineColor() {
        return Color.WHITE;
    }

    /**
     * @return the size for the sea connections between two territory's.
     */
    public Stroke getCapitalLineStroke() {
        return new BasicStroke(2f);
    }

    /**
     * @return the current valid background color for a territory.
     */
    public Color getTerritoryBackgroundColor(Territory t) {
        boolean highlighted = state.isMouseTargetClickable() && state.getMouseOverTerritory() == t;
        if (t.getOccupant() == Occupants.NotDef)
            if (highlighted)
                return Color.YELLOW;
            else
                return Color.WHITE;
        else if (t.getOccupant() == Occupants.Human)
            if (highlighted)
                return Color.decode("#107C0F");
            else
                return Color.decode("#108840");
        else if (t.getOccupant() == Occupants.Pc)
            if (highlighted)
                return Color.decode("#E81123");
            else
                return Color.decode("#F7630C");


        return Color.BLACK;
    }

    /**
     * @return the current valid boundary color for a territory.
     */
    public Color getTerritoryBoundaryColor(Territory t) {
        return Color.decode("#4C4A48");
    }

    /**
     * @return the current valid stroke for a territory.
     */
    public Stroke getTerritoryBoundaryStroke(Territory t) {
        return new BasicStroke(3.0f);
    }

    /**
     * @return the endless and mystic see
     */
    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    /**
     * @return the image placed at the capital position.
     */
    public BufferedImage getCapitalImage() {
        return capitalImage;
    }

    public void setCapitalImage(BufferedImage capitalImage) {
        this.capitalImage = capitalImage;
    }
}
