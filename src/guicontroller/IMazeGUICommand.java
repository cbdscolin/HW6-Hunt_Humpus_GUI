package guicontroller;

import java.io.IOException;

import mazeexceptions.RecoverableException;
import mazeexceptions.UnrecoverableException;
import player.PlayerKilledException;
import player.PlayerKillsWumpusException;

/**
 * The interface which outlines the behaviour of the command that can be run on the maze model.
 */
public interface IMazeGUICommand {

  /**
   * The function executes the action defined by the command. The various actions are move a
   * player, kill a player, show directions to a player and shoot the wumpus.
   */
  CommandOutputMessage execute();
}
