/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 06.01.2016.
 * A territory is occupied by an occupant which can be a player or a computer.
 * The territory consists of one or more patches.
 */
public class Territory {
    private final String name;
    private final ArrayList<Patch> patches = new ArrayList<>();
    private final ArrayList<Territory> neighbors = new ArrayList<>();
    private Capital capital;
    private Player occupant = null;

    public Territory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Territory> getNeighbors() {
        return neighbors;
    }

    public Capital getCapital() {
        return capital;
    }

    public void setCapital(Point point) {
        this.capital = new Capital(this, point);
    }

    public Player getOccupant() {
        return occupant;
    }

    public void setOccupant(Player occupant) {
        this.occupant = occupant;
    }

    public boolean contains(Point p) {
        for (Patch patch : patches) {
            if (patch.getPolygon().contains(p))
                return true;
        }
        return false;
    }

    public ArrayList<Patch> getPatches() {
        return patches;
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
}
