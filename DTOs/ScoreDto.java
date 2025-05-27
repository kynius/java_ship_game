package DTOs;

import java.io.Serializable;
/**
 * Data transfer object representing a single player's score entry.
 * Contains the player's name, game result, total game time, and number of moves made.
 */
public class ScoreDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String playerName;
    private final boolean playerWon;
    private final long gameTimeInSeconds;
    private final int movesDoneByYou;
    /**
     * Constructs a new ScoreDto.
     *
     * @param playerName        the name of the player
     * @param playerWon         true if the player won, false otherwise
     * @param gameTimeInSeconds total game time in seconds
     * @param movesDoneByYou    number of moves made by the player
     */
    public ScoreDto(String playerName, boolean playerWon, long gameTimeInSeconds, int movesDoneByYou) {
        this.playerName = playerName;
        this.playerWon = playerWon;
        this.gameTimeInSeconds = gameTimeInSeconds;
        this.movesDoneByYou = movesDoneByYou;
    }
    /**
     * Returns the player's name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }
    /**
     * Returns whether the player won the game.
     *
     * @return true if the player won, false otherwise
     */
    public boolean isPlayerWon() {
        return playerWon;
    }
    /**
     * Returns the total game time in seconds.
     *
     * @return game time in seconds
     */
    public long getGameTimeInSeconds() {
        return gameTimeInSeconds;
    }
    /**
     * Returns the number of moves made by the player.
     *
     * @return number of moves
     */
    public int getMovesDoneByYou() {
        return movesDoneByYou;
    }
}

