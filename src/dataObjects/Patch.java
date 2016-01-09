/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.awt.*;

/**
 * A patch of land for a territory. A territory consist of one or more patches. One patch belongs to exactly one
 * territory.
 */
public class Patch {
    private final Territory territory;
    private final Polygon polygon;

    public Patch(Territory territory, Polygon polygon) {
        this.territory = territory;
        this.polygon = polygon;
    }


    /**
     * @return the owning territory of the patch. Each patch belongs to a territory.
     */
    public Territory getTerritory() {
        return territory;
    }

    /**
     * @return the polygon which can be used to draw the patch which belongs to a territory.
     */
    public Polygon getPolygon() {
        return polygon;
    }
}
