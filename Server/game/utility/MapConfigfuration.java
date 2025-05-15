package Server.game.utility;

public class MapConfigfuration {
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