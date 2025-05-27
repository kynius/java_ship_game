package Server.game.cell;

import java.io.Serializable;
/**
 * Represents a cell on the game board that may contain a ship.
 * Stores information about ship presence, hit status, and ship assignment.
 * Extends {@link Cell} and implements {@link Serializable} for object serialization.
 */
public class ShipsCell extends Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean isShip = false;
    private boolean isPossibleToShip = true;
    private boolean isHit = false;
    private Integer shipId = null;
    /**
     * Constructs a ShipsCell at the specified coordinates.
     *
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    public ShipsCell(int x, int y) {
        super(x, y);
    }
    /**
     * Returns whether this cell contains a ship.
     *
     * @return true if the cell contains a ship; false otherwise
     */
    public boolean isShip() {
        return isShip;
    }
    /**
     * Sets whether this cell contains a ship.
     *
     * @param isShip true if the cell should contain a ship; false otherwise
     */
    public void setShip(boolean isShip) {
        this.isShip = isShip;
    }
    /**
     * Returns whether it is possible to place a ship in this cell.
     *
     * @return true if a ship can be placed; false otherwise
     */
    public boolean isPossibleToShip() {
        return isPossibleToShip;
    }
    /**
     * Sets whether it is possible to place a ship in this cell.
     *
     * @param isPossibleToShip true if a ship can be placed; false otherwise
     */
    public void setPossibleToShip(boolean isPossibleToShip) {
        this.isPossibleToShip = isPossibleToShip;
    }
    /**
     * Returns whether this cell has been hit.
     *
     * @return true if the cell has been hit; false otherwise
     */
    public boolean isHit() {
        return isHit;
    }
    /**
     * Sets the hit status of this cell.
     *
     * @param isHit true if the cell has been hit; false otherwise
     */
    public void setHit(boolean isHit) {
        this.isHit = isHit;
    }
    /**
     * Returns the ID of the ship occupying this cell, if any.
     *
     * @return the ship ID, or null if no ship is present
     */
    public Integer getShipId() {
        return this.shipId;
    }
    /**
     * Sets the ID of the ship occupying this cell.
     *
     * @param shipId the ship ID to assign, or null if no ship is present
     */
    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }
}