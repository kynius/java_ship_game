package Server.game.player.computerPlayer;

import Server.game.cell.CellCoordinates;
import Server.game.cell.ShootingCell;
import Server.game.map.ShootingMap;
import Server.game.utility.Directions;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Implements logic for destroying a partially hit ship by the computer player.
 * Tracks hit coordinates, analyzes ship orientation, and selects optimal directions to continue shooting.
 */
public class DestroyMechanism {

    private Boolean _isShipDirectionDetermined = false;
    private final List<CellCoordinates> _shipToBeDestroyed;
    private final ShootingMap _cellsLeftToShoot;
    private final ShootingMap _shootingMap;
    private final List<Directions> _directionsLeftToShoot;
    private Directions _lastUsedDriection = null;
    private ShotStatus _lastShotStatus;
    private final Random _random = new Random();

    /**
     * Creates a new instance of the destroy mechanism.
     *
     * @param cellsLeftToShoot the map tracking cells that haven't been shot yet
     * @param shootingMap the map showing the status of previous shots
     * @param shotStatus the result of the most recent shot
     */
    public DestroyMechanism(
            ShootingMap cellsLeftToShoot,
            ShootingMap shootingMap,
            ShotStatus shotStatus) {

        this._shipToBeDestroyed = new ArrayList<>();
        this._cellsLeftToShoot = cellsLeftToShoot;
        this._shootingMap = shootingMap;
        this._directionsLeftToShoot = new ArrayList<>(List.of(
                Directions.LEFT,
                Directions.UP,
                Directions.DOWN,
                Directions.RIGHT
        ));
        this._lastShotStatus = shotStatus;
    }

    /**
     * Executes the destroy mechanism's logic to determine the next coordinates to shoot at.
     *
     * @return the next cell coordinates to shoot
     */
    public CellCoordinates destroy() {
        if (_lastShotStatus.getStatus() == ShotStatuses.SHOT) {
            _shipToBeDestroyed.add(_lastShotStatus.getShootCoordinate());
        } else if (_lastShotStatus.getStatus() == ShotStatuses.MISSED) {
            _directionsLeftToShoot.remove(_lastUsedDriection);
        }

        while (true) {
            if (_shipToBeDestroyed.size() >= 2 && !_isShipDirectionDetermined) {
                determineShipDirection();
            }

            var direction = getRandomDirectionFromLeftDirections();
            var cellToShootCoordinates = getEdgeCellForDirection(direction);

            if (validateCellCoordinatesForShot(cellToShootCoordinates)) {
                _lastUsedDriection = direction;
                _cellsLeftToShoot.removeCellAt(cellToShootCoordinates);
                return cellToShootCoordinates;
            } else {
                _directionsLeftToShoot.remove(direction);
            }
        }
    }

    /**
     * Randomly selects one of the remaining valid directions to try shooting in.
     *
     * @return a random direction
     */
    private Directions getRandomDirectionFromLeftDirections() {
        int index = _random.nextInt(_directionsLeftToShoot.size());
        return _directionsLeftToShoot.get(index);
    }

    /**
     * Checks whether the given coordinates are within bounds and still available for shooting.
     *
     * @param cellCoordinates the coordinates to validate
     * @return true if valid for shooting, false otherwise
     */
    private boolean validateCellCoordinatesForShot(CellCoordinates cellCoordinates) {
        return _shootingMap.areCoordinatesInBounds(cellCoordinates)
                && _cellsLeftToShoot.getCellAt(cellCoordinates) != null;
    }

    /**
     * Analyzes the collected hit cells to determine the ship's direction (horizontal or vertical),
     * and removes irrelevant directions accordingly.
     */
    private void determineShipDirection() {
        if (_shipToBeDestroyed.size() >= 2) {
            boolean allXEqual = _shipToBeDestroyed.stream()
                    .map(CellCoordinates::getX)
                    .distinct()
                    .count() == 1;

            boolean allYEqual = _shipToBeDestroyed.stream()
                    .map(CellCoordinates::getY)
                    .distinct()
                    .count() == 1;

            if (allXEqual) {
                _directionsLeftToShoot.remove(Directions.LEFT);
                _directionsLeftToShoot.remove(Directions.RIGHT);
            } else if (allYEqual) {
                _directionsLeftToShoot.remove(Directions.UP);
                _directionsLeftToShoot.remove(Directions.DOWN);
            }

            _isShipDirectionDetermined = true;
        }
    }

    /**
     * Determines the next edge cell to target based on the given direction and the current list of hit cells.
     *
     * @param direction the direction to extend the attack
     * @return the coordinates of the edge cell in the specified direction
     */
    private CellCoordinates getEdgeCellForDirection(Directions direction) {
        return switch (direction) {
            case UP -> {
                CellCoordinates top = _shipToBeDestroyed.stream()
                        .min(Comparator.comparingInt(CellCoordinates::getY)).get();
                yield new CellCoordinates(top.getX(), top.getY() - 1);
            }
            case DOWN -> {
                CellCoordinates bottom = _shipToBeDestroyed.stream()
                        .max(Comparator.comparingInt(CellCoordinates::getY)).get();
                yield new CellCoordinates(bottom.getX(), bottom.getY() + 1);
            }
            case LEFT -> {
                CellCoordinates left = _shipToBeDestroyed.stream()
                        .min(Comparator.comparingInt(CellCoordinates::getX)).get();
                yield new CellCoordinates(left.getX() - 1, left.getY());
            }
            case RIGHT -> {
                CellCoordinates right = _shipToBeDestroyed.stream()
                        .max(Comparator.comparingInt(CellCoordinates::getX)).get();
                yield new CellCoordinates(right.getX() + 1, right.getY());
            }
        };
    }

    /**
     * Removes all cells surrounding the currently identified ship from the list of available targets.
     * Called when the ship is confirmed destroyed.
     */
    public void removeNeighborsOfShipToBeDestroyed() {
        for (CellCoordinates coordinate : _shipToBeDestroyed) {
            _cellsLeftToShoot.removeCellsAround(coordinate);
        }
    }
}
