package Server.game.utility;

import java.io.Serializable;
import java.util.Random;

public enum Directions implements Serializable {
    UP,
    DOWN,
    LEFT,
    RIGHT;
    private static final long serialVersionUID = 1L;
    private static final Random random = new Random();

    public static Directions getRandom() {
        Directions[] directions = values();
        return directions[random.nextInt(directions.length)];
    }
}