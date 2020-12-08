package mazecreatures;

import maze.IMaze;
import player.MazePlayer;
import player.PlayerKilledException;

/**
 * This class is used to define the action performed by a pit or a wumpus when a player enters a
 * cell which already hosts a pit or a bit. The action here is to kill the player.
 */
public abstract class WumpusPitAbstractAction implements ICreatureAction {

  @Override
  public void executeAction(MazePlayer player, IMaze maze) throws PlayerKilledException {
    player.markPlayerDead();
    maze.incrementKilledPlayersCount(player);
  }
}
