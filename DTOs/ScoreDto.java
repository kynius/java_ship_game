package DTOs;

import java.io.Serializable;

public class ScoreDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String playerName;
    private final boolean playerWon;
    private final long gameTimeInSeconds;
    private final int movesDoneByYou;

    public ScoreDto(String playerName, boolean playerWon, long gameTimeInSeconds, int movesDoneByYou) {
        this.playerName = playerName;
        this.playerWon = playerWon;
        this.gameTimeInSeconds = gameTimeInSeconds;
        this.movesDoneByYou = movesDoneByYou;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public long getGameTimeInSeconds() {
        return gameTimeInSeconds;
    }

    public int getMovesDoneByYou() {
        return movesDoneByYou;
    }
}

