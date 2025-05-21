package DTOs;

import Server.game.map.ShipsMap;

import java.io.Serializable;

public class ShipPlacementRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int shipId;
    private final int shipLength;
    private final ShipsMap shipMap;

    public ShipPlacementRequestDto(int shipId, int shipLength, ShipsMap shipMap) {
        this.shipId = shipId;
        this.shipLength = shipLength;
        this.shipMap = shipMap;
    }

    public int getShipId() {
        return shipId;
    }

    public int getShipLength() {
        return shipLength;
    }
    public ShipsMap getShipmap() {
        return shipMap;
    }
}
