package guicontroller;

import gamemodel.IGameModel;
import maze.Direction;
import player.PlayerKilledException;

/**
 * Class that accepts commands to move a player from one position in the maze to another position
 * in the maze. The player keeps on moving in the direction, until they encounter a non tunnel
 * cell.
 */
public class MoveGUICommand implements IMazeGUICommand {

  private final Direction direction;

  private final IGameModel model;

  /**
   * Constructor for the sub type of the abstract command class. It initializes the model that
   * is used by other commands inheriting this class.
   * @param model model of the mvc architecture which has maze, player and other creatures.
   * @param direction direction to move the player
   * @throws IllegalArgumentException thrown when direction or model is null
   */
  public MoveGUICommand(IGameModel model, Direction direction) throws IllegalArgumentException {
    if (direction == null || model == null) {
      throw new IllegalArgumentException("Direction/model cannot be null in GUI move command");
    }
    this.model = model;
    this.direction = direction;
  }

  @Override
  public CommandOutputMessage execute() {
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
