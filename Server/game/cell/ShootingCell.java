package Server.game.cell;

import java.io.Serializable;
/**
 * Represents a cell on the shooting map used to track shots and aiming status.
 * Extends {@link Cell} and implements {@link Serializable} for object serialization.
 */
public class ShootingCell extends Cell implements Serializable {
    private boolean isShot = false;
    private boolean isAimed = false;
    /**
     * Copy constructor. Creates a new {@code ShootingCell} by copying another.
     *
     * @param other the cell to copy
     */
    public ShootingCell(ShootingCell other) {
        super(other.getX(), other.getY());
        this.isShot = other.isShot;
        this.isAimed = other.isAimed;
    }
    /**
     * Constructs a {@code ShootingCell} at the specified coordinates.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    public ShootingCell(int x, int y) {
        super(x, y);
    }
    /**
     * Returns whether this cell has been shot.
     *
     * @return true if the cell has been shot; false otherwise
     */
    public boolean isShot() {
        return isShot;
    }
    /**
     * Sets the shot status of this cell.
     *
     * @param isShot true if the cell has been shot; false otherwise
     */
    public void setShot(boolean isShot) {
        this.isShot = isShot;
    }
    /**
     * Returns whether this cell is currently aimed at.
     *
     * @return true if the cell is aimed at; false otherwise
     */
    public boolean isAimed() {
        return isAimed;
    }
    /**
     * Sets the aimed status of this cell.
     *
     * @param isAimed true if the cell is aimed at; false otherwise
     */
    public void setIsAimed(boolean isAimed) {
        this.isAimed = isAimed;
    }
}
