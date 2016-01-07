package dataObjects;

import dataObjects.enums.Occupants;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 06.01.2016.
 */
public class Territory {
    private Polygon polygon;
    private String name;
    private ArrayList<Territory> neighbors;
    private Capital capital;
    private Occupants Occupant = Occupants.NotDef;


    public Territory(String name) {
        this.name = name;

    }


    public void setNeighbors(ArrayList<Territory> neighbors) {
        this.neighbors = neighbors;
    }

    public void setCapital(Point point) {
        this.capital = new Capital(this, point);
    }

    public Polygon getPolygon() {

        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
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

    public Occupants getOccupant() {
        return Occupant;
    }

    public void setOccupant(Occupants occupant) {
        Occupant = occupant;
    }
}
