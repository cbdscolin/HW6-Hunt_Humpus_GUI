package gamecontroller;

import java.io.IOException;

/**
 * Interface that outlines the behaviour of the controller for hunt the wumpus game.
 */
public interface IMazeController {

  /**
   * Starts the controller by calling the relevant objects which use command design pattern to
   * execute actions on the player and the maze.
   * @throws IOException thrown when invalid input is supplied to the controller
   */
  void start() throws IOException;
}
