/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import drawing.GameDrawingBoard;
import exceptions.MapFileFormatException;
import gameInit.GameLoader;

import java.io.IOException;

/**
 * Created by chris on 07.01.2016.
 * The game collapses all machines and data objects required by one game.
 */
public class Game {
    private final GameData data;
    private final GameDesign design;
    private final GameState state;
    private final GameEngine engine;
    private final GameKi pcPlayer;
    private final GameDrawingBoard drawingBoard;
    private final GameMessages messages;

    public Game() {
        data = new GameData();
        design = new GameDesign();
        state = new GameState();
        drawingBoard = new GameDrawingBoard();
        engine = new GameEngine();
        pcPlayer = new GameKi();
        messages = new GameMessages();

        data.init(this);
        design.init(this);
        state.init(this);
        engine.init(this);
        pcPlayer.init(this);
        drawingBoard.init(this);
        messages.init(this);
    }

    /**
     * @return Gets the gama data scope. This is where all Territories and continents are stored.
     */
    public GameData getData() {
        return data;
    }

    /**
     * @return Gets the design scope. This is the logical part for the designing progress where colors are selected.
     */
    public GameDesign getDesign() {
        return design;
    }

    /**
     * @return Gets the state scope. This is where the current state of any object inside this game is changed.
     */
    public GameState getState() {
        return state;
    }

    /**
     * @return Gets the engine scope. This is where the magic happens and the game comes to live.
     */
    public GameEngine getEngine() {
        return engine;
    }

    /**
     * @return Gets the pc player engine. This is where the pc players logic is described. This is the perfect place for an KI
     */
    public GameKi getPcPlayer() {
        return pcPlayer;
    }

    /**
     * @return Gets the drawing board. This is the component where everything is drawn at.
     */
    public GameDrawingBoard getDrawingBoard() {
        return drawingBoard;
    }


    /**
     * @return Gets the messaging scope. This is used for the output messages presented to the user in the toolbar.
     */
    public GameMessages getMessages() {
        return messages;
    }



    /**
     * Loads all required data from resource files (Images, map files, ....).
     *
     * @throws IOException
     */
    public void load() throws IOException, MapFileFormatException {
        GameLoader loader = new GameLoader();
        loader.init(this);
        loader.load();
    }

}
