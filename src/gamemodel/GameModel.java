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
 * The class that is used to describe encapsulate the model for hunt the wumpus game. The model
 * mainly consists of a maze which can be wrapping or non-wrapping. The maze internally has
 * 1 player, 1 wumpus, a certain percentage of pits & bats.
 */
public class GameModel implements IGameModel {

  private IMaze maze;

  /**
   * Initialises game model by using the maze passed in as parameter.
   * @param maze the maze used for the game
   */
  public GameModel(IMaze maze) {
    this.maze = maze;
  }

  /**
   * Initializes empty game model. A maze has to be passed at a later point of time.
   */
  public GameModel() {
  }

  public void setMaze(IMaze maze) {
    if (maze == null) {
      throw new IllegalArgumentException("Null maze passed to game model");
    }
    this.maze = maze;
  }

  private boolean isMazeNull() {
    return maze == null;
  }

  public String printMaze(boolean showBarriers) throws IllegalStateException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in printMaze");
    }
    return this.maze.printMaze(showBarriers);
  }

  @Override
  public void removeWalls(Map<CreatureType, Integer> percentages, int totalPlayerArrows)
          throws UnsupportedOperationException, IllegalArgumentException,
          PlayerKilledException, IllegalStateException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in removeWalls");
    }
    this.maze.removeWalls(percentages, totalPlayerArrows);
  }

  @Override
  public MazePoint getActivePlayerCoordinates() throws IllegalStateException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in getActivePlayerCoordinates");
    }
    return maze.getActivePlayerCoordinates();
  }

  @Override
  public List<Direction> getValidDirectionsForMovement() throws IllegalStateException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in getValidDirectionsForMovement");
    }
    return maze.getValidDirectionsForMovement();
  }

  public boolean checkCreatureInAdjacentCells(CreatureType creatureType) throws
          IllegalStateException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in checkCreatureInAdjacentCells");
    }
    MazePoint playerPos = getActivePlayerCoordinates();
    return this.maze.checkCreatureInAdjacentCells(playerPos, creatureType, null);
  }

  @Override
  public boolean shootArrow(Direction dir, int power) throws IllegalStateException,
          PlayerKillsWumpusException, RecoverableException, PlayerKilledException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in shootArrow");
    }
    MazePoint playerPos = getActivePlayerCoordinates();
    return maze.shootArrow(playerPos, dir, power);
  }

  @Override
  public void movePlayerInDirection(Direction direction)
          throws PlayerKilledException, RecoverableException, IllegalStateException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in movePlayerInDirection");
    }
    maze.movePlayerInDirection(direction);
  }

  @Override
  public boolean resultingCellHasCreature(MazePoint point, Direction dir,
                                          CreatureType creatureType, int distance)
          throws IllegalStateException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in resultingCellHasCreature");
    }
    return maze.resultingCellHasCreature(point, dir, creatureType, distance);
  }

  @Override
  public MazePoint getExpectedMovementPosition(MazePoint point, Direction dir) throws
          IllegalStateException  {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in getExpectedMovementPosition");
    }
    return this.maze.getExpectedMovementPosition(point, dir);
  }

  @Override
  public int getActivePlayerIndex() throws IllegalStateException {
    if (isMazeNull()) {
      throw new IllegalStateException("Maze is null in getActivePlayerIndex");
    }
    return this.maze.getActivePlayerIndex();
  }
}
