package Server.game.map;

import Server.game.cell.ShootingCell;

import java.io.Serializable;

/**
 * Represents a shooting map used by a player to track their shots.
 * Each cell is a {@link ShootingCell}, which holds information about whether a cell was shot and aimed.
 */
public class ShootingMap extends Map<ShootingCell> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new shooting map with the specified size.
     *
     * @param size the width and height of the square map
     */
    public ShootingMap(int size) {
        super(size);
    }

    /**
     * Creates a deep copy of another {@code ShootingMap}, duplicating its cells.
     *
     * @param other the map to copy
     */
    public ShootingMap(ShootingMap other) {
        super(other.getSize());
        this._cells.clear();
        for (ShootingCell cell : other.get_cells()) {
            this._cells.add(new ShootingCell(cell)); 
        }
    }

    /**
     * Initializes the shooting map by filling it with {@link ShootingCell} objects.
     *
     * @param size the size of the map
     */
    @Override
    protected void initialize(int size) {
        for (int y = 1; y <= size; y++) {
            for (int x = 1; x <= size; x++) {
                _cells.add(new ShootingCell(x, y));
            }
        }
    }
}
