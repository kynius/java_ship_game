package Server.game.utility;

import Server.game.cell.CellCoordinates;
import Server.game.utility.ShotStatuses;

import java.io.Serializable;

public class ShotStatus implements Serializable {
    private ShotStatuses status;
    private CellCoordinates shootCoordinate;

    public ShotStatus(ShotStatuses status, CellCoordinates shootCoordinate) {
        this.status = status;
        this.shootCoordinate = shootCoordinate;
    }
    public ShotStatus(){

    }
    public ShotStatuses getStatus() {
        return status;
    }

    public CellCoordinates getShootCoordinate() {
        return shootCoordinate;
    }
    public void updateFrom(ShotStatus other) {
        this.status = other.status;
        this.shootCoordinate = other.shootCoordinate;
    }

}