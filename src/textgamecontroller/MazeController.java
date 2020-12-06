package textgamecontroller;

import java.io.IOException;
import java.util.Scanner;

import gamecontrollerdefault.IMazeCommand;
import gamecontrollerdefault.IMazeController;
import gamemodel.IGameModel;
import maze.MazeUtils;
import mazeexceptions.RecoverableException;
import mazeexceptions.UnrecoverableException;
import player.PlayerKilledException;
import player.PlayerKillsWumpusException;

/**
 * Class that is used to define the features offered by the controller of the hunt the wumpus
 * game.
 */
public class MazeController implements IMazeController {

  private static final String YES_VALUE = "y";

  private final Readable input;

  private final Appendable output;

  private final IGameModel model;

  /**
   * Initializes the controller class with a model which has maze, players, wumpus and so on. The
   * input and output streams are used to read input and write response to the appropriate
   * streams.
   * @param input data to read from
   * @param output data to write to
   * @param model maze model
   */
  public MazeController(Readable input, Appendable output, IGameModel model) {
    this.input = input;
    this.output = output;
    this.model = model;
  }

  private void printDebugTable(String enableTableDebug, boolean showBarriers) throws IOException {
    //System.out.println(this.model.printMaze());
    if (YES_VALUE.equals(enableTableDebug)) {
      this.output.append(this.model.printMaze(showBarriers)).append("\n");
    }
  }

  @Override
  public void start() throws IOException {
    try {
      Scanner scanner = new Scanner(this.input);
      IMazeCommand baseCmd = new RemoveWallsCommand(model, scanner, output);
      baseCmd.execute();
      output.append("Do you want to view the table with Wumpus/Player/Pit positions?:\n");
      output.append("Type Y or y to enable displaying table, press N otherwise\n");
      String enableTableDebug = scanner.next();
      enableTableDebug = MazeUtils.cleanString(enableTableDebug).toLowerCase();
      printDebugTable(enableTableDebug, true);
      while (true) {
        IMazeCommand cmd = new ShowDirectionsCommand(model, output);
        cmd.execute();
        try {
          output.append("Shoot or move? Press S or s to shoot and M or m to move:\n");
          String shootMoveCh = scanner.next();
          shootMoveCh = MazeUtils.cleanString(shootMoveCh).toLowerCase();
          switch (shootMoveCh) {
            case "s":
              cmd = new ShootCommand(model, scanner, output);
              break;
            case "m":
              cmd = new MoveCommand(model, scanner, output);
              break;
            default:
              throw new RecoverableException("Unknown option entered: " + shootMoveCh);
          }
          cmd.execute();
          //printDebugTable(enableTableDebug, false);
        }
        catch (RecoverableException xp) {
          output.append(xp.getMessage() + "\n");
        }
      }

    } catch (PlayerKilledException exp) {
      output.append("Player "  + (model.getActivePlayerIndex() + 1)+ "  Lost !!!!\n");
      output.append(exp.getMessage()).append("\n");
    } catch (PlayerKillsWumpusException exp) {
      output.append("Player " + (model.getActivePlayerIndex() + 1)+ " Wins !!!!\n");
      output.append(exp.getMessage()).append("\n");
    } catch (UnrecoverableException | RecoverableException exp) {
      output.append(exp.getMessage()).append("\n");
      exp.printStackTrace();
    }
    output.append(model.printMaze(true));
    output.append("\n");
  }
}
