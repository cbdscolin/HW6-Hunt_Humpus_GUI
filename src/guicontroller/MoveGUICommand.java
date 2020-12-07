package guicontroller;

import gamemodel.IGameModel;
import graph.MazePoint;
import maze.Direction;
import mazecreatures.CreatureType;
import player.PlayerKilledException;

/**
 * Class that accepts commands to move a player from one position in the maze to another position
 * in the maze. The player keeps on moving in the direction, until they encounter a non tunnel
 * cell.
 */
public class MoveGUICommand implements IMazeGUICommand {

  private final Direction direction;

  private final IGameModel model;
  public MoveGUICommand(IGameModel model, Direction direction) throws IllegalArgumentException {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null in GUI move command");
    }
    this.model = model;
    this.direction = direction;
  }

  @Override
  public CommandOutputMessage execute() {
    MazePoint oldPosition = this.model.getActivePlayerCoordinates();
    MazePoint expectedPosition = this.model
            .getExpectedMovementPosition(oldPosition, direction);
    MazePoint newPosition;
    boolean hasBats = this.model.resultingCellHasCreature(oldPosition,
            direction, CreatureType.BAT, 2);
    StringBuilder status = new StringBuilder();
    try {
      model.movePlayerInDirection(direction);
    } catch (PlayerKilledException exception) {
      return new CommandOutputMessage(true, exception.getMessage(), true,
              false);
    } catch (Exception exception) {
      return new CommandOutputMessage(false, exception.getMessage(), false,
              false);
    }
    return new CommandOutputMessage(true, "", false,
            false);
  }
}
