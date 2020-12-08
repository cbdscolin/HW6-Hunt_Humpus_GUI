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

  private long wallGenerator;

  private long adversaryGenerator;

  private long batMovementGenerator;

  private IView view;

  /**
   * Initializes the controller for the GUI version of the hunt the wumpus game.
   * @param model the model for this game
   */
  public MazeGUIController(IGameModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Null model passed to controller");
    }
    this.model = model;
    this.wallGenerator = 0;
    this.adversaryGenerator = 0;
    this.batMovementGenerator = 0;
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
                         int playerCount, int pitPercentage, int batPercentage, int arrowCount,
                         boolean usePast) {
    IMaze maze;
    if (!usePast || (batMovementGenerator == 0 && adversaryGenerator == 0
            && wallGenerator == 0)) {
      wallGenerator =  Math.round(Math.random());
      adversaryGenerator = Math.round(Math.random());
      batMovementGenerator = Math.round(Math.random());
    }
    if (externalWalls > 0) {
      maze = new WrappingRoomMaze(new Random(wallGenerator), new Random(adversaryGenerator),
              new Random(batMovementGenerator), columns, rows, internalWalls, externalWalls,
              playerCount);
    } else {
      maze = new NonWrappingRoomMaze(new Random(wallGenerator), new Random(adversaryGenerator),
              new Random(batMovementGenerator), columns, rows, internalWalls, playerCount);
    }
    model.setMaze(maze);
    view.hideInputScreen();
    view.showMaze();
    view.showGameStatusMessage("GAME ACTIVE");
    Map<CreatureType, Integer> data = new LinkedHashMap<>();
    data.put(CreatureType.BAT, batPercentage);
    data.put(CreatureType.PIT, pitPercentage);
    IMazeGUICommand command = new RemoveWallsGuiCommand(model, data, arrowCount);
    executeAction(command);
  }

  private void sendPlayerTurnMessage() {
    view.showPlayerTurnMessage("Player " + (model.getActivePlayerIndex() + 1) + "'s turn now!");
  }

  private void sendValidDirectionsMessage() {
    List<Direction> directions = model.getValidDirectionsForMovement();
    StringBuilder builder = new StringBuilder("Player can move:  ");
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
      if (model.isGameComplete()) {
        view.endGameWithMessage("Player " + (model.lastKilledPlayerIndex() + 1) + " has lost!\n");
        view.sendCellImagesToView(model.getImagesToDisplayInCells(true));
        view.showPlayerTurnMessage(outputMessage.getStatusMessage());
      } else {
        view.showGameStatusMessage("Player " + (model.lastKilledPlayerIndex() + 1)
                + " has lost!\n");
        view.sendCellImagesToView(model.getImagesToDisplayInCells(false));
        this.sendPlayerTurnMessage();
        this.sendValidDirectionsMessage();
      }
    } else if (outputMessage.isWumpusKilled()) {
      view.sendCellImagesToView(model.getImagesToDisplayInCells(true));
      view.endGameWithMessage("Player " + (model.getActivePlayerIndex() + 1) + " has won!\n");
      view.showPlayerTurnMessage(outputMessage.getStatusMessage());
    } else {
      view.sendCellImagesToView(model.getImagesToDisplayInCells(false));
      if (outputMessage.getStatusMessage() != null
              && !outputMessage.getStatusMessage().isEmpty()) {
        view.showErrorMessage(outputMessage.getStatusMessage());
      }
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
