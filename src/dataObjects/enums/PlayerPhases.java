/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.enums;

public enum PlayerPhases {

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
     * Player currently attacking with army.
     */
    Attacking,

    /**
     * Player currently finished with attacking and won the territory.
     */
    AttackedWin,

    /**
     * Player currently finished with attacking and lost the battle.
     */
    AttackedLost,

    /**
     * Army got transferred.
     */
    ArmyTransferred,

    /**
     * Round finished
     */
    RoundFinished,

}
