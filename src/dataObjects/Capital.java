package dataObjects;

import java.awt.*;

/**
 * Created by chris on 06.01.2016.
 */
public class Capital {
    Territory owner;
    Point point;

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
