/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Continent;
import dataObjects.Player;
import dataObjects.Territory;

import java.util.ArrayList;

/**
 * Created by chris on 07.01.2016.
 * The game data is the place where all data items are stored.
 * This includes the continents the territories, the players and their stats and so on.
 */
public class GameData extends GameBase {
    //TODO there could be some performance improvements for the init if those two list would be converted into Maps,
    //TODO but this results in other problems with the drawing routines where the territories needs to be iterated in a reverse order
    private final ArrayList<Territory> allTerritories = new ArrayList<>();
    private final ArrayList<Continent> allContinents = new ArrayList<>();
    private Player humanPlayer;
    private Player compPlayer;


    @Override
    public void init(Game game) {
        super.init(game);
        humanPlayer = new Player();
        compPlayer = new Player();
    }

    /**
     * @return the human player data.
     */
    public Player getHumanPlayer() {
        return humanPlayer;
    }

    /**
     * @return the computer player data.
     */
    public Player getCompPlayer() {
        return compPlayer;
    }


    /**
     * @return A list of all available continents
     */
    public ArrayList<Continent> getAllContinents() {
        return allContinents;
    }

    /**
     * @return A list of all available territories.
     */
    public ArrayList<Territory> getAllTerritories() {
        return allTerritories;
    }

    /**
     * @param name the name of the continent.
     * @return the continent by name or null if not exists.
     */
    public Continent getContinent_ByName(String name) {
        assert name != null && !name.equals("") : "name is null or empty";

        for (Continent continent : allContinents) {
            if (continent.getName().equals(name))
                return continent;
        }
        return null;
    }

    /**
     * @param name the name of the territory.
     * @return the territory by name or null if not exists.
     */
    public Territory getTerritory_ByName(String name) {
        assert name != null && !name.equals("") : "name is null or empty";

        for (Territory territory : allTerritories) {
            if (territory.getName().equals(name))
                return territory;
        }
        return null;
    }

    public Territory getOrCreateTerritory_ByName(String name) {
        Territory territory = getTerritory_ByName(name);
        if (territory == null) {
            territory = new Territory(name);
            allTerritories.add(territory);
        }
        return territory;
    }

    public Continent getOrCreateContinent_ByName(String name) {
        Continent continent = getContinent_ByName(name);
        if (continent == null) {
            continent = new Continent(name);
            allContinents.add(continent);
        }
        return continent;
    }
}
