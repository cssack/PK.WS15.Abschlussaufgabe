/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package gameInit;

import dataObjects.Continent;
import dataObjects.Territory;
import exceptions.MapFileFormatException;
import game.GameData;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chris on 06.01.2016.
 * The map file reader interprets a map file into the corresponding data objects.
 */
class MapFileReader {
    private static final Pattern patchNamePattern = Pattern.compile("[a-z-].*? (.*?) [0-9:]");
    private static final Pattern coordinatesPattern = Pattern.compile("([0-9]+) ([0-9]+) ?");
    private static final Pattern neighborsPattern = Pattern.compile(": (.*)");
    private static final Pattern continentPattern = Pattern.compile("continent (.*?) ([0-9]+) : (.*)");

    private final String dataSource;
    private final GameData gameData;

    public MapFileReader(GameData gameData, String file) {
        this.gameData = gameData;
        dataSource = file;
    }

    public void start_Interpret() throws IOException, MapFileFormatException {
        List<String> lines = loadFileLines(dataSource);

        for (String line : lines) {
            if (line.startsWith("patch-of"))
                parseTerritory(line);
            else if (line.startsWith("capital-of"))
                parseCapital(line);
            else if (line.startsWith("neighbors-of"))
                parseNeighbors(line);
            else if (line.startsWith("continent"))
                parseContinent(line);
        }
    }

    private void parseTerritory(String line) throws MapFileFormatException {
        Territory targetTerritory = getOrCreate_Territory_ByName(getTerritoryName_FromLine(line));
        Polygon polygon = new Polygon();

        Matcher polygonMatcher = coordinatesPattern.matcher(line);

        while (polygonMatcher.find()) {
            Point currentPoint = getCurrentPoint(polygonMatcher);
            polygon.addPoint(currentPoint.x, currentPoint.y);
        }

        targetTerritory.addPatch(polygon);
    }

    private void parseCapital(String line) throws MapFileFormatException {
        Territory targetTerritory = getOrCreate_Territory_ByName(getTerritoryName_FromLine(line));
        Matcher matcher = coordinatesPattern.matcher(line);

        if (!matcher.find())
            throw new MapFileFormatException(dataSource, line);

        targetTerritory.setCapital(getCurrentPoint(matcher));
    }

    private void parseNeighbors(String line) throws MapFileFormatException {
        Territory targetTerritory = getOrCreate_Territory_ByName(getTerritoryName_FromLine(line));
        Matcher matcher = neighborsPattern.matcher(line);


        if (!matcher.find())
            throw new MapFileFormatException(dataSource, line);

        String[] neighbors = matcher.group(1)
                .split(" - ");

        for (String neighbor : neighbors) {
            targetTerritory.addNeighbor(getOrCreate_Territory_ByName(neighbor));
        }
    }

    private void parseContinent(String line) throws MapFileFormatException {
        Matcher matcher = continentPattern.matcher(line);

        if (!matcher.find())
            throw new MapFileFormatException(dataSource, line);

        String continentName = matcher.group(1);
        int reinforcementBooster = Integer.parseInt(matcher.group(2));
        String[] territories = matcher.group(3).split(" - ");

        Continent continent = getOrCreate_Continent_ByName(continentName);

        continent.setReinforcementBonus(reinforcementBooster);

        for (String territoryName : territories) {
            Territory territory = getOrCreate_Territory_ByName(territoryName);
            continent.addTerritory(territory);
        }
    }

    private Territory getOrCreate_Territory_ByName(String name) {
        Territory territory = gameData.getTerritory_ByName(name);
        if (territory == null) {
            territory = new Territory(name);
            gameData.addTerritory(territory);
        }
        return territory;
    }

    private Continent getOrCreate_Continent_ByName(String name) {
        Continent continent = gameData.getContinent_ByName(name);
        if (continent == null) {
            continent = new Continent(name);
            gameData.addContinent(continent);
        }
        return continent;
    }

    private String getTerritoryName_FromLine(String line) throws MapFileFormatException {
        Matcher matcher = patchNamePattern.matcher(line);

        if (!matcher.find())
            throw new MapFileFormatException(dataSource, line);

        return matcher.group(1);
    }

    private Point getCurrentPoint(Matcher matcher) {
        Integer x = Integer.parseInt(matcher.group(1));
        Integer y = Integer.parseInt(matcher.group(2));
        return new Point(x, y);
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
