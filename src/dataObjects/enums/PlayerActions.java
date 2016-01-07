/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.enums;

public enum PlayerActions {
    /**
     * Distributing the available reinforcements over the territories.
     */
    Reinforcement,



    /**
     * Player has currently selected an territory. Now he can either attack or transport multiple army's.
     */
    FirstTerritorySelected,

    /**
     * Player currently attacking with army.
     */
    Attacking,

    /**
     * Player currently finished with attacking.
     */
    Attacked,

    /**
     * Army got transferred.
     */
    ArmyTransferred,

    /**
     * Round finished
     */
    RoundFinished,

}
