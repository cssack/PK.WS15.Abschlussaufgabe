/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package drawing;

import dataObjects.Patch;
import dataObjects.Territory;
import dataObjects.game.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Created by chris on 07.01.2016.
 * The drawing board draws the content of a game by using the game data the state and the design.
 */
public class GameDrawingBoard extends JComponent {
    private final int toolBarHeight = 50;
    private final Dimension worldMapSize = new Dimension(1250, 650);
    private final Font toolBarFont = new Font("Verdana", Font.PLAIN, 20);
    private int paintCount;
    private GameData data;
    private GameDesign design;
    private GameState state;
    private GameMessages messages;

    public void init(Game game) {
        this.data = game.getData();
        this.design = game.getDesign();
        this.state = game.getState();
        this.messages = game.getMessages();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(worldMapSize.width, worldMapSize.height + toolBarHeight);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        DrawBackground(g2);
        DrawCapitalLines(g2);
        DrawTerritories(g2);
        DrawInfoBar(g2);

    }

    private void DrawBackground(Graphics2D g) {
        g.drawImage(design.getBackgroundImage(), 0, 0, null);
    }

    private void DrawTerritories(Graphics2D g) {
        Color prevColor = g.getColor();
        Stroke prevStroke = g.getStroke();

        for (int i = data.getAllTerritories().size() - 1; i >= 0; i--) {
            Territory territory = data.getAllTerritories().get(i);

            g.setColor(design.getTerritoryBackgroundColor(territory));
            for (Patch patch : territory.getPatches()) {
                g.fillPolygon(patch.getPolygon());
            }

            g.setColor(design.getTerritoryBoundaryColor(territory));
            g.setStroke(design.getTerritoryBoundaryStroke(territory));
            for (Patch patch : territory.getPatches()) {
                g.drawPolygon(patch.getPolygon());
            }

            DrawCapital(g, territory);
        }

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }

    private void DrawCapital(Graphics2D g, Territory t) {
        int x = t.getCapital().getPoint().x - design.getCapitalImage().getWidth() / 2;
        int y = t.getCapital().getPoint().y - design.getCapitalImage().getHeight() / 2;

        //g.drawImage(design.getCapitalImage(), x, y, null);
        g.drawString(t.getName(), x, y);
    }

    private void DrawCapitalLines(Graphics2D g) {
        Color prevColor = g.getColor();
        Stroke prevStroke = g.getStroke();
        g.setColor(design.getCapitalLineColor());
        g.setStroke(design.getCapitalLineStroke());

        HashSet<Territory> visitedNodes = new HashSet<>();
        Territory currentNode = data.getAllTerritories().get(0);
        DrawCapitalLines(g, visitedNodes, currentNode);

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }

    private void DrawCapitalLines(Graphics2D g, HashSet<Territory> visitedNodes, Territory currentNode) {
        for (Territory neighbor : currentNode.getNeighbors()) {
            if (visitedNodes.contains(neighbor))
                continue;

            Point capitalA = currentNode.getCapital().getPoint();
            Point capitalB = neighbor.getCapital().getPoint();
            g.drawLine(capitalA.x, capitalA.y, capitalB.x, capitalB.y);

            visitedNodes.add(neighbor);
            DrawCapitalLines(g, visitedNodes, neighbor);
        }
    }

    private void DrawInfoBar(Graphics2D g) {
        // Currently in work by chris
        // A Toolbar on the bottom of the game which gives an overview of the current game and state

        Font prevFont = g.getFont();
        Color prevColor = g.getColor();

        g.fillRect(0, worldMapSize.height, worldMapSize.width, toolBarHeight);


        g.setFont(toolBarFont);
        g.setColor(Color.WHITE);
        int vertMiddlePos = g.getFontMetrics().getAscent() / 2 + toolBarHeight / 2 + worldMapSize.height;

        //noinspection SuspiciousNameCombination
        g.drawString(messages.getCurrentPhase(), 20, vertMiddlePos);


        g.setFont(prevFont);
        g.setColor(prevColor);
    }


}
