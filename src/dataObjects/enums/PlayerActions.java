/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.enums;

public enum PlayerActions {
    /**
     * Distributing the available reinforcements over the territories.
     */
    ArmyReinforcement,

    /**
     * Player has currently selected an army.
     */
    ArmySelected,

    /**
     * Player currently attacking with army.
     */
    ArmyAttacking,

    /**
     * Player currently finished with attacking.
     */
    ArmyAttacked,

    /**
     * Army got transferred.
     */
    ArmyTransferred,

    /**
     * Round finished
     */
    RoundFinished,

}
