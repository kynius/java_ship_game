package RequestClasses;

import java.io.Serializable;

/**
 * Request object sent by the client to retrieve the current scoreboard.
 * Used to request the list of player scores from the server.
 */
public class ScoreboardRequest implements Serializable {
    /**
     * Constructs a new ScoreboardRequest.
     */
    public ScoreboardRequest(){};
}
