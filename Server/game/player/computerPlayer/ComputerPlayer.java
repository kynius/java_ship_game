package game.player.computerPlayer;

import game.cell.CellCoordinates;
import game.player.Player;
import game.utility.Directions;
import game.utility.ShipsConfiguration;
import game.utility.ShotStatus;
import game.utility.ShotStatuses;

import java.util.Random;

public class ComputerPlayer extends Player {

    private final Random _random = new Random();
    private final ComputerShootingManager _computerShootingManager;
    private  ShotStatus _lastShotStatus = null;

    public ComputerPlayer(int mapSize, ShipsConfiguration shipsConfiguration) {
        super(mapSize, shipsConfiguration);
        _computerShootingManager = new ComputerShootingManager(_shootingMap, _lastShotStatus);
    }

    @Override
    public void makeShoot(CellCoordinates coordinates) {
        _computerShootingManager.shoot();
    }

    //loops over ships configuration
    @Override
    public void placeShips() {
        int[] shipsPerLength = _shipsConfiguration.getShipAmounts();
        int shipId = 1;

        for (int i = 0; i < shipsPerLength.length; i++) {
            int shipLength = shipsPerLength.length - i;
            int shipsToPlace = shipsPerLength[i];

            for (int j = 0; j < shipsToPlace; j++) {
                boolean placed = false;

                while (!placed) {
                    int x = _random.nextInt(_shipsMap.getSize());
                    int y = _random.nextInt(_shipsMap.getSize());
                    Directions direction = Directions.getRandom();

                    CellCoordinates coords = new CellCoordinates(x, y);
                    placed = _shipPlacingManager.placeShip(shipLength, coords, direction, shipId);
                }
                shipId++;
            }
        }
    }

    @Override
    public void getShotInformationReturn(ShotStatus shotStatus) {
        _lastShotStatus = shotStatus;
        var cellToMark = this._shootingMap.getCellAt(shotStatus.getShootCoordinate());
        if(shotStatus.getStatus() == ShotStatuses.MISSED) {
            cellToMark.setShot(true);
        } else {
            cellToMark.setShot(true);
            cellToMark.setIsAimed(true);

            if(shotStatus.getStatus() == ShotStatuses.SHOT){
                _computerShootingManager.setIsDestroying(true);
            } else if(shotStatus.getStatus() == ShotStatuses.SHOTNDESTORYED) {
                _computerShootingManager.setIsDestroying(false);
            }
        }

    }
}
