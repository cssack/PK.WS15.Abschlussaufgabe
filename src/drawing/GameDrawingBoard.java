/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package drawing;

import bases.TacticalMovement;
import dataObjects.Patch;
import dataObjects.Territory;
import game.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Created by chris on 07.01.2016. The drawing board draws the content of a game by using the game data the state and
 * the design.
 */
public class GameDrawingBoard extends JComponent {
    private final int toolBarHeight = 50;
    private final Dimension worldMapSize = new Dimension(1250, 650);
    private final Font toolBarFont = new Font("Verdana", Font.PLAIN, 20);
    private final Font armyFont = new Font("Verdana", Font.BOLD, 16);
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

        paintCount++;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        DrawBackground(g2);
        DrawNeighborLines(g2);
        DrawTerritories(g2);
        DrawTransportMovements(g2);
        DrawAttackMovements(g2);
        DrawTerritoryTexts(g2);
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

            if (territory == data.getHumanPlayer().getSelectedTerritory())
                continue;//skip if available so it can be drawn at last (correct border drawing)

            DrawTerritory(g, territory);
        }
        if (data.getHumanPlayer().getSelectedTerritory() != null) {
            DrawTerritory(g, data.getHumanPlayer().getSelectedTerritory());
        }

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }

    private void DrawTerritoryTexts(Graphics2D g) {
        Font prevFont = g.getFont();
        Color prevColor = g.getColor();

        g.setColor(Color.white);
        g.setFont(armyFont);

        for (Territory territory : data.getAllTerritories()) {
            DrawTerritoryText(g, territory);
        }

        g.setFont(prevFont);
        g.setColor(prevColor);
    }

    private void DrawTerritory(Graphics2D g, Territory territory) {
        g.setColor(design.getTerritoryBackgroundColor(territory));
        for (Patch patch : territory.getPatches()) {
            g.fillPolygon(patch.getPolygon());
        }

        g.setColor(design.getTerritoryBoundaryColor(territory));
        g.setStroke(design.getTerritoryBoundaryStroke(territory));
        for (Patch patch : territory.getPatches()) {
            g.drawPolygon(patch.getPolygon());
        }

    }

    private void DrawTerritoryText(Graphics2D g, Territory t) {
        if (t.getOccupant() == null)
            return;

        String capitalText = String.valueOf(t.getArmyCount());

        int stringWidth = SwingUtilities.computeStringWidth(g.getFontMetrics(), capitalText);
        int stringHeight = (int) armyFont.getLineMetrics(capitalText, g.getFontRenderContext()).getAscent();

        int x = t.getCapital().getPoint().x - (stringWidth / 2);
        int y = t.getCapital().getPoint().y + (stringHeight / 2);

        g.drawString(capitalText, x, y);

    }

    private void DrawNeighborLines(Graphics2D g) {
        Color prevColor = g.getColor();
        Stroke prevStroke = g.getStroke();
        g.setColor(design.getCapitalLineColor());
        g.setStroke(design.getCapitalLineStroke());

        HashSet<Territory> visitedNodes = new HashSet<>();
        Territory currentNode = data.getAllTerritories().get(0);
        DrawNeighborLines(g, visitedNodes, currentNode);

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }

    private void DrawNeighborLines(Graphics2D g, HashSet<Territory> visitedNodes, Territory currentNode) {
        visitedNodes.add(currentNode);
        for (Territory neighbor : currentNode.getNeighbors()) {
            if (visitedNodes.contains(neighbor))
                continue;
            Point capitalA = currentNode.getCapital().getPoint();
            Point capitalB = neighbor.getCapital().getPoint();

            if (currentNode.getCapital().getOwner().getName().equals("Alaska") && neighbor.getCapital().getOwner()
                    .getName().equals("Kamchatka")) {
                g.drawLine(capitalA.x, capitalA.y, 0, capitalB.y);
                g.drawLine(capitalB.x, capitalB.y, worldMapSize.width, capitalA.y);
            } else {
                g.drawLine(capitalA.x, capitalA.y, capitalB.x, capitalB.y);
            }
        }
        for (Territory neighbor : currentNode.getNeighbors()) {
            if (visitedNodes.contains(neighbor))
                continue;
            DrawNeighborLines(g, visitedNodes, neighbor);
        }
    }

    private void DrawTransportMovements(Graphics2D g) {
        Color prevColor = g.getColor();
        Stroke prevStroke = g.getStroke();

        g.setColor(Color.BLUE);
        g.setStroke(design.getCapitalLineStroke());
        DrawMovement(g, data.getHumanPlayer().getTransportMovement());
        DrawMovement(g, data.getCompPlayer().getTransportMovement());

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }

    private void DrawAttackMovements(Graphics2D g) {
        Color prevColor = g.getColor();
        Stroke prevStroke = g.getStroke();

        g.setColor(Color.BLACK);
        g.setStroke(design.getCapitalLineStroke());
        DrawMovement(g, data.getHumanPlayer().getAttackMovement());
        DrawMovement(g, data.getCompPlayer().getAttackMovement());

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }


    private void DrawMovement(Graphics2D g, TacticalMovement movement) {
        if (movement == null)
            return;


        Point capitalB = movement.to.getCapital().getPoint();

        DrawLineBetweenCapitals(g, movement.from, movement.to);
        g.drawOval(capitalB.x - 10, capitalB.y - 10, 20, 20);

        Font prevFont = g.getFont();

        g.setFont(armyFont);
        DrawLineBetweenCapitalsDescription(g, movement.from, movement.to, String.valueOf(movement.getArmyCount()));

        g.setFont(prevFont);
    }

    private void DrawLineBetweenCapitals(Graphics2D g, Territory from, Territory to) {
        Point capitalA = from.getCapital().getPoint();
        Point capitalB = to.getCapital().getPoint();
        String fromName = from.getCapital().getOwner().getName();
        String toName = to.getCapital().getOwner().getName();


        if (fromName.equals("Alaska") && toName.equals("Kamchatka")) {
            g.drawLine(capitalA.x, capitalA.y, 0, capitalB.y);
            g.drawLine(capitalB.x, capitalB.y, worldMapSize.width, capitalA.y);
        } else if (fromName.equals("Kamchatka") && toName.equals("Alaska")) {
            g.drawLine(capitalB.x, capitalB.y, 0, capitalA.y);
            g.drawLine(capitalA.x, capitalA.y, worldMapSize.width, capitalB.y);
        } else {
            g.drawLine(capitalA.x, capitalA.y, capitalB.x, capitalB.y);
        }
    }

    private void DrawLineBetweenCapitalsDescription(Graphics2D g, Territory from, Territory to, String text) {
        Point fromPoint = from.getCapital().getPoint();
        Point toPoint = to.getCapital().getPoint();
        String fromName = from.getCapital().getOwner().getName();
        String toName = to.getCapital().getOwner().getName();

        int x, y;

        if (fromName.equals("Alaska") && toName.equals("Kamchatka")) {
            toPoint = new Point(0, toPoint.y);
        } else if (fromName.equals("Kamchatka") && toName.equals("Alaska")) {
            toPoint = new Point(worldMapSize.width, toPoint.y);
        }

        Point vectorToTheMiddleOfTheLine = new Point((toPoint.x - fromPoint.x) / 2, (toPoint.y - fromPoint.y) / 2);
        x = fromPoint.x + vectorToTheMiddleOfTheLine.x;
        y = fromPoint.y + vectorToTheMiddleOfTheLine.y;
        if (vectorToTheMiddleOfTheLine.x * vectorToTheMiddleOfTheLine.y < 0) {
            //move x coordinate if slope is negative to avoid text rendering over line
            x = x - SwingUtilities.computeStringWidth(g.getFontMetrics(), text);
        }
        g.drawString(text, x, y);
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
