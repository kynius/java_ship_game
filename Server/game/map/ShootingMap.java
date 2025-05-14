package game.map;

import game.cell.ShootingCell;

public class ShootingMap extends Map<ShootingCell> {

    public ShootingMap(int size) {
        super(size);
    }

    @Override
    protected void initialize(int size) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                _cells.add(new ShootingCell(x, y));
            }
        }
    }
}
