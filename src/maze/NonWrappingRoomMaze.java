package maze;

import java.util.Map;
import java.util.Random;

import mazecreatures.CreatureType;
import player.PlayerKilledException;

/**
 * Client facing class that can be used to create a non-wrapping maze that has Wumpus, player, pits
 * and bats.
 */
public class NonWrappingRoomMaze extends AbstractMaze {

  private final int extraInternalWallsToRemove;

  private boolean removedInternalWalls;

  /**
   * Constructor that accepts random number generators, rows, column count in maze, start &
   * end co-ordinates to generate a non-wrapping room maze.
   * @param wallGenerator random number generator for Kruskal's algorithm
   * @param adversaryGenerator random number generator to add pits, bats to cells.
   * @param batMovementGenerator random number generator to help with bat moving a player
   *                             to random cell
   * @param totalColumns number of columns in maze
   * @param totalRows number of rows in maze
   * @param extraInternalWallsToRemove extra walls that have to be removed. These walls are
   *                                   removed after a perfect maze is generated.
   * @param playerCount
   * @throws IllegalArgumentException thrown when invalid null generators, total rows & columns
   *        are used or trying to remove too many internal walls.
   */
  public NonWrappingRoomMaze(Random wallGenerator, Random adversaryGenerator,
                             Random batMovementGenerator, int totalColumns,
                             int totalRows, int extraInternalWallsToRemove, int playerCount)
          throws IllegalArgumentException {
    super(wallGenerator, adversaryGenerator, batMovementGenerator, totalColumns, totalRows,
            playerCount);
    if (extraInternalWallsToRemove < 0) {
      throw new IllegalArgumentException("Non-negative value required for internal walls to be "
              + "removed");
    }
    if (this.walls.length < extraInternalWallsToRemove + (totalRows * totalColumns - 1)) {
      throw new IllegalArgumentException("Not enough walls exist that can be removed to generate "
              + "non-wrapping room maze");
    }
    this.extraInternalWallsToRemove = extraInternalWallsToRemove;
    this.removedInternalWalls = false;
    this.initWallsForMaze();
  }

  @Override
  public void removeWalls(Map<CreatureType, Integer> percentages, int totalPlayerArrows)
          throws UnsupportedOperationException,
          IllegalArgumentException, PlayerKilledException {
    super.removeWalls(percentages, totalPlayerArrows);
    if (this.removedInternalWalls) {
      throw new UnsupportedOperationException("Remove internal walls should be called only once");
    }
    this.removeExtraWalls(this.walls, this.extraInternalWallsToRemove);
    this.removedInternalWalls = true;
    if (allRequestedWallsRemoved()) {
      super.addCreaturesToCells(percentages, totalPlayerArrows);
    }
  }

  protected void removeExtraWalls(Wall[] walls, int wallsToRemove) throws IllegalArgumentException {
    int totalRemoved = 0;
    while (totalRemoved < wallsToRemove) {
      Wall wallToBreak = walls[this.wallGenerator.nextInt(walls.length)];
      if (wallToBreak.isRemoved()) {
        continue;
      }
      removeWallHelper(wallToBreak);
      totalRemoved += 1;
    }
  }

  @Override
  public boolean allRequestedWallsRemoved() {
    return super.allRequestedWallsRemoved() && this.removedInternalWalls;
  }
}
