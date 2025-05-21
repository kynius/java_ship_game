package DTOs;

import java.io.Serializable;

public class ShipPlacementRequestDto implements Serializable {
    private final int shipId;
    private final int shipLength;

    public ShipPlacementRequestDto(int shipId, int shipLength) {
        this.shipId = shipId;
        this.shipLength = shipLength;
    }

    public int getShipId() {
        return shipId;
    }

    public int getShipLength() {
        return shipLength;
    }
}
