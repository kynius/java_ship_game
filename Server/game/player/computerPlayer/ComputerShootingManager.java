package game.player.computerPlayer;

import game.cell.CellCoordinates;
import game.cell.ShootingCell;
import game.map.ShootingMap;
import game.utility.ShotStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerShootingManager {
    private final ShootingMap _shootingMap;
    private final List<ShootingCell> _cellsLeftToShoot;
    private DestroyMechanism _destroyMechanism;
    private final Random _random = new Random();
    private final ShotStatus _lastShotStatus;
    private boolean _isDestroying = false;


    public ComputerShootingManager(ShootingMap shootingMap, ShotStatus lastShotStatus) {
        this._shootingMap = shootingMap;
        this._cellsLeftToShoot = new ArrayList<>(shootingMap.get_cells());
        this._destroyMechanism = new DestroyMechanism(_cellsLeftToShoot, _shootingMap, lastShotStatus);
        this._lastShotStatus = lastShotStatus;
    }

    public void setIsDestroying(boolean isDestroying) {
        this._isDestroying = isDestroying;
    }

    public CellCoordinates shoot() {
        if(_isDestroying) {
            return ShootRandomCell();
        }
        else {
            return _destroyMechanism.destroy();
        }
    }

    private CellCoordinates ShootRandomCell() {
        var cellCoordinates = GetRandomTargetCoordinates();
        RemoveFromShootable(cellCoordinates);
        _shootingMap.getCellAt(cellCoordinates).setShot(true);
        return cellCoordinates;
    }

    private CellCoordinates GetRandomTargetCoordinates() {
        if (_cellsLeftToShoot.isEmpty()) return null;

        int index = _random.nextInt(_cellsLeftToShoot.size());
        return _cellsLeftToShoot.get(index).getCoordinates();
    }

    private void RemoveFromShootable(CellCoordinates coordinates) {
        _cellsLeftToShoot.removeIf(cell -> cell.getCoordinates().equals(coordinates));
    }

    private void ResetDestroyer() {
        _destroyMechanism = new DestroyMechanism(_cellsLeftToShoot, _shootingMap, _lastShotStatus);
    }
}