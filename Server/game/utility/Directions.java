package Server.game.utility;

import java.io.Serializable;
import java.util.Random;

public enum Directions implements Serializable {
    UP("↑ GÓRA"),
    RIGHT("→ PRAWO"),
    DOWN("↓ DÓŁ"),
    LEFT("← LEWO");
    private final String label;
    private static final long serialVersionUID = 1L;
    private static final Random random = new Random();

    Directions(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Directions getRandom() {
        Directions[] directions = values();
        return directions[random.nextInt(directions.length)];
    }
}