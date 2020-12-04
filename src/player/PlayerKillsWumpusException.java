package player;

/**
 * Exception that is thrown when player is killed by Wumpus.
 */
public class PlayerKillsWumpusException extends Exception {

  public PlayerKillsWumpusException(String message) {
    super(message);
  }
}
