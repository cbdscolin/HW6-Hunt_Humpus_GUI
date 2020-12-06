package driver;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import gamecontrollerdefault.IMazeController;
import guicontroller.IMazeGUIController;
import guicontroller.MazeGUIController;
import textgamecontroller.MazeController;
import gamemodel.GameModel;
import gamemodel.IGameModel;
import maze.IMaze;
import maze.MazeUtils;
import maze.NonWrappingRoomMaze;
import maze.WrappingRoomMaze;
import view.GUIView;
import view.IView;

/**
 * Class that is used to instantiate a standalone java application which runs the Hunt the
 * Wumpus game.
 */
public class Driver {

  private  Map<String, Runnable> commandLineMap;

  private static int readInt() {
    Scanner sc = new Scanner(System.in);
    String inp = MazeUtils.cleanString(sc.nextLine());
    return Integer.parseInt(inp);
  }

  private void executeTextActions() {
    try {

      System.out.println("Enter the number of rows in the maze:\n");
      int rows = readInt();
      System.out.println("Enter the number of columns in the maze:\n");
      int cols = readInt();
      System.out.println("Enter the number of internal walls from the maze to be removed:\n");
      int internalWalls = readInt();
      System.out.println("Enter the number of border walls from the maze to be removed:\n");
      int borderWalls = readInt();
      System.out.println("Enter the number of players playing the game:\n");
      int playerCount = readInt();

      IMaze maze;
      if (borderWalls > 0) {
        maze = new WrappingRoomMaze(new Random(), new Random(), new Random(),
                cols, rows, internalWalls, borderWalls, playerCount);
      } else {
        maze = new NonWrappingRoomMaze(new Random(), new Random(), new Random(),
                cols, rows, internalWalls, playerCount);
      }
      IGameModel model = new GameModel(maze);
      IMazeController controller = new MazeController(new InputStreamReader(System.in),
              System.out, model);
      controller.start();
    } catch (Exception exception) {
      System.out.println("Exception occurred: " + exception.getMessage());
      exception.printStackTrace();
      System.out.println("Exiting....\n");
    }
  }

  private void executeGUIActions() {
    IGameModel model = new GameModel();
    IMazeGUIController controller = new MazeGUIController(model);
    IView view = new GUIView("Hunt the Wumpus Game", controller);
    controller.setView(view);
  }

  private void initCommandLineMapper() {
    commandLineMap = new HashMap<>();
    commandLineMap.put("--text", this::executeTextActions);
    commandLineMap.put("--gui", this::executeGUIActions);
  }

  private void execute(String action) {
    if (this.commandLineMap.containsKey(action)) {
      this.commandLineMap.get(action).run();
    }
  }


  /**
   * The main function which start the standalone application. This used MVC design pattern to
   * build a maze and carry out user actions.
   * @param args the argument for the function
   */
  public static void main(String[] args) {
    if (args != null && (args.length != 1)) {
      System.err.println("Specify --text or --gui as the only argument for the program");
      System.exit(1);
    }
    Driver driver = new Driver();
    driver.initCommandLineMapper();
    driver.execute(args[0]);
  }
}
