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
   * Hides error message on the screen..
   * @param message error message
   */
  public void hideErrorMessage();

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

}
