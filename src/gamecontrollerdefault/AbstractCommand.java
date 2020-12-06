package gamecontrollerdefault;

import java.io.IOException;

import gamemodel.IGameModel;
import mazeexceptions.RecoverableException;
import mazeexceptions.UnrecoverableException;
import player.PlayerKilledException;
import player.PlayerKillsWumpusException;

/**
 * The super class that should be overridden by the sub classes that are used to
 * encapsulate and execute commands from the controller in MVC architecture.
 */
public abstract class AbstractCommand implements IMazeCommand {

  protected final IGameModel model;

  /**
   * Constructor for the super class. It initializes the model that is used by other commands
   * inheriting this class.
   * @param model model of the mvc architecture which has maze, player and other creatures.
   */
  protected AbstractCommand(IGameModel model) {
    this.model = model;
  }

  @Override
  public abstract void execute() throws UnrecoverableException, PlayerKilledException,
          PlayerKillsWumpusException, IOException, RecoverableException;
}
