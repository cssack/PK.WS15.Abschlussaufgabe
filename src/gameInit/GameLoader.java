/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package gameInit;

import dataObjects.Territory;
import dataObjects.game.Game;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 07.01.2016.
 * The game loader loads and interprets the files which are needed by a game.
 */
public class GameLoader {

    private static String[] mapFiles = new String[]{"world.map"};
    Game game;
    private Map<String, Territory> territories = new HashMap<>();

    public GameLoader(Game game) {
        this.game = game;
    }

    public void load() throws IOException {
        readImages();
        readMapFiles();
    }

    private void readMapFiles() throws IOException {
        for (String mapFile : mapFiles) {
            MapFileReader mapFileReader = new MapFileReader(game.getData(), getFile_FromResource(mapFile));
            mapFileReader.start_Interpret();
        }
    }

    private void readImages() throws IOException {
        game.getDesign().setBackgroundImage(ImageIO.read(new File(getFile_FromResource("waterTexture.jpg"))));
        game.getDesign().setCapitalImage(ImageIO.read(new File(getFile_FromResource("CapitalIcon.png"))));
    }

    private String getFile_FromResource(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        //TODO find a method which finds a file in the out directory which is not depricated.
        return URLDecoder.decode((new File(classLoader.getResource("resources/" + path)
                .getFile()).getAbsolutePath()));
    }
}
