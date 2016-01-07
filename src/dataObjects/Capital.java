/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.awt.*;

/**
 * Created by chris on 06.01.2016.
 * The capital of a territory.
 */
public class Capital {
    private final Territory owner;
    private final Point point;

    public Capital(Territory owner, Point point) {
        this.owner = owner;
        this.point = point;
    }

    public Territory getOwner() {
        return owner;
    }

    public Point getPoint() {
        return point;
    }
}
