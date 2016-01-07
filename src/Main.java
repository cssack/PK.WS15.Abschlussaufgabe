/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

import dataObjects.game.Game;
import drawing.DrawingWindow;
import exceptions.MapFileFormatException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, MapFileFormatException {
        Game game = new Game();
        game.load();
        DrawingWindow dw = new DrawingWindow(game);
    }
}
