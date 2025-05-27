package Server.game.utility;

import java.io.Serializable;
import java.util.Random;

/**
 * Represents the four cardinal directions used in the game.
 * Implements {@link Serializable} for use in networked or persistent game logic.
 */
public enum Directions implements Serializable {

    /**
     * Up direction (↑ GÓRA).
     */
    UP("↑ GÓRA"),

    /**
     * Right direction (→ PRAWO).
     */
    RIGHT("→ PRAWO"),

    /**
     * Down direction (↓ DÓŁ).
     */
    DOWN("↓ DÓŁ"),

    /**
     * Left direction (← LEWO).
     */
    LEFT("← LEWO");

    private final String label;
    private static final long serialVersionUID = 1L;
    private static final Random random = new Random();

    /**
     * Constructs a direction with a given label.
     *
     * @param label a human-readable label  representing the direction
     */
    Directions(String label) {
        this.label = label;
    }

    /**
     * Returns the label associated with this direction.
     *
     * @return the direction label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns a randomly selected direction.
     *
     * @return a random {@code Directions} value
     */
    public static Directions getRandom() {
        Directions[] directions = values();
        return directions[random.nextInt(directions.length)];
    }
}
