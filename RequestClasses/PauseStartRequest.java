package RequestClasses;

import java.io.Serializable;
/**
 * Request object sent by the client to indicate the start of a pause.
 * Used for pausing the game session.
 */
public class PauseStartRequest implements Serializable {
    /**
     * Constructs a new PauseStartRequest.
     */
    public PauseStartRequest(){};
}
