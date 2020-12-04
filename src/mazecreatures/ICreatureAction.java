package mazecreatures;

import maze.IMaze;
import player.MazePlayer;
import player.PlayerKilledException;

/**
 * Actions that can be defined which are performed when a creature encounters a player in any of the
 * cells. The default action for a bat is to move player from a cell to another randomly. The action
 * for pit and wumpus is to kill the player.
 */
public interface ICreatureAction {

  /**
   * Executes an action that a creatures performs when a creature and player meet in a cell.
   * @param player the player playing the game
   * @param maze the maze which is hosting the game
   * @throws PlayerKilledException thrown when this actions kills the player.
   */
  public void executeAction(MazePlayer player, IMaze maze) throws PlayerKilledException;
}
