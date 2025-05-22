package Server.game.player.computerPlayer;
import Server.game.cell.CellCoordinates;
import Server.game.map.ShootingMap;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;
import java.util.Random;

public class ComputerShootingManager {
    private final ShootingMap _shootingMap;
    private final ShootingMap _cellsLeftToShoot;
    private DestroyMechanism _destroyMechanism;
    private final Random _random = new Random();
    private final ShotStatus _lastShotStatus;
    private boolean _isDestroying = false;


    public ComputerShootingManager(ShootingMap shootingMap, ShotStatus lastShotStatus) {
        this._shootingMap = shootingMap;
        this._cellsLeftToShoot = new ShootingMap(_shootingMap);
        this._lastShotStatus = lastShotStatus;
        this._destroyMechanism = new DestroyMechanism(_cellsLeftToShoot, _shootingMap, this._lastShotStatus);
    }

    public void setIsDestroying(boolean isDestroying) {
        this._isDestroying = isDestroying;
    }

    public CellCoordinates shoot() {
        if(!_isDestroying) {
            return ShootRandomCell();
        }
        else {
            return _destroyMechanism.destroy();
        }
    }

    private CellCoordinates ShootRandomCell() {
        if(_lastShotStatus.getStatus() == ShotStatuses.SHOTNDESTORYED) {
            _cellsLeftToShoot.removeCellsAround(_lastShotStatus.getShootCoordinate());
        }
        var cellCoordinates = GetRandomTargetCoordinates();
        RemoveFromShootable(cellCoordinates);
        _shootingMap.getCellAt(cellCoordinates).setShot(true);
        return cellCoordinates;
    }

    private CellCoordinates GetRandomTargetCoordinates() {

        int index = _random.nextInt(_cellsLeftToShoot.get_cells().size());
        return _cellsLeftToShoot.getCellAtIndex(index).getCoordinates();
    }

    private void RemoveFromShootable(CellCoordinates coordinates) {
        _cellsLeftToShoot.removeCellAt(coordinates);
    }

    public void ResetDestroyer() {
        _isDestroying = false;
        _destroyMechanism.removeNeighborsOfShipToBeDestroyed();
        _destroyMechanism = new DestroyMechanism(_cellsLeftToShoot, _shootingMap, _lastShotStatus);
    }
}