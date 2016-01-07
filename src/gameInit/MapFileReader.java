package gameInit;

import dataObjects.Territory;
import dataObjects.game.GameData;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chris on 06.01.2016.
 */
public class MapFileReader {
    private static Pattern patchNamePattern = Pattern.compile("[a-z-].*? (.*?) [0-9:]");
    private static Pattern coordinatesPattern = Pattern.compile("([0-9]+) ([0-9]+) ?");
    private static Pattern neighborsPattern = Pattern.compile(": (.*)");

    private String dataSource;
    private GameData gameData;

    public MapFileReader(GameData gameData, String file) {
        this.gameData = gameData;
        dataSource = file;
    }

    public void start_Interpret() throws IOException {
        List<String> lines = loadFileLines(dataSource);

        for (String line : lines) {
            if (line.startsWith("patch-of"))
                parseTerritory(line);
            else if (line.startsWith("capital-of"))
                parseCapital(line);
            else if (line.startsWith("neighbors-of"))
                parseNeighbors(line);
        }
    }

    private void parseTerritory(String line)
    {
        Territory targetTerritory = getOrCreate_Territory_ByName(getTerritoryName_FromLine(line));
        Polygon polygon = new Polygon();

        Matcher polygonMatcher = coordinatesPattern.matcher(line);

        while (polygonMatcher.find())
        {
            Point currentPoint = getCurrentPoint(polygonMatcher);
            polygon.addPoint(currentPoint.x, currentPoint.y);
        }

        targetTerritory.setPolygon(polygon);

    }
    private void parseCapital(String line)
    {
        Territory targetTerritory = getOrCreate_Territory_ByName(getTerritoryName_FromLine(line));
        Matcher matcher = coordinatesPattern.matcher(line);
        matcher.find(); //TODO capture exception if matcher find is false.

        targetTerritory.setCapital(getCurrentPoint(matcher));
    }
    private void parseNeighbors(String line)
    {
        Territory targetTerritory = getOrCreate_Territory_ByName(getTerritoryName_FromLine(line));
        Matcher matcher = neighborsPattern.matcher(line);
        matcher.find(); //TODO capture exception if matcher find is false.

        String[] neighbors = matcher.group(1).split(" - ");
        ArrayList<Territory> neighborTerritories = new ArrayList<>();
        for (String neighbor : neighbors) {
            neighborTerritories.add(getOrCreate_Territory_ByName(neighbor));
        }

        targetTerritory.setNeighbors(neighborTerritories);
    }


    private Territory getOrCreate_Territory_ByName(String name)
    {
        Territory territory = gameData.getTerritory_ByName(name);
        if (territory == null)
        {
            territory = new Territory(name);
            gameData.addTerritory(territory);
        }
        return territory;
    }

    private String getTerritoryName_FromLine(String line)
    {
        Matcher matcher = patchNamePattern.matcher(line);
        matcher.find();
        return matcher.group(1);
    }

    private Point getCurrentPoint(Matcher matcher)
    {
        Integer x = Integer.parseInt(matcher.group(1));
        Integer y = Integer.parseInt(matcher.group(2));
        return new Point(x,y);
    }


    private List<String> loadFileLines(String file) throws IOException {
        return Files.readAllLines(Paths.get(file));
        /*Scanner sc = new Scanner(file);

        ArrayList<String> lines = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            lines.add(line);
        }

        sc.close();

        return lines;*/
    }
}
