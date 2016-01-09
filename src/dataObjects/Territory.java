/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.awt.*;
import java.util.ArrayList;

/**
 * A territory is occupied by an occupant which can be a player or a computer. The territory consists of one or more
 * patches. A territory have neighbors.
 */
public class Territory {
    private final String name;
    private final ArrayList<Patch> patches = new ArrayList<>();
    private final ArrayList<Territory> neighbors = new ArrayList<>();
    private Capital capital;
    private Player occupant = null;
    private int armyCount = 0;

    public Territory(String name) {
        this.name = name;
    }


    /**
     * @return the name of the territory as described in the map file.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the neighbors of the territory as described in the map file
     */
    public ArrayList<Territory> getNeighbors() {
        return neighbors;
    }

    /**
     * @return the capital which stores the position which can or should be used for the army count in the map drawing.
     */
    public Capital getCapital() {
        return capital;
    }

    /**
     * Set the capital, should only be used in game init phase.
     */
    public void setCapital(Point point) {
        this.capital = new Capital(this, point);
    }

    /**
     * @return the player who owns this territory. Consider that the occupant can be null in 'Landerwerb' phase.
     */
    public Player getOccupant() {
        return occupant;
    }

    /**
     * Set the Occupant of this territory.
     */
    public void setOccupant(Player occupant) {
        this.occupant = occupant;
    }

    /**
     * get the patches which belongs to this territory.
     */
    public ArrayList<Patch> getPatches() {
        return patches;
    }


    /**
     * get the army's stationed at this territory.
     */
    public int getArmyCount() {
        return armyCount;
    }

    /**
     * set the army's stationed at this territory.
     */
    public void setArmyCount(int armyCount) {
        this.armyCount = armyCount;
    }

    /**
     * increases the army's stationed at this territory by one.
     */
    public void increaseArmyCount() {
        this.armyCount++;
    }


    /**
     * decreases the army's stationed at this territory by one.
     */
    public void decreaseArmyCount() {
        this.armyCount--;
    }


    public void addPatch(Polygon p) {
        patches.add(new Patch(this, p));
    }

    public void addNeighbor(Territory t) {
        neighbors.add(t);
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * @return true if any of the patches which are associated with this territory contains this point.
     */
    public boolean contains(Point p) {
        for (Patch patch : patches) {
            if (patch.getPolygon().contains(p))
                return true;
        }
        return false;
    }
}
