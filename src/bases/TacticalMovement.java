/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package bases;

import dataObjects.Territory;

/**
 * A pair of two generic instances.
 */
public class TacticalMovement {
    public final Territory from;
    public final Territory to;
    private int armyCount;

    public TacticalMovement(Territory from, Territory to) {
        assert from != to;
        this.from = from;
        this.to = to;
    }

    public TacticalMovement(Territory from, Territory to, int armyCount) {
        this(from, to);
        this.armyCount = armyCount;
    }

    @Override
    public int hashCode() {
        return from.hashCode() ^ to.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        TacticalMovement movement = (TacticalMovement) obj;
        return this.from == movement.from && this.to == movement.to;
    }


    public int getArmyCount() {
        return armyCount;
    }

    public void setArmyCount(int armyCount) {
        this.armyCount = armyCount;
    }

    public void increaseArmys() {
        this.armyCount++;
    }

    public void decreaseArmys() {
        this.armyCount--;
    }

    public boolean consitsOf(Territory t1, Territory t2) {
        return contains(t1) && contains(t2);
    }

    public boolean contains(Territory t) {
        return from == t || to == t;
    }
}
