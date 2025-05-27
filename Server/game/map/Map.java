package Server.game.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Server.game.cell.Cell;
import Server.game.cell.CellCoordinates;

/**
 * Represents a generic square map containing cells of a specific type.
 * Used as a base class for specific map types like shooting maps or ship maps.
 *
 * @param <T> the type of cell this map stores, must extend {@link Cell}
 */
public abstract class Map<T extends Cell> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected List<T> _cells;
    private int _size;

    /**
     * Default constructor for serialization or subclass use.
     */
    public Map() {
    }

    /**
     * Constructs a map with the given size and initializes it.
     *
     * @param size the size of the map (both width and height)
     */
    public Map(int size) {
        this._cells = new ArrayList<>();
        initialize(size);
        this._size = size;
    }

    /**
     * Returns the list of all cells on the map.
     *
     * @return list of cells
     */
    public List<T> get_cells() {
        return _cells;
    }

    /**
     * Adds a new cell to the map.
     *
     * @param cell the cell to add
     */
    public void addCell(T cell) {
        _cells.add(cell);
    }

    /**
     * Returns the size (width and height) of the map.
     *
     * @return map size
     */
    public int getSize() {
        return _size;
    }

    /**
     * Retrieves a cell located at the specified coordinates.
     *
     * @param coordinates the coordinates to retrieve the cell from
     * @return the cell at the coordinates, or {@code null} if not found
     */
    public T getCellAt(CellCoordinates coordinates) {
        for (T cell : _cells) {
            if (cell.getCoordinates().equals(coordinates)) {
                return cell;
            }
        }
        return null;
    }

    /**
     * Checks if the given coordinates are within the bounds of the map.
     *
     * @param coordinates the coordinates to check
     * @return true if within bounds; false otherwise
     */
    public boolean areCoordinatesInBounds(CellCoordinates coordinates) {
        return coordinates.getX() > 0 &&
                coordinates.getX() <= _size &&
                coordinates.getY() > 0 &&
                coordinates.getY() <= _size;
    }

    /**
     * Removes a cell at the specified coordinates.
     *
     * @param coordinates the coordinates of the cell to remove
     */
    public void removeCellAt(CellCoordinates coordinates) {
        _cells.removeIf(cell -> cell.getCoordinates().equals(coordinates));
    }

    /**
     * Removes all cells surrounding the specified center cell (excluding the center itself).
     *
     * @param center the center coordinates
     */
    public void removeCellsAround(CellCoordinates center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                CellCoordinates neighbor = new CellCoordinates(center.getX() + dx, center.getY() + dy);
                removeCellAt(neighbor);
            }
        }
    }

    /**
     * Returns the cell at a given index in the list.
     *
     * @param index the index to retrieve
     * @return the cell at the specified index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public T getCellAtIndex(int index) {
        if (index < 0 || index >= _cells.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        return _cells.get(index);
    }

    /**
     * Initializes the map's internal structure based on the given size.
     * This method must be implemented by subclasses.
     *
     * @param size the size of the map
     */
    protected abstract void initialize(int size);
}
