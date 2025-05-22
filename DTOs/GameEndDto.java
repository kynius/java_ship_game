package DTOs;

import java.io.Serializable;

public class GameEndDto implements Serializable {
    private final boolean _playerWon;
    private final long _gameTimeInSeconds;
    private final int _movesDoneByYou;

    public GameEndDto(boolean playerWon, long gameTimeInSeconds, int movesDoneByYou) {
        this._playerWon = playerWon;
        this._gameTimeInSeconds = gameTimeInSeconds;
        this._movesDoneByYou = movesDoneByYou;
    }

    public boolean isPlayerWon() {
        return _playerWon;
    }

    public long getGameTimeInSeconds() {
        return _gameTimeInSeconds;
    }

    public int getMovesDoneByYou() {
        return _movesDoneByYou;
    }
}