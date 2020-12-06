package guicontroller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import gamecontrollerdefault.IMazeController;
import gamemodel.IGameModel;
import maze.IMaze;
import maze.NonWrappingRoomMaze;
import maze.WrappingRoomMaze;
import mazecreatures.CreatureType;
import view.IView;

/**
 * Class that is used to handle functionalities related to the controller that works with the view
 * that supports GUI and has a maze model. The controller offers various operations to the players
 * by using the view in MVC.
 */
public class MazeGUIController implements IMazeController, IMazeGUIController {

  private static final int MAX_ROWS_AND_COLUMNS = 30;

  private final IGameModel model;

  private IView view;

  public MazeGUIController(IGameModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Null model passed to controller");
    }
    this.model = model;
  }

  @Override
  public void start() {
  }

  @Override
  public void setView(IView view) {
    this.view = view;
  }

  @Override
  public int getMaximumColumns() {
    return MAX_ROWS_AND_COLUMNS;
  }

  @Override
  public int getMaximumRows() {
    return MAX_ROWS_AND_COLUMNS;
  }

  @Override
  public void createMaze(int rows, int columns, int internalWalls, int externalWalls,
                         int playerCount, int pitPercentage, int batPercentage, int arrowCount) {
    IMaze maze = null;
    try {
      if (externalWalls > 0) {
        maze = new WrappingRoomMaze(new Random(), new Random(), new Random(),
                columns, rows, internalWalls, externalWalls, playerCount);
      } else {
        maze = new NonWrappingRoomMaze(new Random(), new Random(), new Random(),
                columns, rows, internalWalls, playerCount);
      }
      model.setMaze(maze);
      Map<CreatureType, Integer> data = new LinkedHashMap<>();
      data.put(CreatureType.BAT, batPercentage);
      data.put(CreatureType.PIT, pitPercentage);
      model.removeWalls(data, arrowCount);
    } catch (Exception exception) {
      view.showErrorMessage("Cannot construct maze: " + exception.getMessage());
      return;
    }
    view.hideErrorMessage();
    view.hideInputScreen();
    view.displayMaze();
  }
}
