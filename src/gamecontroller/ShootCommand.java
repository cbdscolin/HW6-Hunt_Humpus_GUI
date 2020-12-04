package gamecontroller;

import java.io.IOException;
import java.util.Scanner;
import gamemodel.IGameModel;
import maze.Direction;
import maze.MazeUtils;
import mazeexceptions.RecoverableException;
import player.PlayerKilledException;
import player.PlayerKillsWumpusException;

/**
 * The class that is used to handle the action where a player shoots the wumpus with a certain
 * power. The wumpus dies only if the shot ends up exactly in the cell where wumpus lies.
 */
public class ShootCommand extends AbstractCommand {

  protected final Scanner input;

  protected final Appendable output;

  /**
   * Constructor for the sub class of AbstractCommand. It initializes the model that is used
   * the class with model, input and output stream.
   * @param model model of the mvc architecture which has maze, player and other creatures.
   * @param input source of input
   * @param output sink of output
   */
  public ShootCommand(IGameModel model, Scanner input, Appendable output) {
    super(model);
    this.input = input;
    this.output = output;
  }

  protected Direction getRelevantDirection(Scanner scanner) {
    String dirStr = scanner.next();
    dirStr = MazeUtils.cleanString(dirStr);
    Direction validDirection = null;
    for (Direction dir: Direction.values()) {
      if (dir.getDirectionString().equals(dirStr)) {
        validDirection = dir;
        break;
      }
    }
    return validDirection;
  }

  @Override
  public void execute() throws RecoverableException, PlayerKillsWumpusException,
          PlayerKilledException, IOException {
    Scanner scanner = this.input;
    output.append("Enter the direction (N / S / E / W ) in which arrow has to be shot:\n");
    Direction validDirection = getRelevantDirection(scanner);
    if (validDirection == null) {
      throw new RecoverableException("Invalid direction entered while shooting\n");
    }
    output.append("Enter the non-zero power value with which arrow has to be shot:\n");
    String powerStr = scanner.next();
    powerStr = MazeUtils.cleanString(powerStr);
    int power = MazeUtils.parseInt(powerStr);
    if (model.shootArrow(validDirection, power)) {
      throw new PlayerKillsWumpusException("Player kills Wumpus !!!!");
    }
  }
}
