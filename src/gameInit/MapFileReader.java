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
 * The map file reader interprets a map file into the corresponding data objects.
 */
class MapFileReader {
    // sample line 'patch-of North West Territory 225 39 232 40 235 ...'
    // sample line 'capital-of North America 74 72'
    // sample line 'neighbors-of North America : ...'
    private static final Pattern lineIdentifierPattern = Pattern.compile("[a-z-].*? (.*?) [0-9:]");
    // sample line '225 39 232 40 235 ...'
    private static final Pattern coordinatesPattern = Pattern.compile("([0-9]+) ([0-9]+) ?");
    // sample line 'neighbors-of North America : ...'
    private static final Pattern neighborsPattern = Pattern.compile(": (.*)");
    // sample line 'continent North America 5 : Alaska - '
    private static final Pattern continentPattern = Pattern.compile("continent (.*?) ([0-9]+) : (.*)");
    // sample line 'backgroundImage waterTexture.jpg'
    private static final Pattern backgroundImagePattern = Pattern.compile("backgroundImage (.+)");

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
                parsePatch(line);
            else if (line.startsWith("capital-of"))
                parseCapital(line);
            else if (line.startsWith("neighbors-of"))
                parseNeighbors(line);
            else if (line.startsWith("continent"))
                parseContinent(line);
            else if (line.startsWith("backgroundImage"))
                parseBackgroundImage(line);
        }
    }


    /**
     * Parses an patch to an territory.
     */
    private void parsePatch(String line) throws MapFileFormatException {
        Territory targetTerritory = gameData.getOrCreateTerritory_ByName(getTerritoryName_FromLine(line));
        Polygon polygon = new Polygon();

        Matcher polygonMatcher = coordinatesPattern.matcher(line);

        while (polygonMatcher.find()) {
            Point currentPoint = getCurrentPoint(polygonMatcher);
            polygon.addPoint(currentPoint.x, currentPoint.y);
        }

        targetTerritory.addPatch(polygon);
    }


    /**
     * Parses an capital which belongs to a patch.
     */
    private void parseCapital(String line) throws MapFileFormatException {
        Territory targetTerritory = gameData.getOrCreateTerritory_ByName(getTerritoryName_FromLine(line));
        Matcher matcher = coordinatesPattern.matcher(line);

        if (!matcher.find())
            throw new MapFileFormatException(dataSource, line);

        targetTerritory.setCapital(getCurrentPoint(matcher));
    }

    /**
     * Parses neighbors of a territory.
     */
    private void parseNeighbors(String line) throws MapFileFormatException {
        Territory targetTerritory = gameData.getOrCreateTerritory_ByName(getTerritoryName_FromLine(line));
        Matcher matcher = neighborsPattern.matcher(line);


        if (!matcher.find())
            throw new MapFileFormatException(dataSource, line);

        String[] neighbors = matcher.group(1)
                .split(" - ");

        for (String neighbor : neighbors) {
            Territory neighborTerritory = gameData.getOrCreateTerritory_ByName(neighbor);
            neighborTerritory.addNeighbor(targetTerritory);
            targetTerritory.addNeighbor(neighborTerritory);
        }

    }


    /**
     * Parses territories which belongs to a continent.
     */
    private void parseContinent(String line) throws MapFileFormatException {
        Matcher matcher = continentPattern.matcher(line);

        if (!matcher.find())
            throw new MapFileFormatException(dataSource, line);

        String continentName = matcher.group(1);
        int reinforcementBooster = Integer.parseInt(matcher.group(2));
        String[] territories = matcher.group(3).split(" - ");

        Continent continent = gameData.getOrCreateContinent_ByName(continentName);

        continent.setReinforcementBonus(reinforcementBooster);

        for (String territoryName : territories) {
            Territory territory = gameData.getOrCreateTerritory_ByName(territoryName);
            continent.addTerritory(territory);
        }
    }

    /**
     * parses the background image string from line.
     */
    private void parseBackgroundImage(String line) throws MapFileFormatException {
        Matcher matcher = backgroundImagePattern.matcher(line);

        if (!matcher.find())
            throw new MapFileFormatException(dataSource, line);

        gameData.setBackgroundImageString(matcher.group(1));
    }

    /**
     * gets the name of the territory from line.
     */
    private String getTerritoryName_FromLine(String line) throws MapFileFormatException {
        Matcher matcher = lineIdentifierPattern.matcher(line);

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
    }
}
