/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package game;

import bases.GameBase;
import dataObjects.Continent;
import dataObjects.Player;
import dataObjects.Territory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The game data is the place where all data items are stored. This includes the continents the territories, the
 * players, their stats and so on. No game logic should be included inside the data classes.
 */
public class GameData extends GameBase {
    //TODO there could be some performance improvements for the init if those two list would be converted into Maps,
    //TODO but this results in other problems with the drawing routines where the territories needs to be iterated in a reverse order
    private final ArrayList<Territory> allTerritories = new ArrayList<>();
    private final ArrayList<Continent> allContinents = new ArrayList<>();
    private final Random rand = new Random();
    private Player humanPlayer;
    private Player compPlayer;

    /**
     * Initializes the base class.
     */
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

        return allContinents.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * @param name the name of the territory.
     * @return the territory by name or null if not exists.
     */
    public Territory getTerritory_ByName(String name) {
        assert name != null && !name.equals("") : "name is null or empty";

        return allTerritories.stream().filter(t -> t.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Used for the game init phase only.
     *
     * @param name the name of the territory.
     * @return the territory by name.
     */
    public Territory getOrCreateTerritory_ByName(String name) {
        Territory territory = getTerritory_ByName(name);
        if (territory == null) {
            territory = new Territory(name);
            allTerritories.add(territory);
        }
        return territory;
    }

    /**
     * Used for the game init phase only.
     *
     * @param name the name of the continent.
     * @return the continent by name.
     */
    public Continent getOrCreateContinent_ByName(String name) {
        Continent continent = getContinent_ByName(name);
        if (continent == null) {
            continent = new Continent(name);
            allContinents.add(continent);
        }
        return continent;
    }

    /**
     * Gets a random territory from a list of territories
     */
    public Territory getRandomTerritory(List<Territory> from) {
        return from.get(rand.nextInt(from.size()));
    }

    /**
     * @return a randomly chosen not occupied territory or null if all territories have been assigned.
     */
    public Territory getRandomUnassignedTerritory() {
        if (state.getOccupiedTerritories() == allTerritories.size())
            return null;

        return getRandomTerritory(allTerritories.stream().filter(Territory::isUnoccupied)
                .collect(Collectors.toList()));
    }
}
