package driver;

import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

import gamecontroller.IMazeController;
import gamecontroller.MazeController;
import gamemodel.GameModel;
import gamemodel.IGameModel;
import maze.IMaze;
import maze.MazeUtils;
import maze.NonWrappingRoomMaze;
import maze.WrappingRoomMaze;

/**
 * Class that is used to instantiate a standalone java application which runs the Hunt the
 * Wumpus game.
 */
public class Driver {

  private static int readInt() {
    Scanner sc = new Scanner(System.in);
    String inp = MazeUtils.cleanString(sc.nextLine());
    return Integer.parseInt(inp);
  }

  /**
   * The main function which start the standalone application. This used MVC design pattern to
   * build a maze and carry out user actions.
   * @param args the argument for the function
   */
  public static void main(String[] args) {
    try {
      System.out.println("Enter the number of rows in the maze:\n");
      int rows = readInt();
      System.out.println("Enter the number of columns in the maze:\n");
      int cols = readInt();
      System.out.println("Enter the number of internal walls from the maze to be removed:\n");
      int internalWalls = readInt();
      System.out.println("Enter the number of border walls from the maze to be removed:\n");
      int borderWalls = readInt();

      IMaze maze;
      if (borderWalls > 0) {
        maze = new WrappingRoomMaze(new Random(), new Random(), new Random(),
                cols, rows, internalWalls, borderWalls);
      } else {
        maze = new NonWrappingRoomMaze(new Random(), new Random(), new Random(),
                cols, rows, internalWalls);
      }
      IGameModel model = new GameModel(maze);
      IMazeController controller = new MazeController(new InputStreamReader(System.in),
              System.out, model);
      controller.start();
    } catch (Exception exception) {
      System.out.println("Exception occurred: " + exception.getMessage());
      System.out.println("Exiting....\n");
    }
  }
}
