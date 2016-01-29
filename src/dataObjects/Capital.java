/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects;

import java.awt.*;

/**
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

    @Override
    public String toString() {
        return String.format("Capital: [Owner: {%s}, Point: {%s}]", this.owner.getName(), this.point);
    }
}
