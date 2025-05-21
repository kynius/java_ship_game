package Server.game.map;

import Server.game.cell.ShootingCell;

import java.io.Serializable;

public class ShootingMap extends Map<ShootingCell> implements Serializable {

    private static final long serialVersionUID = 1L;

    public ShootingMap(int size) {
        super(size);
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
