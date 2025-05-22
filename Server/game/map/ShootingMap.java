package Server.game.map;

import Server.game.cell.ShootingCell;

import java.io.Serializable;

public class ShootingMap extends Map<ShootingCell> implements Serializable {

    private static final long serialVersionUID = 1L;

    public ShootingMap(int size) {
        super(size);
    }

    public ShootingMap(ShootingMap other) {
        super(other.getSize());
        this._cells.clear(); // Clear what super() added
        for (ShootingCell cell : other.get_cells()) {
            this._cells.add(new ShootingCell(cell)); // assumes ShootingCell has a copy constructor
        }
    }

    @Override
    protected void initialize(int size) {
        for (int y = 1; y <= size; y++) {
            for (int x = 1; x <= size; x++) {
                _cells.add(new ShootingCell(x, y));
            }
        }
    }
}
