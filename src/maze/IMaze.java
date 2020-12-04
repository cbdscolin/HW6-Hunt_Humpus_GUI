package maze;


import java.util.List;
import java.util.Map;
import java.util.Random;

import graph.MazePoint;
import mazecreatures.CreatureType;
import mazeexceptions.RecoverableException;
import player.PlayerKilledException;
import player.PlayerKillsWumpusException;

/**
 * Interface that offers functionality related to creation of various kinds of mazes such as
 * perfect mazes, wrapping mazes, non-wrapping mazes and movement of players inside the maze and
 * also hands them treasures such as gold and removes treasures from the players based on the cell
 * they visit.
 */
public interface IMaze {

  /**
   * Function returns the number of maze for perfect and non-perfect wrapping or non-wrapping
   * mazes.
   *
   * @return
   */
  int minimumWallsToRemove();

  /**
   * Function returns if the current maze is a wrapping maze or a non-wrapping maze.
   *
   * @return true if the maze is wrapping, false otherwise
   */
  boolean isWrappingMaze();

  /**
   * Function used to generate a perfect maze by keeping only those edges that are necessary to make
   * the maze perfect. This also removes extra internal walls and border walls and  also
   * adds creatures such as wumpus, bat, player and pits to the maze.
   *
   * @param percentages the key-value pair which is used to place bats and pits in cells. Ex:
   *                    [Bat] => 40 means 40% non-tunnel cells have a bat in them.
   * @param totalPlayerArrows the number of arrows a player has.
   * @throws PlayerKilledException thrown when player is killed because he ended up in a cell
   *          that has a wumpus
   * @throws UnsupportedOperationException when this is called more than once
   */
  void removeWalls(Map<CreatureType, Integer> percentages, int totalPlayerArrows)
          throws UnsupportedOperationException,
          IllegalArgumentException, PlayerKilledException;

  /**
   * Moves in the given direction - North, East, West, South. If the movement is invalid various
   * exceptions are thrown.
   *
   * @param direction direction to be moved
   * @throws IllegalArgumentException when direction specified is invalid
   * @throws IllegalStateException    when trying to move to invalid location such as out of
   *                                  boundary or negative index
   */
  void movePlayerInDirection(Direction direction) throws IllegalArgumentException,
          IllegalStateException, PlayerKilledException, RecoverableException;

  /**
   * Function creates a list of walls for this maze. The walls are removed from this list to create
   * a perfect maze and to transform perfect maze to wrapping and non-wrapping room maze.
   */
  void initWallsForMaze();


  /**
   * Prints the maze by displaying all the cells.
   *
   * @return the view of the maze
   */
  String printMaze();

  /**
   * Function returns the number of rows present in the maze.
   *
   * @return number of rows in maze
   */
  int getTotalRows();

  /**
   * Function returns the number of columns present in the maze.
   *
   * @return number of columns in maze
   */
  int getTotalColumns();

  /**
   * Function returns a list of cells that are not tunnels. These cells can be used to place a
   * player, bat, pit or Wumpus.
   *
   * @return
   */
  List<Cell> getNonTunnelCells();

  /**
   * Function returns the generator that is used to determine where a player is dropped after a bat
   * picks it up. The same generator is used to check if the bat picks the player and drops it
   * randomly to another cell.
   *
   * @return random number generator
   */
  Random getMovementGenerator();

  /**
   * Function checks if the creature type passed in the argument is present in the current cell. If
   * the current cell is a tunnel, the function recursively searches for the last cell that is not a
   * tunnel.
   *
   * @param point        co-ordinates of the current cell.
   * @param creatureType the type of creature to be searched for
   * @param exclude      used to prevent infinite calls in tunnels.
   * @return true if the creature is present in the adjacent cells.
   */
  boolean checkCreatureInAdjacentCells(MazePoint point, CreatureType creatureType,
                                              Direction exclude);


  /**
   * Function returns true if internal walls & border walls that have to be removed are actually
   * removed when this function is called.
   *
   * @return true if the relevant walls are removed
   */
  boolean allRequestedWallsRemoved();

  /**
   * Shoots an arrow in a given direction with the specified power. The arrow can kill wumpus
   * if the distance from the players position and wumpus matches the power.
   * @param dir the direction to be shot in
   * @param power the power of the arrow
   * @return true if the wumpus was killed
   * @throws PlayerKillsWumpusException thrown when wumpus is killed
   * @throws PlayerKilledException thrown when player is killed if arrows expire
   * @throws RecoverableException if the arrow is shot in a wrong direction of the wall
   */
  boolean shootArrow(MazePoint point, Direction dir, int power) throws
          PlayerKillsWumpusException, RecoverableException, PlayerKilledException;

  /**
   * Returns the current position of the player in a maze.
   * @return the current position of the player in a maze.
   */
  MazePoint getPlayerCoordinates();

  /**
   * Returns a list of directions (N, E, W, S) a player can move from the current cell.
   * @return list of directions a player can move from the current cell
   */
  List<Direction> getValidDirectionsForMovement();

  /**
   * Checks if a cell has a given creature such as pit, bat and wumpus.
   * @param point the current cell from which to check
   * @param dir the direction to check
   * @param creatureType the creature to be checked
   * @param distance the distance from the current start point where a creature might occur.
   * @return true if the creature is found at a given distance.
   */
  boolean resultingCellHasCreature(MazePoint point, Direction dir,
                                   CreatureType creatureType, int distance);

  /**
   * Used to check the expected end point where a player stops moving from the current position.
   * This is used to check if the player was moved by the bats from that final position. This
   * method disregards the pits and wumpus and is only used to check the final position a player
   * may end up in after walking.
   * @param point the start point
   * @param dir direction to start walking in
   * @return  the end point where a player stops.
   */
  MazePoint getExpectedMovementPosition(MazePoint point, Direction dir);

}
