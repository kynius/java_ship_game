package Server.game.map;

import Server.game.cell.CellCoordinates;
import Server.game.cell.ShipsCell;

import java.io.Serializable;

/**
 * Represents a map used for tracking ship positions in the game.
 * Each cell on the map is a {@link ShipsCell}, containing information about ship placement and state.
 */
public class ShipsMap extends Map<ShipsCell> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for serialization or framework usage.
     */
    public ShipsMap() {
    }

    /**
     * Constructs a new ships map with the specified size.
     *
     * @param size the width and height of the square map
     */
    public ShipsMap(int size) {
        super(size);
    }

    /**
     * Initializes the ship map by filling it with {@link ShipsCell} objects.
     *
     * @param size the size of the map
     */
    @Override
    protected void initialize(int size) {
        for (int y = 1; y <= size; y++) {
            for (int x = 1; x <= size; x++) {
                _cells.add(new ShipsCell(x, y));
            }
        }
    }

    /**
     * Checks if a ship is present at the given coordinates.
     *
     * @param coordinates the cell coordinates to check
     * @return true if a ship exists at the coordinates; false otherwise
     */
    public boolean hasShipAt(CellCoordinates coordinates) {
        ShipsCell cell = getCellAt(coordinates);
        return cell != null && cell.isShip() && cell.getShipId() != null;
    }

    /**
     * Checks if the ship with the specified ID has any unhit cells remaining.
     *
     * @param shipId the ID of the ship to check
     * @return true if the ship has at least one unhit cell; false otherwise
     */
    public boolean hasUnhitCellOfShip(int shipId) {
        return _cells.stream()
                .anyMatch(cell -> cell.isShip()
                        && shipId == cell.getShipId()
                        && !cell.isHit());
    }
}
