package dataObjects.game;

import drawing.GameDrawingBoard;
import engine.GameEngine;
import engine.PcPlayerEngine;
import gameInit.GameLoader;

import java.io.IOException;

/**
 * Created by chris on 07.01.2016.
 */
public class Game {
    private PcPlayerEngine pcPlayer = new PcPlayerEngine(this);
    private GameData data = new GameData(this);
    private GameDesign design = new GameDesign(this);
    private GameState state = new GameState(this);
    private GameDrawingBoard drawingBoard = new GameDrawingBoard(this);
    private GameEngine engine = new GameEngine(this);

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
    public PcPlayerEngine getPcPlayer() {
        return pcPlayer;
    }

    /**
     * @return Gets the drawing board. This is the component where evrything is drawn at.
     */
    public GameDrawingBoard getDrawingBoard() {
        return drawingBoard;
    }

    /**
     * Loads all required data from resource files (Images, map files, ....).
     *
     * @throws IOException
     */
    public void load() throws IOException {
        GameLoader loader = new GameLoader(this);
        loader.load();
    }
}
