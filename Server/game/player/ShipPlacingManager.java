package Server.game.player;

import Server.game.cell.CellCoordinates;
import Server.game.cell.ShipsCell;
import Server.game.map.ShipsMap;
import Server.game.utility.Directions;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for placing ships on the map based on user input.
 * Handles both one-cell and multi-cell ships, ensuring valid placement and marking adjacent cells as blocked.
 */
public class ShipPlacingManager {

    private ShipsMap _map;

    /**
     * Constructs a new ship placing manager with the given map.
     *
     * @param map the ship map to place ships on
     */
    public ShipPlacingManager(ShipsMap map) {
        this._map = map;
    }

    /**
     * Attempts to place a ship of a given length starting from a coordinate in a specific direction.
     *
     * @param shipLength the length of the ship
     * @param startingCooridinate the starting position of the ship
     * @param direction the direction in which the ship should extend
     * @param id a unique identifier for the ship
     * @return true if the ship was successfully placed; false otherwise
     */
    public boolean placeShip(int shipLength, CellCoordinates startingCooridinate, Directions direction, int id) {
        if (shipLength == 1) {
            return addOneCellShip(startingCooridinate, id);
        } else {
            return addMultipeCellShip(startingCooridinate, direction, shipLength, id);
        }
    }

    /**
     * Attempts to place a one-cell ship at the given coordinates.
     *
     * @param coordinates the location of the ship
     * @param id the ID to assign to the ship
     * @return true if placement is valid and successful; false otherwise
     */
    private boolean addOneCellShip(CellCoordinates coordinates, int id) {
        List<ShipsCell> shipCells = new ArrayList<>();
        boolean isPossible = isPossibleToShip(coordinates);

        if (isPossible) {
            shipCells.add(_map.getCellAt(coordinates));
            markAsShip(shipCells, id);
            markSurroundingCellsBlocked(shipCells);
        }

        return isPossible;
    }

    /**
     * Attempts to place a multi-cell ship starting from a coordinate in a given direction.
     *
     * @param start the starting coordinate
     * @param direction the direction of the ship
     * @param length the length of the ship
     * @param id the ID to assign to the ship
     * @return true if placement is valid and successful; false otherwise
     */
    private boolean addMultipeCellShip(CellCoordinates start, Directions direction, int length, int id) {
        List<ShipsCell> shipCells = new ArrayList<>();

        int dx = 0;
        int dy = 0;

        switch (direction) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
        }

        for (int i = 0; i < length; i++) {
            int x = start.getX() + i * dx;
            int y = start.getY() + i * dy;
            CellCoordinates current = new CellCoordinates(x, y);

            if (!_map.areCoordinatesInBounds(current)) return false;

            ShipsCell cell = _map.getCellAt(current);
            if (cell == null || !cell.isPossibleToShip()) return false;

            shipCells.add(cell);
        }

        markAsShip(shipCells, id);
        markSurroundingCellsBlocked(shipCells);
        return true;
    }

    /**
     * Marks all surrounding cells of the given ship cells as blocked (unavailable for ship placement).
     *
     * @param shipCells list of ship cells whose neighbors will be blocked
     */
    private void markSurroundingCellsBlocked(List<ShipsCell> shipCells) {
        for (ShipsCell shipCell : shipCells) {
            int baseX = shipCell.getX();
            int baseY = shipCell.getY();

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;

                    int newX = baseX + dx;
                    int newY = baseY + dy;
                    CellCoordinates neighborCoords = new CellCoordinates(newX, newY);

                    if (_map.areCoordinatesInBounds(neighborCoords)) {
                        ShipsCell neighbor = _map.getCellAt(neighborCoords);
                        if (neighbor != null && neighbor.isPossibleToShip()) {
                            neighbor.setPossibleToShip(false);
                        }
                    }
                }
            }
        }
    }

    /**
     * Marks the given cells as belonging to a ship with a specific ID.
     *
     * @param cells the cells to mark as part of the ship
     * @param id the ship ID to assign to each cell
     */
    private void markAsShip(List<ShipsCell> cells, int id) {
        for (ShipsCell cell : cells) {
            cell.setShip(true);
            cell.setPossibleToShip(false);
            cell.setShipId(id);
        }
    }

    /**
     * Checks if a ship can be placed at the given coordinates.
     *
     * @param coordinates the location to check
     * @return true if the coordinates are valid and available for ship placement; false otherwise
     */
    private boolean isPossibleToShip(CellCoordinates coordinates) {
        if (!_map.areCoordinatesInBounds(coordinates)) return false;
        ShipsCell cell = _map.getCellAt(coordinates);
        return cell.isPossibleToShip();
    }
}
