/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

import dataObjects.game.Game;
import drawing.DrawingWindow;
import exceptions.MapFileFormatException;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws IOException, MapFileFormatException, URISyntaxException {
        Game game = new Game();
        game.load();
        DrawingWindow dw = new DrawingWindow(game);
    }
}
