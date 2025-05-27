package Server.game.cell;

import java.io.Serializable;

/**
 * Abstract base class representing a cell on the game board.
 * Each cell has immutable coordinates and can be extended for specific cell types.
 * Implements {@link Serializable} for object serialization.
 */
public abstract class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private final CellCoordinates coordinates;
    /**
     * Abstract base class representing a cell on the game board.
     * Each cell has immutable coordinates and can be extended for specific cell types.
     * Implements {@link Serializable} for object serialization.
     */
    public Cell(int x, int y) {
        this.coordinates = new CellCoordinates(x, y);
    }
    /**
     * Returns the coordinates of this cell.
     *
     * @return the cell's coordinates
     */
    public CellCoordinates getCoordinates() {
        return coordinates;
    }
    /**
     * Returns the x-coordinate of this cell.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return coordinates.getX();
    }
    /**
     * Returns the y-coordinate of this cell.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return coordinates.getY();
    }
}
