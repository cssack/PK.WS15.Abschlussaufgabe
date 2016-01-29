/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.util.ArrayList;

/**
 * A continent consists of multiple territories. If a player has all territories on a continent he gains a
 * reinforcementBonus.
 */
public class Continent {
    private final String name;
    private final ArrayList<Territory> territories = new ArrayList<>();
    private int reinforcementBonus;

    public Continent(String name) {
        this.name = name;
    }


    /**
     * @return the name of the continent
     */
    public String getName() {
        return name;
    }

    /**
     * @return all territories which belongs to the continent.
     */
    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    /**
     * Adds a territory to the continent, this method should only be used in the init phase of the game.
     */
    public void addTerritory(Territory territory) {
        this.territories.add(territory);
        territory.setContinent(this);
    }

    /**
     * @return the reinforcement bonus for the owner of the continent.
     */
    public int getReinforcementBonus() {
        return reinforcementBonus;
    }

    /**
     * Sets the reinforcement bonus of the continent, this method should only be used in the init phase of the game.
     */
    public void setReinforcementBonus(int reinforcementBonus) {
        this.reinforcementBonus = reinforcementBonus;
    }

    @Override
    public String toString() {
        return String.format("Continent: [name: {%s}, reinforcementBonus: {%d}]", this.name, this.reinforcementBonus);
    }
}
