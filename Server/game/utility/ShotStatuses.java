package Server.game.utility;

import java.io.Serializable;

/**
 * Represents the possible outcomes of a shot in the game.
 * Implements {@link Serializable} for potential network transmission or persistence.
 */
public enum ShotStatuses implements Serializable {

    /**
     * Indicates the shot missed the target.
     */
    MISSED,

    /**
     * Indicates the shot hit a ship but did not destroy it.
     */
    SHOT,

    /**
     * Indicates the shot hit and destroyed a ship.
     */
    SHOTNDESTORYED;
}