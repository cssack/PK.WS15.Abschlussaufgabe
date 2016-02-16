/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.enums;


/**
 * The phases of the game.
 */
public enum Phases {
    /**
     * The game is currently not started.
     */
    NotStarted,


    /**
     * The players selects their starting territories each with one army recruited.
     */
    Landerwerb,

    /**
     * Current game phase is reinforcement. In this phase the players gathers their reinforcement and apply's them to
     * their territories.
     */
    Reinforcement,

    /**
     * Current game phase is attack or move. In this phase the players can attack other territories or move army from
     * one owned territory to the other.
     */
    AttackOrMove,

    /**
     * All Movements will be visible before movements will be executed.
     */
    QuickOverViewBefore,

    /**
     * After a successful attack the players can fortify their newly won territories by moving troops from the attacking
     * to the new territory. ("Nachziehen")
     */
    Fortifying,

    /**
     * The game has ended. The winner is the player which owns all territories.
     */
    End,
}
