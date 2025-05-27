package DTOs;

import java.io.Serializable;

/**
 * Data transfer object representing the end of a game.
 * Contains information about the game result, total game time, and the number of moves made by the player.
 */
public class GameEndDto implements Serializable {
    private final boolean _playerWon;
    private final long _gameTimeInSeconds;
    private final int _movesDoneByYou;
    /**
     * Constructs a new GameEndDto.
     *
     * @param playerWon         true if the player won the game, false otherwise
     * @param gameTimeInSeconds total game time in seconds
     * @param movesDoneByYou    number of moves made by the player
     */
    public GameEndDto(boolean playerWon, long gameTimeInSeconds, int movesDoneByYou) {
        this._playerWon = playerWon;
        this._gameTimeInSeconds = gameTimeInSeconds;
        this._movesDoneByYou = movesDoneByYou;
    }
    /**
     * Returns whether the player won the game.
     *
     * @return true if the player won, false otherwise
     */
    public boolean isPlayerWon() {
        return _playerWon;
    }
    /**
     * Returns the total game time in seconds.
     *
     * @return game time in seconds
     */
    public long getGameTimeInSeconds() {
        return _gameTimeInSeconds;
    }
    /**
     * Returns the number of moves made by the player.
     *
     * @return number of moves
     */
    public int getMovesDoneByYou() {
        return _movesDoneByYou;
    }
}