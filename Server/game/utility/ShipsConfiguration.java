package Server.game.utility;

import java.io.Serializable;

/**
 * Represents the configuration of ships used in the game.
 * Each index in the array corresponds to the number of ships of a given size,
 * ordered from longest to shortest.
 * <p>
 * For example: index 0 = number of 4-cell ships, index 1 = number of 3-cell ships, etc.
 * </p>
 * <p>
 * This class is immutable and serializable.
 * </p>
 */
public class ShipsConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int[] shipAmounts;

    /**
     * Creates a ship configuration with variable ship lengths.
     * The first element is the number of ships with the longest size.
     *
     * @param shipAmounts array of ship counts ordered from longest to shortest ship size
     */
    public ShipsConfiguration(int[] shipAmounts) {
        this.shipAmounts = shipAmounts.clone(); // defensive copy
    }

    /**
     * Returns the ship configuration array.
     * Each index represents a ship size, ordered from longest to shortest.
     *
     * @return array of ship counts
     */
    public int[] getShipAmounts() {
        return shipAmounts.clone(); // return a copy to preserve immutability
    }

    /**
     * Returns the total number of ships.
     *
     * @return total number of ships
     */
    public int countAllShips() {
        int sum = 0;
        for (int amount : shipAmounts) {
            sum += amount;
        }
        return sum;
    }
}
