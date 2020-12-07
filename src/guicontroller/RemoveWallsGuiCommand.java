package guicontroller;

import java.util.Map;

import gamemodel.IGameModel;
import mazecreatures.CreatureType;
import mazeexceptions.UnrecoverableException;
import player.PlayerKilledException;

/**
 * Class that is responsible to generate a maze in the model. It generates the maze by reading
 * the number of bats, pit and places them in non-tunnel cells in a maze. The number of bats
 * and pits occupying the maze should be sent as a percentage input to the maze. The maze then
 * fills the cells with bats & pits from the elligible cells.
 */
public class RemoveWallsGuiCommand implements IMazeGUICommand {

  private final IGameModel model;

  private final Map<CreatureType, Integer> batPitPercentage;

  private final int arrowCount;

  /**
   * Initializes fields for this command. The command removes walls from the maze.
   * @param model maze model
   * @param batPitPercentage percentage of bat and pit in maze
   */
  public RemoveWallsGuiCommand(IGameModel model, Map<CreatureType, Integer> batPitPercentage,
                               int arrowCount)
          throws IllegalArgumentException {
    if (model == null || batPitPercentage == null) {
      throw new IllegalArgumentException("Mode and bat-pit percentage can't be null");
    }
    this.model = model;
    this.batPitPercentage = batPitPercentage;
    this.arrowCount = arrowCount;
  }

  @Override
  public CommandOutputMessage execute() {
    try {
      model.removeWalls(batPitPercentage, arrowCount);
    } catch (PlayerKilledException exception) {
      return new CommandOutputMessage(true, exception.getMessage(),
              true, false);
    } catch (Exception exception) {
      return new CommandOutputMessage(false, "Cannot create maze : "
              + exception.getMessage(), false, false);
    }
    return new CommandOutputMessage(true, "", false,
            false);
  }
}
