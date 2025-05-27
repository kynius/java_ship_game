package Server.game.cell;

import java.io.Serializable;
import java.util.Objects;
/**
 * Represents immutable coordinates of a cell on the game board.
 * Used for identifying cell positions by their x and y values.
 * Implements {@link Serializable} for object serialization.
 */
public class CellCoordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int x;
    private final int y;
    /**
     * Constructs cell coordinates with the specified x and y values.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public CellCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Returns the x-coordinate.
     *
     * @return the x value
     */
    public int getX() {
        return x;
    }
    /**
     * Returns the y-coordinate.
     *
     * @return the y value
     */
    public int getY() {
        return y;
    }
    /**
     * Checks if this object is equal to another object.
     * Two {@code CellCoordinates} are equal if their x and y values are the same.
     *
     * @param obj the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CellCoordinates that = (CellCoordinates) obj;
        return x == that.x && y == that.y;
    }
    /**
     * Returns the hash code for this object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}