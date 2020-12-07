package guicontroller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gamemodel.IGameModel;
import maze.Direction;
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
public class MazeGUIController implements IMazeGUIController {

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
    IMaze maze;
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
    view.hideInputScreen();
    view.showMaze();
    view.sendCellImagesToView(model.getImagesToDisplayInCells(false));
    view.showGameStatusMessage("Game Active");
    sendPlayerTurnMessage();
    sendValidDirectionsMessage();
    System.out.println(model.printMaze(true));
  }

  private void sendPlayerTurnMessage() {
    view.showPlayerTurnMessage("Player " + (model.getActivePlayerIndex() + 1) + "'s turn");
  }

  private void sendValidDirectionsMessage() {
    List<Direction> directions = model.getValidDirectionsForMovement();
    StringBuilder builder = new StringBuilder("Directions:  ");
    for (int ii = 0; ii < directions.size(); ii++) {
      if (ii >= (directions.size() - 1)) {
        builder.append(directions.get(ii).getDirectionString());
      } else {
        builder.append(directions.get(ii).getDirectionString()).append(" | ");
      }
    }
    view.showValidDirectionsMessage(builder.toString());
  }

  private void executeAction(IMazeGUICommand command) {
    CommandOutputMessage outputMessage = command.execute();
    if (!outputMessage.isSuccess()) {
      view.showErrorMessage(outputMessage.getStatusMessage());
      view.sendCellImagesToView(model.getImagesToDisplayInCells(false));
    } else if (outputMessage.isPlayerKilled()) {
      StringBuilder output = new StringBuilder();
      output.append("Player " + (model.getActivePlayerIndex() + 1) + " has lost!\n");
      view.sendCellImagesToView(model.getImagesToDisplayInCells(true));
      view.endGameWithMessage(output.toString());
      view.showPlayerTurnMessage(outputMessage.getStatusMessage());
    } else if (outputMessage.isWumpusKilled()) {
      StringBuilder output = new StringBuilder();
      output.append("Player " + (model.getActivePlayerIndex() + 1) + " has won!\n");
      view.sendCellImagesToView(model.getImagesToDisplayInCells(true));
      view.endGameWithMessage(output.toString());
      view.showPlayerTurnMessage(outputMessage.getStatusMessage());
    } else {
      view.sendCellImagesToView(model.getImagesToDisplayInCells(false));
      this.sendPlayerTurnMessage();
      this.sendValidDirectionsMessage();
    }
  }

  @Override
  public void movePlayerInDirection(Direction direction) {
    IMazeGUICommand command = new MoveGUICommand(model, direction);
    executeAction(command);
  }

  @Override
  public void shootPlayerInDirection(Direction direction, int arrowPower) {
    IMazeGUICommand command = new ShootGuiCommand(model, direction, arrowPower);
    executeAction(command);
  }
}
