package player;

/**
 * Exception that is thrown when a player is killed by a wumpus, a pit or when his arrows expire.
 */
public class PlayerKilledException extends Exception {

  /**
   * Used to initialize exception with a message when player is killed.
   * @param message message when player is killed
   */
  public PlayerKilledException(String message) {
    super(message);
  }
}
