package Server.game.player;

import Server.game.cell.CellCoordinates;
import Server.game.map.ShootingMap;
import Server.game.map.ShipsMap;
import Server.game.utility.ShipsConfiguration;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;

import java.util.concurrent.CompletableFuture;

/**
 * Represents a player in the game.
 * Can be extended by human or AI implementations.
 * Manages the player's own ship map, shooting map, ship configuration, and game state.
 */
public abstract class Player {

    protected ShootingMap _shootingMap;
    protected ShipsMap _shipsMap;
    protected ShipsConfiguration _shipsConfiguration;
    protected ShipPlacingManager _shipPlacingManager;
    private int _shipsLeftInGame;
    protected int _shotsMade = 0;

    /**
     * Default constructor.
     */
    public Player() {
    }

    /**
     * Initializes a player with a specified map size and ship configuration.
     *
     * @param mapSize the size of the game map
     * @param shipsConfiguration the configuration of ships for this player
     */
    public Player(int mapSize, ShipsConfiguration shipsConfiguration) {
        this._shootingMap = new ShootingMap(mapSize);
        this._shipsMap = new ShipsMap(mapSize);
        this._shipsConfiguration = shipsConfiguration;
        this._shipPlacingManager = new ShipPlacingManager(this._shipsMap);
        this._shipsLeftInGame = _shipsConfiguration.countAllShips();
    }

    /**
     * Returns the number of shots made by the player.
     *
     * @return number of shots made
     */
    public int getShotsMade() {
        return _shotsMade;
    }

    /**
     * Called to initiate the player's shooting action.
     *
     * @return a future resolving to the coordinates chosen for the shot
     */
    public abstract CompletableFuture<CellCoordinates> makeShoot();

    /**
     * Called to start ship placement logic.
     *
     * @return a future that completes when the player finishes placing ships
     */
    public abstract CompletableFuture<Void> placeShips();

    /**
     * Called to inform the player of the result of a shot they made.
     *
     * @param shotStatus the result of the shot
     */
    public abstract void getShotInformationReturn(ShotStatus shotStatus);

    /**
     * Processes a shot made against this player at the given coordinates.
     * Updates ship state and returns the result.
     *
     * @param coordinates the coordinates being shot at
     * @return the result of the shot
     */
    public ShotStatus takeShot(CellCoordinates coordinates) {
        var shootCell = _shipsMap.getCellAt(coordinates);
        shootCell.setHit(true);
        if (_shipsMap.hasShipAt(coordinates)) {
            if (_shipsMap.hasUnhitCellOfShip(shootCell.getShipId())) {
                return new ShotStatus(ShotStatuses.SHOT, coordinates);
            } else {
                _shipsLeftInGame--;
                return new ShotStatus(ShotStatuses.SHOTNDESTORYED, coordinates);
            }
        } else {
            return new ShotStatus(ShotStatuses.MISSED, coordinates);
        }
    }

    /**
     * Returns the number of ships the player still has in the game.
     *
     * @return number of remaining ships
     */
    public int getShipsLeftInGame() {
        return _shipsLeftInGame;
    }
}
