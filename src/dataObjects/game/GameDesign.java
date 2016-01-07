package dataObjects.game;

import dataObjects.Territory;
import dataObjects.enums.Occupants;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by chris on 07.01.2016.
 */
public class GameDesign {
    private BufferedImage backgroundImage;
    private BufferedImage capitalImage;

    private Game game;


    public GameDesign(Game gd) {
        this.game = gd;
    }



    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
    public void setCapitalImage(BufferedImage capitalImage) {
        this.capitalImage = capitalImage;
    }




    /**
     * @return the current valid background color for a territory.
     */
    public Color getTerritoryBackgroundColor(Territory t)
    {
        boolean highlighted = game.getState().isMouseTargetClickable() && game.getState().getMouseOverTerritory() == t;
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
    public Color getTerritoryBoundaryColor(Territory t)
    {
        return Color.decode("#4C4A48");
    }

    /**
     * @return the current valid stroke for a territory.
     */
    public Stroke getTerritoryBoundaryStroke(Territory t)
    {
        return new BasicStroke(3.0f);
    }

    /**
     * @return the endless and mystic see
     */
    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * @return the image placed at the capital position.
     */
    public BufferedImage getCapitalImage() {
        return capitalImage;
    }
}
