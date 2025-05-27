package Server.game.utility;

import java.io.Serializable;

/**
 * Represents the configuration of the game map, including its size and ship layout.
 * Implements {@link Serializable} for potential transmission or storage.
 */
public class MapConfigfuration implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int mapSize;
    private final ShipsConfiguration shipsConfiguration;

    /**
     * Constructs a new map configuration with the specified map size and ship configuration.
     *
     * @param mapSize the size (width and height) of the square game map
     * @param shipsConfiguration the configuration of ships to be placed on the map
     */
    public MapConfigfuration(int mapSize, ShipsConfiguration shipsConfiguration) {
        this.mapSize = mapSize;
        this.shipsConfiguration = shipsConfiguration;
    }

    /**
     * Returns the size of the game map.
     *
     * @return the map size
     */
    public int getMapSize() {
        return mapSize;
    }

    /**
     * Returns the ship configuration used on the map.
     *
     * @return the ship configuration
     */
    public ShipsConfiguration getShipsConfiguration() {
        return shipsConfiguration;
    }
}
