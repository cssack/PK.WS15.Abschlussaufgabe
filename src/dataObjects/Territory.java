package dataObjects;

import dataObjects.enums.Occupants;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 06.01.2016.
 */
public class Territory {
    private String name;
    private ArrayList<Patch> patches = new ArrayList<>();
    private ArrayList<Territory> neighbors = new ArrayList<>();
    private Capital capital;
    private Occupants Occupant = Occupants.NotDef;


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

    public Occupants getOccupant() {
        return Occupant;
    }

    public void setOccupant(Occupants occupant) {
        Occupant = occupant;
    }


    public boolean contains(Point p) {
        for (Patch patch : patches) {
            if (patch.getPolygon().contains(p))
                return true;
        }
        return false;
    }

    public ArrayList<Patch> getPaches() {
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
