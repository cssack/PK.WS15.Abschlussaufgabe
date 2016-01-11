/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package gameInit;

import bases.GameBase;
import exceptions.InvalidResourceException;
import exceptions.MapFileFormatException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
    public void load() throws IOException, MapFileFormatException, URISyntaxException, InvalidResourceException {
        readMapFiles();
        readImages();
    }

    /**
     * loads the map files into the game.
     */
    private void readMapFiles() throws IOException, MapFileFormatException, URISyntaxException, InvalidResourceException {
        for (String mapFile : mapFiles) {
            MapFileReader mapFileReader = new MapFileReader(data, getFilePath_FromResource(mapFile));
            mapFileReader.start_Interpret();
        }
    }

    /**
     * loads the images into the game.
     */
    private void readImages() throws IOException, URISyntaxException, InvalidResourceException {
        design.setBackgroundImage(ImageIO.read(new File(getFilePath_FromResource(data.getBackgroundImageString()))));
        design.setCapitalImage(ImageIO.read(new File(getFilePath_FromResource("CapitalIcon.png"))));
    }

    /**
     * Converts a filename to the current valid full file path for the file.
     *
     * @return relative file paths to the application folder.
     */
    private String getFilePath_FromResource(String path) throws URISyntaxException, InvalidResourceException {
        URL resource = ClassLoader.getSystemResource("resources/" + path);

        if (resource == null) {
            throw new InvalidResourceException();
        }

        return Paths.get(resource.toURI()).toString();
    }
}
