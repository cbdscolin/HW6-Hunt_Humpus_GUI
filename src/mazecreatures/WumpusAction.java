package mazecreatures;

import maze.IMaze;
import player.MazePlayer;
import player.PlayerKilledException;

/**
 * This class is used to define the action performed by a wumpus when a player enters a
 * cell which already hosts a wumpus. The action here is to kill the player.
 */
public class WumpusAction extends WumpusPitAbstractAction {

  @Override
  public void executeAction(MazePlayer player, IMaze maze) throws PlayerKilledException {
    super.executeAction(player, maze);
    throw new PlayerKilledException("Player killed by Wumpus!");
  }
}
