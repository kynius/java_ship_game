package Server.game.utility;

import Server.game.cell.CellCoordinates;
import Server.game.utility.ShotStatuses;

import java.io.Serializable;

public class ShotStatus implements Serializable {
    private final ShotStatuses status;
    private final CellCoordinates shootCoordinate;

    public ShotStatus(ShotStatuses status, CellCoordinates shootCoordinate) {
        this.status = status;
        this.shootCoordinate = shootCoordinate;
    }

    public ShotStatuses getStatus() {
        return status;
    }

    public CellCoordinates getShootCoordinate() {
        return shootCoordinate;
    }
}