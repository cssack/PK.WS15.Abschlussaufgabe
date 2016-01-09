/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.tacticalMovements;

import bases.Pair;
import dataObjects.Territory;

/**
 * Describes the army transport move
 */
public class ArmyTransport extends Pair<Territory, Territory> {
    private int armys;

    public ArmyTransport(Territory first, Territory last) {
        super(first, last);
    }

    public void increaseArmys() {
        this.armys++;
    }

    public void decreaseArmys() {
        this.armys--;
    }

    public int getArmys() {
        return armys;
    }

    public void setArmys(int armys) {
        this.armys = armys;
    }


}
