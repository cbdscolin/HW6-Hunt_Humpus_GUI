package gamecontroller;

import java.io.IOException;
import java.util.Scanner;
import gamemodel.IGameModel;
import graph.MazePoint;
import maze.Direction;
import mazecreatures.CreatureType;
import mazeexceptions.RecoverableException;
import player.PlayerKilledException;

/**
 * Class that accepts commands to move a player from one position in the maze to another position
 * in the maze. The player keeps on moving in the direction, until they encounter a non tunnel
 * cell.
 */
public class MoveCommand extends ShootCommand {

  /**
   * Constructor for the sub type of the abstract command class. It initializes the model that
   * is used by other commands inheriting this class.
   * @param model model of the mvc architecture which has maze, player and other creatures.
   * @param input source of input
   * @param output sink of output
   */
  public MoveCommand(IGameModel model, Scanner input, Appendable output) {
    super(model, input, output);
  }

  private void writeBatMovementMessage(MazePoint current, MazePoint expected, Boolean
          hasBats) throws IOException {
    if (hasBats) {
      if (current.equals(expected)) {
        output.append("Player dodged the bats!!!\n");
      } else {
        output.append("Player was carried by the bats!!!\n");
      }
    }
    output.append("Player position: " + current.toString() + "\n");
  }

  @Override
  public void execute() throws RecoverableException, IOException, PlayerKilledException {
    Scanner scanner = this.input;
    output.append("Enter the direction (N / S / E / W ) in which player wants to move:\n");
    Direction validDirection = super.getRelevantDirection(scanner);
    if (validDirection == null) {
      throw new RecoverableException("Invalid direction entered to walk\n");
    }
    MazePoint oldPosition = this.model.getPlayerCoordinates();
    MazePoint expectedPosition = this.model
            .getExpectedMovementPosition(oldPosition, validDirection);
    MazePoint newPosition;
    boolean hasBats = this.model.resultingCellHasCreature(oldPosition,
            validDirection, CreatureType.BAT, 2);
    if (hasBats) {
      output.append("Player encountered bats\n");
    }
    try {
      model.movePlayerInDirection(validDirection);
      newPosition = model.getPlayerCoordinates();
      writeBatMovementMessage(newPosition, expectedPosition, hasBats);
    } catch (PlayerKilledException exception) {
      newPosition = model.getPlayerCoordinates();
      writeBatMovementMessage(newPosition, expectedPosition, hasBats);
      throw exception;
    }
    output.append(this.model.printMaze()).append("\n");
  }
}
