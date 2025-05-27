package Server.game;

import DTOs.ScoreDto;
import DTOs.ScoreboardDto;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Handles storing and retrieving player scores using binary serialization.
 * Scores are stored in a file and sorted based on game result, move count, and time.
 */
public class ScoreboardHandler {

    private static final String SCOREBOARD_FILE = "scoreboard.dat";

    /**
     * Adds a new score to the scoreboard and saves it to the file.
     *
     * @param score the new score to be added
     */
    public static void addScore(ScoreDto score) {
        ScoreboardDto dto = readScoreboard();
        ArrayList<ScoreDto> scoreboard = dto.getScores();
        scoreboard.add(score);
        writeScoreboard(scoreboard);
        System.exit(0);
    }

    /**
     * Returns a sorted scoreboard wrapped in ScoreboardDto.
     * Sorting priority:
     * 1. Player won (true first)
     * 2. Fewer moves
     * 3. Shorter time
     *
     * @return ScoreboardDto with sorted scores
     */
    public static ScoreboardDto getSortedScoreboard() {
        ScoreboardDto dto = readScoreboard();
        ArrayList<ScoreDto> scoreboard = dto.getScores();
        scoreboard.sort(Comparator
                .comparing(ScoreDto::isPlayerWon).reversed()
                .thenComparingInt(ScoreDto::getMovesDoneByYou)
                .thenComparingLong(ScoreDto::getGameTimeInSeconds));
        return new ScoreboardDto(scoreboard);
    }

    /**
     * Reads the scoreboard from a binary file.
     *
     * @return ScoreboardDto with scores loaded from file
     */
    private static ScoreboardDto readScoreboard() {
        File file = new File(SCOREBOARD_FILE);
        if (!file.exists()) {
            return new ScoreboardDto(new ArrayList<>());
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            ArrayList<ScoreDto> scores = (ArrayList<ScoreDto>) ois.readObject();
            return new ScoreboardDto(scores);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ScoreboardDto(new ArrayList<>());
        }
    }

    /**
     * Writes the scoreboard to a binary file.
     *
     * @param scoreboard list of scores to be saved
     */
    private static void writeScoreboard(ArrayList<ScoreDto> scoreboard) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCOREBOARD_FILE))) {
            oos.writeObject(scoreboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
