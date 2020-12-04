package mazecreatures;

import java.util.List;
import java.util.Random;

import graph.MazePoint;
import maze.Cell;
import maze.IMaze;
import maze.MazeUtils;
import player.MazePlayer;
import player.PlayerKilledException;

/**
 * This class is used to define the action performed by a bat when a player enters a cell which
 * already hosts a bat. The action here is to move the player from the current cell to another cell
 * inside the maze. A bat may drop the player at its current position too. A bat has 50 percent
 * chance that it picks the player and drops into a random cell.
 */
public class BatAction implements ICreatureAction {

  @Override
  public void executeAction(MazePlayer player, IMaze maze) throws PlayerKilledException {
    Random dropGenerator = maze.getMovementGenerator();

    if (dropGenerator.nextInt(MazeUtils.MAX_PERCENT) > MazeUtils.BAT_PICK_PERCENTAGE) {
      List<Cell> availableCells = maze.getNonTunnelCells();
      int cellToMoveIndex = dropGenerator.nextInt(availableCells.size());
      Cell cellToMove = availableCells.get(cellToMoveIndex);
      MazePoint newPos = new MazePoint(cellToMove.getRowPosition(),
              cellToMove.getColumnPosition());
      player.setNewPosition(newPos);
      cellToMove.performCellActions(player, maze);
    }
  }
}
