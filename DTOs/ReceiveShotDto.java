package DTOs;

import Server.game.cell.CellCoordinates;
import Server.game.map.ShipsMap;
import java.io.Serializable;

/**
 * Data transfer object representing the result of a shot received by the player.
 * Contains the current state of the player's ships map and the coordinates of the last shot.
 */
public class ReceiveShotDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ShipsMap _shipsMap;
    private final CellCoordinates _lastShot;
    /**
     * Constructs a new ReceiveShotDto.
     *
     * @param shipsMap the current state of the player's ships map
     * @param lastShot the coordinates of the last shot received
     */
    public ReceiveShotDto(ShipsMap shipsMap, CellCoordinates lastShot) {
        this._shipsMap = shipsMap;
        this._lastShot = lastShot;
    }
    /**
     * Returns the current state of the player's ships map.
     *
     * @return the ships map
     */
    public ShipsMap getShipsMap() {
        return _shipsMap;
    }
    /**
     * Returns the coordinates of the last shot received.
     *
     * @return the last shot coordinates
     */
    public CellCoordinates getLastShot() {
        return _lastShot;
    }
}