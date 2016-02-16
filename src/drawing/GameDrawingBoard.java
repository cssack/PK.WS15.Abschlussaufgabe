/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package drawing;

import bases.TacticalMovement;
import dataObjects.Patch;
import dataObjects.Territory;
import dataObjects.enums.Phases;
import game.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * The drawing board draws the content of a game by using the game data and the design.
 */
public class GameDrawingBoard extends JComponent {
    private int paintCount;
    private GameData data;
    private GameDesign design;
    private GameState state;
    private GameEngine engine;
    private GameMessages messages;

    public GameDrawingBoard() {
        this.setLayout(null);

    }

    public void init(Game game) {
        this.data = game.getData();
        this.design = game.getDesign();
        this.state = game.getState();
        this.messages = game.getMessages();
        this.engine = game.getEngine();
    }

    /**
     * Overridden to allow the windows to resize automatically.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(design.worldMapSize.width, design.worldMapSize.height + design.toolBarHeight);
    }


    /**
     * Occurs whenever the repaint method is invoked. The repaint method should only be invoked by the game engine.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        paintCount++;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        DrawBackground(g2);
        DrawNeighborLines(g2);
        DrawTerritories(g2);
        DrawTransferMovements(g2);
        DrawAttackMovements(g2);
        DrawTerritoryTexts(g2);
        DrawInfoBar(g2);
        DrawRoundFinishButton(g2);
        DrawTacticalMovementDescriptions(g2);

    }

    /**
     * Draws the background of the game.
     */
    private void DrawBackground(Graphics2D g) {
        g.drawImage(design.getBackgroundImage(), 0, 0, null);
    }


    /**
     * Draws the patches of each territory.
     */
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

    /**
     * Draws the patches of a single territory.
     */
    private void DrawTerritory(Graphics2D g, Territory territory) {
        g.setColor(design.getTerritoryBackgroundColor(territory));
        for (Patch patch : territory.getPatches()) {
            g.fillPolygon(patch.getPolygon());
        }

        g.setColor(design.getTerritoryBoundaryColor(territory));
        g.setStroke(design.boundaryStroke);
        for (Patch patch : territory.getPatches()) {
            g.drawPolygon(patch.getPolygon());
        }

    }

    /**
     * Draws the text of each territory to its capital position.
     */
    private void DrawTerritoryTexts(Graphics2D g) {
        Font prevFont = g.getFont();
        Color prevColor = g.getColor();

        g.setColor(Color.white);
        g.setFont(design.armyFont);

        for (Territory territory : data.getAllTerritories()) {
            DrawTerritoryText(g, territory);
        }

        g.setFont(prevFont);
        g.setColor(prevColor);
    }

    /**
     * Draws the text of a single territory to its capital position.
     */
    private void DrawTerritoryText(Graphics2D g, Territory t) {
        if (t.getOccupant() == null)
            return;

        String capitalText = String.valueOf(t.getArmyCount());

        int stringWidth = SwingUtilities.computeStringWidth(g.getFontMetrics(), capitalText);
        int stringHeight = (int) design.armyFont.getLineMetrics(capitalText, g.getFontRenderContext()).getAscent();

        int x = t.getCapital().getPoint().x - (stringWidth / 2);
        int y = t.getCapital().getPoint().y + (stringHeight / 2);

        g.drawString(capitalText, x, y);

    }

    /**
     * Draws white lines between all territories.
     */
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

    /**
     * Draws white lines between all territories recursively. Using a HashSet to determine which nodes already have been
     * drawn.
     */
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
                g.drawLine(capitalB.x, capitalB.y, design.worldMapSize.width, capitalA.y);
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

    /**
     * Draws the transfer movements for each player.
     */
    private void DrawTransferMovements(Graphics2D g) {
        if (state.getGamePhase() != Phases.AttackOrMove && state.getGamePhase() != Phases.QuickOverViewBefore)
            return;
        Color prevColor = g.getColor();
        Stroke prevStroke = g.getStroke();

        g.setColor(Color.BLUE);
        g.setStroke(design.getCapitalLineStroke());
        DrawMovement(g, data.getHumanPlayer().getTransferMovement());
        DrawMovement(g, data.getCompPlayer().getTransferMovement());

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }

    /**
     * Draws the attach movements for each player.
     */
    private void DrawAttackMovements(Graphics2D g) {
        if (state.getGamePhase() != Phases.AttackOrMove && state.getGamePhase() != Phases.QuickOverViewBefore)
            return;
        Color prevColor = g.getColor();
        Stroke prevStroke = g.getStroke();

        g.setColor(Color.BLACK);
        g.setStroke(design.getCapitalLineStroke());

        for (TacticalMovement tacticalMovement : data.getHumanPlayer().getAttackMovements()) {
            DrawMovement(g, tacticalMovement);
        }
        for (TacticalMovement tacticalMovement : data.getCompPlayer().getAttackMovements()) {
            DrawMovement(g, tacticalMovement);
        }

        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }

    /**
     * Draws a single movement this can either be an attack movement or a transfer movement.
     */
    private void DrawMovement(Graphics2D g, TacticalMovement movement) {
        if (movement == null)
            return;


        Point capitalB = movement.to.getCapital().getPoint();

        DrawLineBetweenCapitals(g, movement.from, movement.to);
        g.drawOval(capitalB.x - 10, capitalB.y - 10, 20, 20);

        Font prevFont = g.getFont();

        g.setFont(design.armyFont);
        DrawDescriptionBetweenCapitals(g, movement.from, movement.to, String.valueOf(movement.getArmyCount()));

        g.setFont(prevFont);
    }

