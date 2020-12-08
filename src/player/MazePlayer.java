package player;

import graph.MazePoint;

/**
 * Class that represents a player in a maze.
 */
public class MazePlayer {

  private final MazePoint startCoordinates;

  private MazePoint currentCoordinates;

  private final int playerIndex;

  private final int totalArrowCount;

  private int currentArrowCount;

  private boolean isPlayerAlive;

  /**
   * Creates a player in a maze.
   * @param startX start x-coordinate
   * @param startY start y-coordinate
   * @param playerIndex used to indicate the player out of list of players playing the game
   * @throws IllegalArgumentException thrown when negative start co-ordinates are used
   */
  public MazePlayer(int startX, int startY, int arrowCount, int playerIndex) throws
          IllegalArgumentException {
    if (startX < 0 || startY < 0) {
      throw new IllegalArgumentException("Negative start coordinates can't be used");
    }
    if (arrowCount <= 0) {
      throw new IllegalArgumentException("Arrow count should be greater than zero");
    }
    if (playerIndex < 0) {
      throw new IllegalArgumentException("Player index can't be negative");
    }
    this.startCoordinates = new MazePoint(startX, startY);
    this.currentCoordinates = new MazePoint(startX, startY);
    this.totalArrowCount = arrowCount;
    this.currentArrowCount = 0;
    this.isPlayerAlive = true;
    this.playerIndex = playerIndex;
  }

  /**
   * Returns the integer used to uniquely identify a player.
   * @return the unique player index
   */
  public int getPlayerIndex() {
    return playerIndex;
  }

  /**
   * Marks the player as dead. No action can be performed on dead player
   */
  public void markPlayerDead() {
    this.isPlayerAlive = false;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Position: ");
    sb.append(startCoordinates.toString());
    return sb.toString();
  }

  /**
   * Returns the start & end co-ordinates of a player.
   * @return starting x & y co-ordinates
   */
  public MazePoint getCurrentCoordinates() {
    return currentCoordinates;
  }

  /**
   * Reduces the number of arrows a player has. If the remaining arrows become zero, then the
   * player is killed.
   * @throws UnsupportedOperationException thrown when the function is called after player is
   *          killed.
   * @throws PlayerKilledException thrown when the remaining arrows become zero.
   */
  public void reduceArrowCount() throws UnsupportedOperationException, PlayerKilledException {
    if (!isPlayerAlive()) {
      throw new UnsupportedOperationException("Trying to reduce arrows when player is killed");
    }
    this.currentArrowCount++;
    if (this.currentArrowCount >= totalArrowCount) {
      markPlayerDead();
      throw new PlayerKilledException("Player is killed since arrows have expired");
    }
  }

  /**
   * Checks if the player is alive or not.
   * @return true if the player is alive, false otherwise
   */
  public boolean isPlayerAlive() {
    return isPlayerAlive;
  }

  /**
   * Sets new co-ordinates when player moves from the current position.
   * @param point new position of the player
   */
  public void setNewPosition(MazePoint point) throws UnsupportedOperationException {
    if (!isPlayerAlive) {
      throw new UnsupportedOperationException("Trying to set position of a dead player");
    }
    this.currentCoordinates = point;
  }
}
