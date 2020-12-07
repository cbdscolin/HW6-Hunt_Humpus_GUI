package guicontroller;

import gamemodel.IGameModel;
import maze.Direction;
import player.PlayerKilledException;

/**
 * The class that is used to handle the action where a player shoots the wumpus with a certain
 * power. The wumpus dies only if the shot ends up exactly in the cell where wumpus lies.
 */
public class ShootGuiCommand implements IMazeGUICommand {

  private final Direction direction;

  private final IGameModel model;

  private final int arrowPower;

  /**
   * Constructor for the sub class of AbstractCommand. It initializes the model that is used
   * the class with model, direction and power of the arrow.
   * @param direction direction to shoot
   * @param model maze model
   * @param arrowPower power of arrow
   */
  public ShootGuiCommand(IGameModel model, Direction direction, int arrowPower) {
    this.direction = direction;
    this.model = model;
    this.arrowPower = arrowPower;
  }

  @Override
  public CommandOutputMessage execute() {
    try {
      if (model.shootArrow(direction, arrowPower)) {
        return new CommandOutputMessage(true, "Player Kills Wumpus",
                false, true);
      }
    } catch (PlayerKilledException exception) {
      return new CommandOutputMessage(true, exception.getMessage(), true,
              false);
    } catch (Exception exception) {
      return new CommandOutputMessage(false, "Error while shooting arrow: "
              + exception.getMessage(), false, false);
    }
    return new CommandOutputMessage(true, "Arrow missed Wumpus!",
            false, false);
  }
}
