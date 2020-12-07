package view;
/**
 * Interface that outlines the functionality of View in MVC design of Hunt the Wumpus console
 * game.
 */
public interface IView {

  /**
   * Shows the screen where player can enter inputs to create a new maze. The input screen
   * has a way to obtain maze rows, columns, percentage of pits, bats in the maze, number of
   * players and number of arrows available per player.
   */
  void showInputScreen();

  /**
   * Hides the screen where player can enter inputs to create a new maze. The input screen
   * has a way to obtain maze rows, columns, percentage of pits, bats in the maze, number of
   * players and number of arrows available per player.
   */
  void hideInputScreen();

  /**
   * Displays error message on the screen when invalid inputs are sent to the view.
   * @param message error message
   */
  void showErrorMessage(String message);

  /**
   * Displays a message and ends the game. The game can end when either wumpus is killed or
   * the player is killed.
   * @param message message showing the winner and the loser of the game
   */
  void endGameWithMessage(String message);

  /**
   * Displays the maze. Each cell can be a tunnel/non-tunnel containing bat, wumpus, player,
   * pit or smell pit or wumpus.
   */
  public void showMaze();

  /**
   * Sends cell images to be displayed by the view.
   * @param images images to be displayed by the view
   */
  void sendCellImagesToView(String[][] images);

  /**
   * Displays the current status of the game. The status can be active if the game is playable
   * or inactive a player has won.
   * @param message game status message
   */
  void showGameStatusMessage(String message);

  /**
   * Displays the valid direction of movement or shooting for the player whose turn is next.
   * @param message message consisting of directions of movement.
   */
  void showValidDirectionsMessage(String message);

  /**
   * Displays the index of the player who has the chance to move or shoot next.
   * @param message index of player whose turn is next
   */
  void showPlayerTurnMessage(String message);

}
