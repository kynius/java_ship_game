package Server.game.player.computerPlayer;

import Server.game.cell.CellCoordinates;
import Server.game.player.Player;
import Server.game.utility.Directions;
import Server.game.utility.ShipsConfiguration;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a computer-controlled player in the game.
 * Handles automatic ship placement and shooting logic using {@link ComputerShootingManager}.
 */
public class ComputerPlayer extends Player {

    private final Random random = new Random();
    private final ComputerShootingManager computerShootingManager;
    private ShotStatus lastShotStatus = new ShotStatus();

    /**
     * Constructs a computer player with the specified map size and ship configuration.
     *
     * @param mapSize the size of the game map
     * @param shipsConfiguration the ship configuration for this player
     */
    public ComputerPlayer(int mapSize, ShipsConfiguration shipsConfiguration) {
        super(mapSize, shipsConfiguration);
        computerShootingManager = new ComputerShootingManager(_shootingMap, lastShotStatus);
    }

    /**
     * Automatically determines and returns the next cell to shoot at.
     *
     * @return a completed future containing the coordinates to shoot
     */
    @Override
    public CompletableFuture<CellCoordinates> makeShoot() {
        _shotsMade++;
        CellCoordinates coords = computerShootingManager.shoot();
        return CompletableFuture.completedFuture(coords);
    }

    /**
     * Automatically places all ships randomly on the map.
     *
     * @return a completed future indicating the placement is done
     */
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
                    int x = random.nextInt(_shipsMap.getSize()) + 1;
                    int y = random.nextInt(_shipsMap.getSize()) + 1;
                    Directions direction = Directions.getRandom();
                    CellCoordinates coords = new CellCoordinates(x, y);
                    placed = _shipPlacingManager.placeShip(shipLength, coords, direction, shipId);
                }

                shipId++;
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Updates the shooting map and internal state based on the result of a recent shot.
     *
     * @param shotStatus the result of the shot made by this player
     */
    @Override
    public void getShotInformationReturn(ShotStatus shotStatus) {
        lastShotStatus.updateFrom(shotStatus);
        var cellToMark = _shootingMap.getCellAt(shotStatus.getShootCoordinate());
        cellToMark.setShot(true);

        if (shotStatus.getStatus() != ShotStatuses.MISSED) {
            cellToMark.setIsAimed(true);

            if (shotStatus.getStatus() == ShotStatuses.SHOT) {
                computerShootingManager.setIsDestroying(true);
            } else if (shotStatus.getStatus() == ShotStatuses.SHOTNDESTORYED) {
                computerShootingManager.setIsDestroying(false);
                computerShootingManager.resetDestroyer();
            }
        }
    }
}
