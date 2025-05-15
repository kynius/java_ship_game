package Server.game.player;

import Server.game.cell.CellCoordinates;
import Server.game.map.ShootingMap;
import Server.game.map.ShipsMap;
import Server.game.utility.ShipsConfiguration;
import Server.game.utility.ShotStatus;
import Server.game.utility.ShotStatuses;

public abstract class Player {
    protected ShootingMap _shootingMap;
    protected ShipsMap _shipsMap;
    protected ShipsConfiguration _shipsConfiguration;
    protected ShipPlacingManager _shipPlacingManager;
    private int _shipsLeftInGame;


    public Player(int mapSize, ShipsConfiguration shipsConfiguration) {
        this._shootingMap = new ShootingMap(mapSize);
        this._shipsMap = new ShipsMap(mapSize);
        this._shipsConfiguration = shipsConfiguration;
        this._shipPlacingManager = new ShipPlacingManager(this._shipsMap);
        this._shipsLeftInGame = _shipsConfiguration.countAllShips();
    }

    public abstract void makeShoot(CellCoordinates coordinates);

    public abstract void placeShips();

    public abstract void getShotInformationReturn(ShotStatus shotStatus);

    public  ShotStatus takeShot(CellCoordinates coordinates) {
        if(_shipsMap.hasShipAt(coordinates)) {
           var shootCell = _shipsMap.getCellAt(coordinates);
           shootCell.setHit(true);
           if(_shipsMap.hasUnhitCellOfShip(shootCell.getShipId())) {
               return new ShotStatus(ShotStatuses.SHOT, coordinates);
           } else {
               _shipsLeftInGame --;
               return new ShotStatus(ShotStatuses.SHOTNDESTORYED, coordinates);
           }
        } else {
            return new ShotStatus(ShotStatuses.MISSED, coordinates);
        }
    }

    public int getShipsLeftInGane() {
        return _shipsLeftInGame;
    }
}