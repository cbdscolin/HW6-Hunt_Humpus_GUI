package maze;

import java.util.Map;
import java.util.Random;


import mazecreatures.CreatureType;
import player.PlayerKilledException;

/**
 * Class that is used to create a wrapping room maze that has Wumpus, player, bats & pits.
 */
public class WrappingRoomMaze extends NonWrappingRoomMaze {

  private final int borderWallsToRemove;

  private final Wall[] borderWalls;

  private boolean removedBorderWalls;

  /**
   * Constructor that accepts random number generators, rows, column count in maze, start &
   * end co-ordinates to generate a non-wrapping room maze.
   * @param wallGenerator random number generator for Kruskal's algorithm
   * @param adversaryGenerator random number generator to add pits, bats to cells.
   * @param batMovementGenerator random number generator to help with bat moving a player
   *                             to random cell
   * @param totalColumns number of columns in maze
   * @param totalRows number of rows in maze
   * @param extraInternalWallsToRemove extra walls that have to be removed
   * @param borderWallsToRemove the number of walls at the border to be removed. At-least
   *                            a value of will make the maze a wrapping maze.
   * @param playerCount
   * @throws IllegalArgumentException thrown when invalid null generators, total rows & columns
   *        are used or trying to remove too many border walls
   */
  public WrappingRoomMaze(Random wallGenerator, Random adversaryGenerator,
                          Random batMovementGenerator, int totalColumns,
                          int totalRows, int extraInternalWallsToRemove, int borderWallsToRemove,
                          int playerCount)
          throws IllegalArgumentException {
    super(wallGenerator, adversaryGenerator, batMovementGenerator, totalColumns,
            totalRows, extraInternalWallsToRemove, playerCount);
    if (borderWallsToRemove > getBorderWallsCount()) {
      throw new IllegalArgumentException("Trying to remove too many border walls");
    }
    this.borderWallsToRemove = borderWallsToRemove;
    this.removedBorderWalls = false;
    this.borderWalls = new Wall[getBorderWallsCount()];
    this.initBorderWalls();
  }

  @Override
  public void removeWalls(Map<CreatureType, Integer> percentages, int totalPlayerArrows)
          throws UnsupportedOperationException, PlayerKilledException {
    super.removeWalls(percentages, totalPlayerArrows);
    if (this.removedBorderWalls) {
      throw new UnsupportedOperationException("Remove border walls should be called only once");
    }
    this.removeExtraWalls(this.borderWalls, this.borderWallsToRemove);
    this.removedBorderWalls = true;
    if (allRequestedWallsRemoved()) {
      this.addCreaturesToCells(percentages, totalPlayerArrows);
    }
  }

  private void initBorderWalls() {
    int rows = getTotalRows();
    int columns = getTotalColumns();
    int wallCount = 0;
    for (int i = 0; i < rows; i++) {
      this.borderWalls[wallCount++] = new Wall(i, columns - 1, true);
    }
    for (int i = 0; i < columns; i++) {
      this.borderWalls[wallCount++] = new Wall(rows - 1, i, false);
    }
  }

  private int getBorderWallsCount() {
    return getTotalColumns() + getTotalRows();
  }

  @Override
  public boolean isWrappingMaze() {
    return true;
  }

  @Override
  public boolean allRequestedWallsRemoved() {
    return super.allRequestedWallsRemoved() && removedBorderWalls;
  }
}
