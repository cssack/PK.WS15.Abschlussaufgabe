package gameInit;

import dataObjects.Territory;
import dataObjects.game.Game;
import dataObjects.game.GameData;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 07.01.2016.
 */
public class GameLoader {

    private static String[] mapFiles = new String[]{"africa.map", "three-continents.map", "world.map"};

    private Map<String, Territory> territories = new HashMap<>();
    Game game;

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

        game.getDesign().setBackgroundImage (ImageIO.read(new File(getFile_FromResource("waterTexture.jpg"))));
        game.getDesign().setCapitalImage (ImageIO.read(new File(getFile_FromResource("CapitalIcon.png"))));
    }

    private String getFile_FromResource(String path)
    {
        ClassLoader classLoader = getClass().getClassLoader();
        return URLDecoder.decode( (new File(classLoader.getResource("resources/" + path).getFile()).getAbsolutePath()));
    }



}
