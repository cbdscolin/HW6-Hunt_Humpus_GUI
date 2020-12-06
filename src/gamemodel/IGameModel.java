package gamemodel;

import java.util.List;
import java.util.Map;

import graph.MazePoint;
import maze.Direction;
import maze.IMaze;
import mazecreatures.CreatureType;
import mazeexceptions.RecoverableException;
import player.PlayerKilledException;
import player.PlayerKillsWumpusException;

/**
 * Interface that necessitates the functionality that has to be offered by the model for hunt-
 * the wumpus game.
 */
public interface IGameModel {

  /**
   * The maze used by the player to play the game is set in this function.
   * @param maze maze used for this game
   */
  public void setMaze(IMaze maze);

  /**
   * Function that prints the maze along with the position of bats, pits & wumpus.
   * @return the view of maze containing bats, pits, player & wumpus.
   * @param showBarriers
   */
  public String printMaze(boolean showBarriers);

  /**
   * Function used to generate a perfect maze by keeping only those edges that are necessary to make
   * the maze perfect.
   * @param percentages the key-value pair contatining percentages of bat and pit to be
   *                    used in cells
   * @param totalPlayerArrows the number of arrows a player has initially
   *
   */
  void removeWalls(Map<CreatureType, Integer> percentages, int totalPlayerArrows)
          throws UnsupportedOperationException,
          IllegalArgumentException, PlayerKilledException;

  /**
   * Returns the players current position.
   * @return the players current position.
   */
  MazePoint getActivePlayerCoordinates();

  /**
   * Returns a list of directions (N, E, W, S) a player can move from the current cell.
   * @return list of directions a player can move from the current cell
   */
  List<Direction> getValidDirectionsForMovement();

  /**
   * Used to detect draft and smell. In general can be used to find if a cell has a certain type
   * of creature adjacent to it. Adjacent also means a cell connected to a cell through tunnel
   * cells.
   * @param creatureType the creature type such as bat, pit, wumpus etc.
   * @return true if the creature is adjacent to cell
   */
  boolean checkCreatureInAdjacentCells(CreatureType creatureType);

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
  boolean shootArrow(Direction dir, int power) throws
          PlayerKillsWumpusException, RecoverableException, PlayerKilledException;

  /**
   * Moves player in the given direction. Player keeps on moving till he reaches a cell.
   * @param direction the direction in which player has to move
   * @throws PlayerKilledException thrown when player meets a wumpus or pit and killed
   * @throws RecoverableException thrown when player moves in direction of wall.
   */
  void movePlayerInDirection(Direction direction) throws
          PlayerKilledException, RecoverableException;

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

  /**
   * Returns the integer identifier of the player whose turn is next.
   * @return identifier for player.
   */
  int getActivePlayerIndex();

  /**
   * Returns the images to be displayed at each cell.
   * @return images to be displayed at each cell.
   */
  public String[][] getImagesToDisplayInCells();

}
