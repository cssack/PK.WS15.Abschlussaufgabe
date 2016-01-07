package dataObjects;

import java.util.ArrayList;

/**
 * Created by chris on 06.01.2016.
 */
public class Continent {
    private String name;
    private int reinforcementBonus;
    private ArrayList<Territory> territories = new ArrayList<>();

    public Continent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public Territory getTerritory_ByName(String Name) {
        for (Territory territory : territories) {
            if (territory.getName().equals(Name))
                return territory;
        }
        return null;
    }

    public void addTerritory(Territory territory) {
        this.territories.add(territory);
    }

    public void setReinforcementBonus(int reinforcementBonus) {
        this.reinforcementBonus = reinforcementBonus;
    }
}