    /**
     * Draws a line between two capitals. This method takes care of special line between alaska and kamchatka.
     */
    private void DrawLineBetweenCapitals(Graphics2D g, Territory from, Territory to) {
        Point capitalA = from.getCapital().getPoint();
        Point capitalB = to.getCapital().getPoint();
        String fromName = from.getCapital().getOwner().getName();
        String toName = to.getCapital().getOwner().getName();


        if (fromName.equals("Alaska") && toName.equals("Kamchatka")) {
            g.drawLine(capitalA.x, capitalA.y, 0, capitalB.y);
            g.drawLine(capitalB.x, capitalB.y, design.worldMapSize.width, capitalA.y);
        } else if (fromName.equals("Kamchatka") && toName.equals("Alaska")) {
            g.drawLine(capitalB.x, capitalB.y, 0, capitalA.y);
            g.drawLine(capitalA.x, capitalA.y, design.worldMapSize.width, capitalB.y);
        } else {
            g.drawLine(capitalA.x, capitalA.y, capitalB.x, capitalB.y);
        }
    }

    /**
     * Draws a descriptive text for a line between two capitals. This method takes care of special descriptive text
     * between alaska and kamchatka. Used to display information for tactical movements.
     */
    private void DrawDescriptionBetweenCapitals(Graphics2D g, Territory from, Territory to, String text) {
        Point fromPoint = from.getCapital().getPoint();
        Point toPoint = to.getCapital().getPoint();
        String fromName = from.getCapital().getOwner().getName();
        String toName = to.getCapital().getOwner().getName();

        int x, y;

        if (fromName.equals("Alaska") && toName.equals("Kamchatka")) {
            toPoint = new Point(0, toPoint.y);
        } else if (fromName.equals("Kamchatka") && toName.equals("Alaska")) {
            toPoint = new Point(design.worldMapSize.width, toPoint.y);
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

    /**
     * Draws the info bar for the game at the very bottom.
     */
    private void DrawInfoBar(Graphics2D g) {
        Font prevFont = g.getFont();
        Color prevColor = g.getColor();

        g.fillRect(design.toolbar.x, design.toolbar.y, design.toolbar.width, design.toolbar.height);

        g.setFont(design.toolBarFont);
        g.setColor(Color.WHITE);
        int vertMiddlePos = g.getFontMetrics().getAscent() / 2 + design.toolBarHeight / 2 + design.worldMapSize.height;

        //noinspection SuspiciousNameCombination
        g.drawString(messages.getCurrentToolbarText(), 20, vertMiddlePos);


        g.setFont(prevFont);
        g.setColor(prevColor);
    }

    /**
     * Draws the round finished button.
     */
    private void DrawRoundFinishButton(Graphics2D g) {
        if (state.getGamePhase() != Phases.AttackOrMove && state.getGamePhase() != Phases.QuickOverViewBefore && state
                .getGamePhase() != Phases.Fortifying)
            return;
        Font prevFont = g.getFont();
        Color prevColor = g.getColor();


        g.setColor(engine.getIsMouseOverEndRoundButton() ? Color.BLACK : Color.decode("#717171"));

        g.drawRect(design.endButton.x, design.endButton.y, design.endButton.width, design.endButton.height);
        g.setColor(engine.getIsMouseOverEndRoundButton() ? Color.decode("#C4C4C4") : Color.decode("#E8E8E8"));
        g.fillRect(design.endButton.x, design.endButton.y, design.endButton.width, design.endButton.height);

        g.setFont(design.endbuttonFont);
        g.setColor(Color.BLACK);

        String buttonText = messages.getCurrentButtonText();
        int stringWidth = SwingUtilities.computeStringWidth(g.getFontMetrics(), buttonText);
        int stringHeight = (int) design.endbuttonFont.getLineMetrics(buttonText, g.getFontRenderContext()).getAscent();


        g.drawString(buttonText, design.endButton.x + (design.endButton.width - stringWidth) / 2, design.endButton.y + stringHeight + (design.endButton.height - stringHeight) / 2);


        g.setFont(prevFont);
        g.setColor(prevColor);
    }

    private void DrawTacticalMovementDescriptions(Graphics2D g) {
        if (state.getGamePhase() != Phases.AttackOrMove && state.getGamePhase() != Phases.QuickOverViewBefore && state
                .getGamePhase() != Phases.Fortifying)
            return;
        Font prevFont = g.getFont();
        Color prevColor = g.getColor();

        g.setFont(design.tacticalMovementsFont);
        g.setColor(design.tacticalMovementsColor);

        int offset = 10;
        for (int i = 0; i < messages.getTacticalMovementMessages().size(); i++) {
            String s = messages.getTacticalMovementMessages().get(i);
            int stringHeight = (int) design.endbuttonFont.getLineMetrics(s, g.getFontRenderContext()).getAscent();
            g.drawString(s, 330, (float) (design.worldMapSize.getHeight() - (offset)));
            offset = offset + stringHeight + 10;
        }


        g.setFont(prevFont);
        g.setColor(prevColor);
    }

}
