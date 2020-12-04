package mazecreatures;

import maze.IMaze;
import player.MazePlayer;
import player.PlayerKilledException;

/**
 * Interface that necessitates the behaviour of items in the caves such as pits, wumpus, bats
 * etc. which are present inside the cells of the maze.
 */
public interface ICreature extends Comparable<ICreature> {

  /**
   * Returns the priority of the creature present in the cell.
   * @return priority of the creatures in the cell.
   */
  int getPriority();

  /**
   * Executes the action associated with this creature. The action can be killing a player
   * or moving a player to a random cell.
   * @param player the maze player
   * @param maze the maze
   * @return true if the action was executed
   */
  boolean executeAction(MazePlayer player, IMaze maze) throws PlayerKilledException;

  /**
   * Function returns the enum value associated with Bat, Pit or Wumpus.
   * @return enum value associated with Bat, Pit or Wumpus
   */
  CreatureType getCreatureType();

}
