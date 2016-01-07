/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package gameInit;

import bases.GameBase;
import exceptions.MapFileFormatException;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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
            MapFileReader mapFileReader = new MapFileReader(data, getFile_FromResource(mapFile));
            mapFileReader.start_Interpret();
        }
    }

    private void readImages() throws IOException {
        game.getDesign().setBackgroundImage(ImageIO.read(new File(getFile_FromResource("waterTexture.jpg"))));
        game.getDesign().setCapitalImage(ImageIO.read(new File(getFile_FromResource("CapitalIcon.png"))));
    }

    private String getFile_FromResource(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        //TODO find a method which finds a file in the out directory which is not deprecated.
        return URLDecoder.decode((new File(classLoader.getResource("resources/" + path)
                .getFile()).getAbsolutePath()));
    }
}
