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
import java.net.URLDecoder;

/**
 * Created by chris on 07.01.2016.
 * The game loader loads and interprets the files which are needed by a game.
 */
public class GameLoader extends GameBase {

    private final static String[] mapFiles = new String[]{"world.map"};

    public GameLoader() {
    }

    public void load() throws IOException, MapFileFormatException {
        readImages();
        readMapFiles();
    }

    private void readMapFiles() throws IOException, MapFileFormatException {
        for (String mapFile : mapFiles) {
            MapFileReader mapFileReader = new MapFileReader(data, getFilePath_FromResource(mapFile));
            mapFileReader.start_Interpret();
        }
    }

    private void readImages() throws IOException {
        game.getDesign().setBackgroundImage(ImageIO.read(new File(getFilePath_FromResource("waterTexture.jpg"))));
        game.getDesign().setCapitalImage(ImageIO.read(new File(getFilePath_FromResource("CapitalIcon.png"))));
    }

    private String getFilePath_FromResource(String path) throws UnsupportedEncodingException {
        return URLDecoder.decode(ClassLoader.getSystemResource("resources/" + path).getPath(), "UTF-8");
    }
}
