/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package gameInit;

import bases.GameBase;
import exceptions.MapFileFormatException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * The game loader loads and interprets the files which are needed by a game.
 */
public class GameLoader extends GameBase {

    /**
     * later should be loaded from start parameters.
     */
    private final static String[] mapFiles = new String[]{"world.map"};

    public GameLoader() {
    }

    /**
     * init the game with the desired content.
     */
    public void load() throws IOException, MapFileFormatException, URISyntaxException {
        readImages();
        readMapFiles();
    }

    /**
     * loads the map files into the game.
     */
    private void readMapFiles() throws IOException, MapFileFormatException, URISyntaxException {
        for (String mapFile : mapFiles) {
            MapFileReader mapFileReader = new MapFileReader(data, getFilePath_FromResource(mapFile));
            mapFileReader.start_Interpret();
        }
    }

    /**
     * loads the images into the game.
     */
    private void readImages() throws IOException, URISyntaxException {
        game.getDesign().setBackgroundImage(ImageIO.read(new File(getFilePath_FromResource("waterTexture.jpg"))));
        game.getDesign().setCapitalImage(ImageIO.read(new File(getFilePath_FromResource("CapitalIcon.png"))));
    }

    /**
     * Converts a filename to the current valid full file path for the file.
     *
     * @return relative file paths to the application folder.
     */
    private String getFilePath_FromResource(String path) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource("resources/" + path).toURI()).toString();
    }
}
