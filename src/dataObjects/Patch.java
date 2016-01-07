/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.awt.*;

/**
 * Created by chris on 07.01.2016.
 * A patch of land for a territory.
 * A territory consist of one or more patches.
 */
public class Patch {
    private final Territory territory;
    private final Polygon polygon;

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
}
