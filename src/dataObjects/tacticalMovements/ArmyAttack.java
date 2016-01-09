/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.tacticalMovements;

import bases.Pair;
import dataObjects.Territory;

/**
 * Describes the army attack move.
 */
public class ArmyAttack extends Pair<Territory, Territory> {
    private int armys;

    public ArmyAttack(Territory first, Territory last) {
        super(first, last);
    }

    public int getArmys() {
        return armys;
    }

    public void setArmys(int armys) {
        this.armys = armys;
    }
}
