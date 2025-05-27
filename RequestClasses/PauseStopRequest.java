package RequestClasses;

import java.io.Serializable;
/**
 * Request object sent by the client to indicate the end of a pause.
 * Used for resuming the game session after a pause.
 */
public class PauseStopRequest implements Serializable {
    /**
     * Constructs a new PauseStopRequest.
     */
    public PauseStopRequest(){};
}
