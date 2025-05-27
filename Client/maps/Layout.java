package Client.maps;

/**
 * Represents the different layout states of the game UI.
 * Used to indicate whether the player is placing ships,
 * the computer is shooting, or the player is shooting.
 */
public enum Layout {
    /** The state where the player is placing ships on the map. */
    Placing,
    /** The state where the computer is taking its shot. */
    ComputerShooting,
    /** The state where the player is taking a shot. */
    PlayerShooting,
}
