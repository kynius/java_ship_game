package Server.game.utility;

import Server.game.cell.CellCoordinates;

import java.io.Serializable;

/**
 * Represents the result of a shot in the game, including its status and target coordinate.
 * Implements {@link Serializable} to allow transmission or persistence.
 */
public class ShotStatus implements Serializable {

    private ShotStatuses status;
    private CellCoordinates shootCoordinate;

    /**
     * Constructs a new {@code ShotStatus} with the given shot result and coordinate.
     *
     * @param status the status of the shot (missed, hit, or destroyed)
     * @param shootCoordinate the coordinates where the shot was fired
     */
    public ShotStatus(ShotStatuses status, CellCoordinates shootCoordinate) {
        this.status = status;
        this.shootCoordinate = shootCoordinate;
    }

    /**
     * Default constructor for serialization purposes.
     */
    public ShotStatus() {
    }

    /**
     * Gets the result status of the shot.
     *
     * @return the shot status
     */
    public ShotStatuses getStatus() {
        return status;
    }

    /**
     * Gets the coordinates where the shot was made.
     *
     * @return the shot coordinates
     */
    public CellCoordinates getShootCoordinate() {
        return shootCoordinate;
    }

    /**
     * Updates this {@code ShotStatus} with the values from another instance.
     *
     * @param other another {@code ShotStatus} to copy values from
     */
    public void updateFrom(ShotStatus other) {
        this.status = other.status;
        this.shootCoordinate = other.shootCoordinate;
    }
}
