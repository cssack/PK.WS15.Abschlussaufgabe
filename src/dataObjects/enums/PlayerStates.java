/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.enums;

/**
 * Player states
 */
public enum PlayerStates {

    /**
     * Currently the player is waiting for input or the other to player to finish.
     */
    Waiting,

    /**
     * Distributing the available reinforcements over the territories.
     */
    Reinforcing,

    /**
     * Player is currently selecting a territory for moving or attacking purpose.
     */
    FirstTerritorySelection,

    /**
     * Player has currently selected an territory. Now he can either attack or move.
     */
    FirstTerritorySelected,

    /**
     * Round finished
     */
    RoundFinished,

    /**
     * Player is currently fortifying the won territories.
     */
    Fortifying,

}
