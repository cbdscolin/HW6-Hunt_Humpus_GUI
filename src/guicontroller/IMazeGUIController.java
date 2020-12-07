package guicontroller;

import maze.Direction;
import view.IView;

/**
 * Controller interface that implements functionality related to the actions a controller
 * performs when a view that has GUI is used.
 */
public interface IMazeGUIController {

  /**
   * Returns the maximum number of rows allowed for the maze.
   * @return maximum rows
   */
  int getMaximumRows();

  /**
   * Returns the maximum number of columns allowed for the maze.
   * @return maximum columns
   */
  int getMaximumColumns();

  /**
   * Creates a maze using the parameters passed to it.
   * @param rows rows in maze
   * @param columns columns in maze
   * @param internalWalls internal walls to be removed
   * @param externalWalls border walls to make the maze wrapping maze
   * @param playerCount number of players in the game
   * @param pitPercentage percentage of cells that have pits
   * @param batPercentage percentage of cells that have bats
   * @param arrowCount number of arrows
   * @param usePast
   */
  void createMaze(int rows, int columns, int internalWalls, int externalWalls,
                  int playerCount, int pitPercentage, int batPercentage, int arrowCount, boolean usePast);

  /**
   * Sets view for this controller.
   * @param view view in the MVC design pattern
   */
  void setView(IView view);

  /**
   * Moves players in the direction specified. If the direction is invalid, then the action cannot
   * be carried out.
   * @param direction direction in which players to move
   */
  void movePlayerInDirection(Direction direction);

  /**
   * Shoots arrow in the direction specified with the specified arrow power.
   * @param direction direction to shoot
   * @param arrowPower power of shot
   */
  void shootPlayerInDirection(Direction direction, int arrowPower);

}
