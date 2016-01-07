package dataObjects;

import java.awt.*;

/**
 * Created by chris on 07.01.2016.
 */
public class Patch {
    private Territory territory;
    private Polygon polygon;


    public Patch(Territory territory, Polygon polygon) {

        this.territory = territory;
        this.polygon = polygon;
    }


    public Territory getTerritory() {
        return territory;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
