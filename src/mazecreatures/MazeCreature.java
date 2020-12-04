package mazecreatures;

import maze.IMaze;
import player.MazePlayer;
import player.PlayerKilledException;

/**
 * Class that contains information about creatures such as Bats, Pits, Wumpus present in cells.
 * When a player enters a cell which has this creature then the particular action of the creature
 * is enforced upon a player. For instance pit or wumpus kills the player, bat moves a player to
 * a random cell and so on.
 */
public class MazeCreature implements ICreature {

  private final CreatureType creatureType;

  private final ICreatureAction action;

  public MazeCreature(CreatureType creatureType, ICreatureAction action) {
    this.creatureType = creatureType;
    this.action = action;
  }

  public int getPriority() {
    return this.creatureType.getPriority();
  }

  @Override
  public boolean executeAction(MazePlayer player, IMaze maze) throws PlayerKilledException {
    action.executeAction(player, maze);
    return true;
  }

  @Override
  public CreatureType getCreatureType() {
    return this.creatureType;
  }

  @Override
  public int compareTo(ICreature o) {
    return this.getPriority() - o.getPriority();
  }
}
