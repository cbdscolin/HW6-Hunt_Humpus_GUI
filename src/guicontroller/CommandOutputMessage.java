package guicontroller;

/**
 * Class that is used to capture the output of the command executed by the controller.
 */
public class CommandOutputMessage {

  private final boolean isSuccess;

  private final String statusMessage;

  private final boolean playerKilled;

  private final boolean wumpusKilled;

  /**
   * Constructor to initialize the fields of this class.
   * @param isSuccess result of the action.
   * @param statusMessage status message of this action.
   * @param playerKilled true if player is killed during this action.
   * @param wumpusKilled true if wumpus is killed during this action.
   */
  public CommandOutputMessage(boolean isSuccess, String statusMessage, boolean playerKilled,
                              boolean wumpusKilled) {
    this.isSuccess = isSuccess;
    this.statusMessage = statusMessage;
    this.playerKilled = playerKilled;
    this.wumpusKilled = wumpusKilled;
  }

  /**
   * Returns the status message of the action command.
   * @return status message
   */
  public String getStatusMessage() {
    return statusMessage;
  }

  /**
   * Returns true if player is killed during the action.
   * @return true if player is killed during the action.
   */
  public boolean isPlayerKilled() {
    return playerKilled;
  }

  /**
   * Returns true if wumpus is killed during the action.
   * @return true if wumpus is killed during the action.
   */
  public boolean isWumpusKilled() {
    return wumpusKilled;
  }

  /**
   * Returns true if the action was successful. If not then player get another turn.
   * @return true if the action was successful
   */
  public boolean isSuccess() {
    return isSuccess;
  }
}
