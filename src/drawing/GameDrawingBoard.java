package drawing;

import dataObjects.Patch;
import dataObjects.Territory;
import dataObjects.game.Game;
import dataObjects.game.GameData;
import dataObjects.game.GameDesign;
import dataObjects.game.GameState;
import engine.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

/**
 * Created by chris on 07.01.2016.
 */
public class GameDrawingBoard extends JComponent {
    private int number;
    private Game game;
    private GameEngine engine;
    private GameData data;
    private GameDesign design;
    private GameState state;

    public GameDrawingBoard(Game game) {
        this.game = game;
        this.engine = game.getEngine();
        this.data = game.getData();
        this.design = game.getDesign();
        this.state = game.getState();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        DrawBackground(g2);
        DrawCapitalLines(g2);
        DrawTerritories(g2);
        g2.drawString("" + number++, 10, 10);
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
        Font font = new Font("Tahoma", Font.PLAIN, 14);
        g.setFont(font);
        int textHeight = g.getFontMetrics().getHeight();
        int infobarHeight = 40;
        int top = infobarHeight / 2 - textHeight / 2;

        g.fillRect(0, 650, 1250, infobarHeight);
        g.drawString(state.getGamePhase().name(), top, top + 650);


    }



}
