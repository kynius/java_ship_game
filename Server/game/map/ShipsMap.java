package Server.game.map;

import Server.game.cell.CellCoordinates;
import Server.game.cell.ShipsCell;

import java.io.Serializable;

public class ShipsMap extends Map<ShipsCell> implements Serializable {

    private static final long serialVersionUID = 1L;

    public ShipsMap(int size) {
        super(size);
    }

    @Override
    protected void initialize(int size) {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                _cells.add(new ShipsCell(x, y));
            }
        }
    }

    public boolean hasShipAt(CellCoordinates coordinates) {
        ShipsCell cell = getCellAt(coordinates);
        return cell != null && cell.isShip() && cell.getShipId() != null;
    }

    public boolean hasUnhitCellOfShip(int shipId) {
        return _cells.stream()
                .anyMatch(cell -> cell.isShip()
                        && shipId == cell.getShipId()
                        && !cell.isHit());
    }
}
