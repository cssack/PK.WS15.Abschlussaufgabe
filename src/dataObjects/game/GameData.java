/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.game;

import dataObjects.Continent;
import dataObjects.Territory;

import java.util.ArrayList;

/**
 * Created by chris on 07.01.2016.
 * The game data is the place where all data items are stored.
 * This includes the continents the territories, the players and their stats and so on.
 */
public class GameData {
    private ArrayList<Territory> allTerritories = new ArrayList<>();
    private ArrayList<Continent> allContinents = new ArrayList<>();
    private Game game;

    public GameData(Game game) {
        this.game = game;
    }

    public ArrayList<Continent> getAllContinents() {
        return allContinents;
    }

    /**
     * @param name the name of the continent.
     * @return the continent by name or null if not exists.
     */
    public Continent getContinent_ByName(String name) {
        for (Continent continent : allContinents) {
            if (continent.getName().equals(name))
                return continent;
        }
        return null;
    }

    /**
     * Adds a new continent to the list.
     *
     * @param continent the continent to add
     */
    public void addContinent(Continent continent) {
        allContinents.add(continent);
    }

    /**
     * @param name the name of the territory.
     * @return the territory by name or null if not exists.
     */
    public Territory getTerritory_ByName(String name) {
        for (Territory territory : allTerritories) {
            if (territory.getName().equals(name))
                return territory;
        }
        return null;
    }

    /**
     * @return A list of all available territories.
     */
    public ArrayList<Territory> getAllTerritories() {
        return allTerritories;
    }

    /**
     * Adds a new territory to the list.
     * @param territory the territory to add
     */
    public void addTerritory(Territory territory) {
        allTerritories.add(territory);
    }
}
