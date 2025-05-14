package game.utility;

import game.cell.CellCoordinates;

public class ShotStatus {
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