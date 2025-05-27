package DTOs;

import Server.game.map.ShipsMap;

import java.io.Serializable;
/**
 * Data transfer object representing a request for ship placement.
 * Contains the ship's ID, its length, and the current state of the player's ship map.
 */
public class ShipPlacementRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int shipId;
    private final int shipLength;
    private final ShipsMap shipMap;
    /**
     * Constructs a new ShipPlacementRequestDto.
     *
     * @param shipId    the unique identifier of the ship to be placed
     * @param shipLength the length of the ship to be placed
     * @param shipMap   the current state of the player's ship map
     */
    public ShipPlacementRequestDto(int shipId, int shipLength, ShipsMap shipMap) {
        this.shipId = shipId;
        this.shipLength = shipLength;
        this.shipMap = shipMap;
    }
    /**
     * Returns the unique identifier of the ship to be placed.
     *
     * @return the ship ID
     */
    public int getShipId() {
        return shipId;
    }
    /**
     * Returns the length of the ship to be placed.
     *
     * @return the ship length
     */
    public int getShipLength() {
        return shipLength;
    }
    /**
     * Returns the current state of the player's ship map.
     *
     * @return the ship map
     */
    public ShipsMap getShipmap() {
        return shipMap;
    }
}
