/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package gameInit;

import bases.GameBase;
import exceptions.MapFileFormatException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Paths;

/**
 * Created by chris on 07.01.2016.
 * The game loader loads and interprets the files which are needed by a game.
 */
public class GameLoader extends GameBase {

    private final static String[] mapFiles = new String[]{"world.map"};

    public GameLoader() {
    }

    public void load() throws IOException, MapFileFormatException, URISyntaxException {
        readImages();
        readMapFiles();
    }

    private void readMapFiles() throws IOException, MapFileFormatException, URISyntaxException {
        for (String mapFile : mapFiles) {
            MapFileReader mapFileReader = new MapFileReader(data, getFilePath_FromResource(mapFile));
            mapFileReader.start_Interpret();
        }
    }

    private void readImages() throws IOException, URISyntaxException {
        game.getDesign().setBackgroundImage(ImageIO.read(new File(getFilePath_FromResource("waterTexture.jpg"))));
        game.getDesign().setCapitalImage(ImageIO.read(new File(getFilePath_FromResource("CapitalIcon.png"))));
    }

    private String getFilePath_FromResource(String path) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource("resources/" + path).toURI()).toString();
    }
}
