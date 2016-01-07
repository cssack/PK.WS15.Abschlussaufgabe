/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.util.ArrayList;

/**
 * Created by chris on 06.01.2016.
 * A continent consists of multiple territories.
 * If a player has all territories on a continent he gains a reinforcementBonus.
 */
public class Continent {
    private final String name;
    private final ArrayList<Territory> territories = new ArrayList<>();
    private int reinforcementBonus;

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
