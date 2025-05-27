package DTOs;

import Server.game.cell.CellCoordinates;
import Server.game.utility.Directions;

import java.io.Serializable;
/**
 * Data transfer object representing a ship placement action.
 * Contains the starting cell coordinates and the direction for placing a ship on the map.
 */
public class ShipPlacementDto implements Serializable {
    private final CellCoordinates cellCoordinates;
    private final Directions direction;
    /**
     * Constructs a new ShipPlacementDto.
     *
     * @param cellCoordinates the starting coordinates for the ship placement
     * @param direction the direction in which the ship is placed
     */
    public ShipPlacementDto(CellCoordinates cellCoordinates, Directions direction) {
        this.cellCoordinates = cellCoordinates;
        this.direction = direction;
    }
    /**
     * Returns the starting coordinates for the ship placement.
     *
     * @return the cell coordinates
     */
    public CellCoordinates getCoordinates() {
        return this.cellCoordinates;
    }
    /**
     * Returns the direction in which the ship is placed.
     *
     * @return the direction
     */
    public Directions getDirection() {
        return direction;
    }
}
