package DTOs;

import Server.game.cell.CellCoordinates;
import Server.game.map.ShipsMap;

import java.io.Serializable;

public class ReceiveShotDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private final ShipsMap _shipsMap;
    private final CellCoordinates _lastShot;

    public ReceiveShotDto(ShipsMap shipsMap, CellCoordinates lastShot) {
        this._shipsMap = shipsMap;
        this._lastShot = lastShot;
    }

    public ShipsMap getShipsMap() {
        return _shipsMap;
    }

    public CellCoordinates getLastShot() {
        return _lastShot;
    }
}