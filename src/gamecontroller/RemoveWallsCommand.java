package gamecontroller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import gamemodel.IGameModel;
import graph.MazePoint;
import maze.Direction;
import maze.MazeUtils;
import mazecreatures.CreatureType;
import mazeexceptions.UnrecoverableException;
import player.PlayerKilledException;

/**
 * Class that is responsible to generate a maze in the model. It generates the maze by reading
 * the number of bats, pit and places them in non-tunnel cells in a maze. The number of bats
 * and pits occupying the maze should be sent as a percentage input to the maze. The maze then
 * fills the cells with bats & pits from the elligible cells.
 */
public class RemoveWallsCommand extends AbstractCommand {

  Scanner input;

  Appendable output;

  /**
   * Constructor for the sub class of AbstractCommand. It initializes the model that is used
   * the class with model, input and output stream.
   * @param model model of the mvc architecture which has maze, player and other creatures.
   * @param input source of input
   * @param output sink of output
   */
  public RemoveWallsCommand(IGameModel model, Scanner input, Appendable output) {
    super(model);
    this.input = input;
    this.output = output;
  }

  @Override
  public void execute() throws UnrecoverableException, PlayerKilledException {
    Scanner scan = this.input;
    Map<CreatureType, Integer> data = new LinkedHashMap<>();
    try {
      for (CreatureType type : CreatureType.getCustomizableCreatures()) {
        output.append("Enter the percentage occurrence of " + type.getCreatureName()
                + " in maze. Enter an integer between 0 & 100\n");
        String inp = scan.next();
        int value = MazeUtils.parseInt(inp);
        data.put(type, value);
      }
      output.append("Enter a non-zero value for number of arrows possessed by the player:\n");
      String inp = scan.next();
      int value = MazeUtils.parseInt(inp);
      model.removeWalls(data, value);
      MazePoint playerPos = model.getPlayerCoordinates();
      output.append("Player is in the position: " + playerPos.toString()
              + " \n");
      if (this.model.resultingCellHasCreature(playerPos, Direction.EAST,
              CreatureType.BAT, 1)) {
        output.append("Player encounters bats & dodges them!!\n");
      }
    } catch (PlayerKilledException exception) {
      throw exception;
    } catch (Exception exception) {
      throw new UnrecoverableException("Unrecoverable exception: " + exception.getMessage());
    }
  }
}
