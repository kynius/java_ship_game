package Server.game.utility;

import java.io.Serializable;

public class MapConfigfuration implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int mapSize;
    private final ShipsConfiguration shipsConfiguration;

    public MapConfigfuration(int mapSize, ShipsConfiguration shipsConfiguration) {
        this.mapSize = mapSize;
        this.shipsConfiguration = shipsConfiguration;
    }

    public int getMapSize() {
        return mapSize;
    }

    public ShipsConfiguration getShipsConfiguration() {
        return shipsConfiguration;
    }
}
