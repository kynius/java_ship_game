package Server.game.utility;

import java.util.Random;

public enum Directions {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    private static final Random random = new Random();

    public static Directions getRandom() {
        Directions[] directions = values();
        return directions[random.nextInt(directions.length)];
    }
}