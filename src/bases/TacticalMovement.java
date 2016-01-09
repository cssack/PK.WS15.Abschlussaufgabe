/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package bases;

import dataObjects.Territory;

/**
 * A tactical movement describes an army move from one territory to another. This could be an attack or a transfer
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


    /**
     * @return the involved army's in this movement.
     */
    public int getArmyCount() {
        return armyCount;
    }

    /**
     * increases the involved army's by one.
     */
    public void increaseArmys() {
        this.armyCount++;
    }

    /**
     * decreases the involved army's by one.
     */
    public void decreaseArmys() {
        this.armyCount--;
    }

    /**
     * check if this movement consists of two territories.
     */
    public boolean consitsOf(Territory t1, Territory t2) {
        return contains(t1) && contains(t2);
    }

    /**
     * check if the territory is either the source or the target of this movement.
     */
    public boolean contains(Territory t) {
        return from == t || to == t;
    }
}
