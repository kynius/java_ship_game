package DTOs;

import Server.game.cell.CellCoordinates;
import Server.game.utility.Directions;

import java.io.Serializable;

public class ShipPlacementDto implements Serializable {
    private final CellCoordinates cellCoordinates;
    private final Directions direction;

    public ShipPlacementDto(CellCoordinates cellCoordinates, Directions direction) {
        this.cellCoordinates = cellCoordinates;
        this.direction = direction;
    }

    public CellCoordinates getCoordinates() {
        return this.cellCoordinates;
    }

    public Directions getDirection() {
        return direction;
    }
}
