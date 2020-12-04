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

  private final IMaze maze;

  /**
   * Initialises game model by using the maze passed in as parameter.
   * @param maze the maze used for the game
   */
  public GameModel(IMaze maze) {
    this.maze = maze;
  }

  public String printMaze() {
    return this.maze.printMaze();
  }

  @Override
  public void removeWalls(Map<CreatureType, Integer> percentages, int totalPlayerArrows)
          throws UnsupportedOperationException, IllegalArgumentException, PlayerKilledException {
    this.maze.removeWalls(percentages, totalPlayerArrows);
  }

  @Override
  public MazePoint getPlayerCoordinates() {
    return maze.getPlayerCoordinates();
  }

  @Override
  public List<Direction> getValidDirectionsForMovement() {
    return maze.getValidDirectionsForMovement();
  }

  public boolean checkCreatureInAdjacentCells(CreatureType creatureType) {
    MazePoint playerPos = getPlayerCoordinates();
    return this.maze.checkCreatureInAdjacentCells(playerPos, creatureType, null);
  }

  @Override
  public boolean shootArrow(Direction dir, int power) throws
          PlayerKillsWumpusException, RecoverableException, PlayerKilledException {
    MazePoint playerPos = getPlayerCoordinates();
    return maze.shootArrow(playerPos, dir, power);
  }

  @Override
  public void movePlayerInDirection(Direction direction)
          throws PlayerKilledException, RecoverableException {
    maze.movePlayerInDirection(direction);
  }

  @Override
  public boolean resultingCellHasCreature(MazePoint point, Direction dir,
                                          CreatureType creatureType, int distance) {
    return maze.resultingCellHasCreature(point, dir, creatureType, distance);
  }

  @Override
  public MazePoint getExpectedMovementPosition(MazePoint point, Direction dir) {
    return this.maze.getExpectedMovementPosition(point, dir);
  }
}
