package dataObjects.game;

import dataObjects.Continent;
import dataObjects.Territory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by chris on 07.01.2016.
 */
public class GameData {
    private ArrayList<Territory> allTerritories = new ArrayList<>();
    private HashMap<String, Continent> allContinents = new HashMap<>();


    private Game game;


    public GameData(Game game) {
        this.game = game;
    }





    public HashMap<String, Continent> getAllContinents() {
        return allContinents;
    }


    /**
     *
     * @param name the name of the territory.
     * @return the territory by name or null if not exists.
     */
    public Territory getTerritory_ByName(String name) {
        for (Territory territory : allTerritories) {
            if (Objects.equals(territory.getName(), name))
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
