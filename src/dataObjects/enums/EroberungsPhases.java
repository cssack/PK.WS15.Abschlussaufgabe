/*
 * Copyright (c) 2016. Tobias Patzl, Christian Sack
 */

package dataObjects.enums;

public enum EroberungsPhases {

    /**
     * Distributing the available reinforcements over the territories.
     */
    VerstaerkungVerteilen,
    /**
     * Attacking territory of other player.
     */
    Angreifen,

    /**
     * Moving towards new conquered territory.
     */
    BewegenZumAngriff,

    /**
     * Move army from one territory to its neighbor.
     */
    TruppenTransport

}
