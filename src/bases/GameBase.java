/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package bases;

import dataObjects.game.Game;
import dataObjects.game.GameData;
import dataObjects.game.GameDesign;
import dataObjects.game.GameState;
import drawing.GameDrawingBoard;
import engine.GameEngine;
import engine.PcPlayerEngine;

/**
 * Created by chris on 07.01.2016.
 * This is the base for each machine or data class where the instance is uniquely bound to a game
 */
public class GameBase {

    protected Game game;
    protected GameData data;
    protected GameState state;
    protected GameEngine engine;
    protected GameDesign design;
    protected PcPlayerEngine pcPlayerEngine;
    protected GameDrawingBoard drawingBoard;

    public void init(Game game) {
        this.game = game;
        this.data = game.getData();
        this.state = game.getState();
        this.engine = game.getEngine();
        this.design = game.getDesign();
        this.pcPlayerEngine = game.getPcPlayer();
        this.drawingBoard = game.getDrawingBoard();
    }
}
