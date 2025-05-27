package DTOs;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Data transfer object representing the scoreboard.
 * Contains a list of player scores to be sent between the server and client.
 */
public class ScoreboardDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<ScoreDto> scores;
    /**
     * Constructs a new ScoreboardDto with the given list of scores.
     *
     * @param scores the list of player scores
     */
    public ScoreboardDto(ArrayList<ScoreDto> scores) {
        this.scores = scores;
    }
    /**
     * Returns the list of player scores.
     *
     * @return the list of scores
     */
    public ArrayList<ScoreDto> getScores() {
        return scores;
    }
    /**
     * Sets the list of player scores.
     *
     * @param scores the new list of scores
     */
    public void setScores(ArrayList<ScoreDto> scores) {
        this.scores = scores;
    }
}
