package mazecreatures;

import maze.IMaze;
import player.MazePlayer;
import player.PlayerKilledException;

/**
 * This class is used to define the action performed by a pit when a player enters a
 * cell which already hosts a pit. The action here is to kill the player.
 */
public class PitAction extends WumpusPitAbstractAction {

  @Override
  public void executeAction(MazePlayer player, IMaze maze) throws PlayerKilledException {
    super.executeAction(player, maze);
    throw new PlayerKilledException("Player killed by falling in pit!");
  }
}
