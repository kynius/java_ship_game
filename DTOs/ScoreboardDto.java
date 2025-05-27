package DTOs;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoreboardDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<ScoreDto> scores;
    public ScoreboardDto(ArrayList<ScoreDto> scores) {
        this.scores = scores;
    }
    public ArrayList<ScoreDto> getScores() {
        return scores;
    }
    public void setScores(ArrayList<ScoreDto> scores) {
        this.scores = scores;
    }
}
