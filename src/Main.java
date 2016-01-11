/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

import drawing.DrawingWindow;
import exceptions.InvalidResourceException;
import exceptions.MapFileFormatException;
import game.Game;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws IOException, MapFileFormatException, URISyntaxException, InvalidResourceException {
        Game game = new Game();
        game.load();
        new DrawingWindow(game);
    }
}
