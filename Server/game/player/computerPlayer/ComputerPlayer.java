package Server.game.player.computerPlayer;

import Server.game.cell.CellCoordinates;
import Server.game.player.Player;
import Server.game.utility.Directions;
import Server.game.utility.ShipsConfiguration;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ComputerPlayer extends Player {

    private final Random _random = new Random();
    private final ComputerShootingManager _computerShootingManager;
    private ShotStatus _lastShotStatus = new ShotStatus();

    public ComputerPlayer(int mapSize, ShipsConfiguration shipsConfiguration) {
        super(mapSize, shipsConfiguration);
        _computerShootingManager = new ComputerShootingManager(_shootingMap, _lastShotStatus);
    }

    @Override
    public CompletableFuture<CellCoordinates> makeShoot() {
        _shotsMade++;
        CellCoordinates coords = _computerShootingManager.shoot();
        return CompletableFuture.completedFuture(coords);
    }

    @Override
    public CompletableFuture<Void> placeShips() {
        int[] shipsPerLength = _shipsConfiguration.getShipAmounts();
        int shipId = 1;

        for (int i = 0; i < shipsPerLength.length; i++) {
            int shipLength = shipsPerLength.length - i;
            int shipsToPlace = shipsPerLength[i];

            for (int j = 0; j < shipsToPlace; j++) {
                boolean placed = false;

                while (!placed) {
                    int x = _random.nextInt(_shipsMap.getSize()) + 1;
                    int y = _random.nextInt(_shipsMap.getSize()) + 1;
                    Directions direction = Directions.getRandom();
                    CellCoordinates coords = new CellCoordinates(x, y);
                    placed = _shipPlacingManager.placeShip(shipLength, coords, direction, shipId);
                }
                shipId++;
            }

        }

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void getShotInformationReturn(ShotStatus shotStatus) {
        _lastShotStatus.updateFrom(shotStatus);
        var cellToMark = this._shootingMap.getCellAt(shotStatus.getShootCoordinate());
        cellToMark.setShot(true);

        if (shotStatus.getStatus() != ShotStatuses.MISSED) {
            cellToMark.setIsAimed(true);

            if (shotStatus.getStatus() == ShotStatuses.SHOT) {
                _computerShootingManager.setIsDestroying(true);
            } else if (shotStatus.getStatus() == ShotStatuses.SHOTNDESTORYED) {
                _computerShootingManager.setIsDestroying(false);
                _computerShootingManager.ResetDestroyer();
            }
        }
    }
}
