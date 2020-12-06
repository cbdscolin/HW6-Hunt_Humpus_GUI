package maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import graph.MazePoint;
import mazecreatures.CreatureType;
import mazecreatures.ICreature;
import player.MazePlayer;
import player.PlayerKilledException;


/**
 * A class that is used to represent a single cell in a maze. The class stores its location in the
 * maze and also if it contains bats, pits or a wumpus. The cells are surrounded by 0 to 4 walls
 * which block the free movement of arrows & players. A cell becomes a tunnel if it has only 2
 * walls in any of the 4 directions.
 */
public class Cell {

  private static final  int TUNNEL_CELL_WALLS = 2;

  private final int rowPosition;

  private final int columnPosition;

  private final List<Direction> walls;

  private final List<ICreature> creatures;

  private boolean isVisible;

  /**
   * Used to initialize a cell in a maze by specifying a specific position in the maze.
   * @param rowPosition row index of the cell
   * @param columnPosition column index of the cell
   * @throws IllegalArgumentException thrown when invalid cell position is passed
   * @throws IllegalArgumentException thrown when a cell has both gold and thief
   */
  public Cell(int rowPosition, int columnPosition) throws
          IllegalArgumentException {
    if (rowPosition < 0 || columnPosition < 0) {
      throw new IllegalArgumentException("Invalid cell position passed");
    }
    this.rowPosition = rowPosition;
    this.columnPosition = columnPosition;
    this.walls =  new ArrayList<>(Arrays.asList(Direction.values()));
    this.creatures = new ArrayList<>();
    this.isVisible = false;
  }

  /**
   * Returns the row index where this cell is present.
   * @return row index of the cell
   */
  public int getColumnPosition() {
    return columnPosition;
  }

  /**
   * Returns the column index where this cell is present.
   * @return column index of the cell
   */
  public int getRowPosition() {
    return rowPosition;
  }

  /**
   * Returns true if the cell already has a type of creatures added. This can be used to avoid
   * duplicate creatures added to the same cell.
   * @param creatureType the type of creature to be checked for
   * @return true if the creature type is present in cell, false otherwise
   */
  public boolean hasCreature(CreatureType creatureType) {
    for (ICreature creature: this.creatures) {
      if (creature.getCreatureType().equals(creatureType)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds a creature such as Wumpus, Pit or Bat to this cell. If it already has such creature,
   * then an exception is thrown. Creature cannot be added to a tunnel.
   * @param creature bat, pit or wumpus
   * @throws UnsupportedOperationException thrown when the cell is a tunnel
   * @throws UnsupportedOperationException thrown when trying to add same creature twice
   * @throws IllegalArgumentException thrown when creature is null
   */
  public void addCreature(ICreature creature) throws UnsupportedOperationException {
    if (creature == null) {
      throw new IllegalArgumentException("Cannot add null creature to a cell");
    }
    if (this.hasCreature(creature.getCreatureType())) {
      throw new UnsupportedOperationException("Cannot add same creature more than once");
    }
    if (this.isTunnel()) {
      throw new UnsupportedOperationException("Cannot add creature to tunnel");
    }
    this.creatures.add(creature);
  }

  public static boolean canCellKillPlayer(Cell cell) {
    return cell == null || (cell.creatures.stream().anyMatch(creature -> (creature.getCreatureType() ==
            CreatureType.PIT || creature.getCreatureType() == CreatureType.WUMPUS)));
  }

  public void markVisible() {
    this.isVisible = true;
  }

  /**
   * Returns true if this cell has been visited before.
   * @return true if this cell has been visited by a player, false otherwise.
   */
  public boolean isVisible() {
    return isVisible;
  }

  /**
   * Performs actions such as kill a player, move a player randomly to a different cell, when
   * a player is added to this cell.
   * @param player player playing the game
   * @param maze the maze in which the cell is present.
   * @throws PlayerKilledException thrown when a player is killed.
   */
  public void performCellActions(MazePlayer player, IMaze maze) throws PlayerKilledException {
    this.isVisible = true;
    MazePoint cellPos = new MazePoint(this.getRowPosition(), this.getColumnPosition());
    Collections.sort(this.creatures);
    Collections.reverse(this.creatures);
    for (ICreature creature: this.creatures) {
      if (!cellPos.equals(player.getCurrentCoordinates())) {
        return;
      }
      creature.executeAction(player, maze);
    }
  }

  /**
   * Returns true if there is a wall in the direction passes in parameter.
   * @param dir direction to be checked
   * @return true if wall is present in the direction mentioned in parameter.
   */
  private boolean hasWallInDirection(Direction dir) {
    return walls.contains(dir);
  }

  /**
   * Removes a wall in the cell if it is already present.
   * @param direction direction where wall has to be removed
   * @throws IllegalStateException thrown when wall is non-present in the direction already
   */
  public void removeWallInDirection(Direction direction) throws IllegalStateException {
    if (!hasWallInDirection((direction))) {
      throw new IllegalStateException("Trying to remove a wall when wall is not present");
    }
    if (this.creatures.size() > 0) {
      throw new IllegalStateException("Should not remove wall once creatures are added");
    }
    this.walls.remove(direction);
  }

  /**
   * Returns the list of walls surrounded by the cells.
   * @return list of walls around this cell
   */
  public List<Direction> getWalls() {
    return walls;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(" ");
    if (this.creatures != null && this.creatures.size() > 0) {
      for (ICreature creature: this.creatures) {
        sb.append(creature.getCreatureType().getCreatureName()).append(" ");
      }
    }
    return sb.toString();
  }

  /**
   * Function returns if there is a wall at the top side of the cell.
   * @return true if there is a wall at the top of the cell
   */
  public boolean hasNorth() {
    return hasWallInDirection(Direction.NORTH);
  }

  /**
   * Function returns if there is a wall at the bottom side of the cell.
   * @return true if there is a wall at the bottom of the cell
   */
  public boolean hasSouth() {
    return hasWallInDirection(Direction.SOUTH);
  }

  /**
   * Function returns if there is a wall at the west side of the cell.
   * @return true if there is a wall at the west side of the cell
   */
  public boolean hasWest() {
    return hasWallInDirection(Direction.WEST);
  }

  /**
   * Function returns if there is a wall at the east side of the cell.
   * @return true if there is a wall at the east side of the cell
   */
  public boolean hasEast() {
    return hasWallInDirection(Direction.EAST);
  }

  /**
   * Function returns if the cell is tunnel or a cave.
   * @return true if the current cell is a tunnel, false otherwise
   */
  boolean isTunnel() {
    return walls.size() == TUNNEL_CELL_WALLS;
  }

  /**
   * Function returns the directions in which a player can move. This method returns a list of
   * directions that does not have any walls and hence permit movement.
   * @return the list of directions a player can move
   */
  public List<Direction> getSuggestionsForMovement() {
    return Arrays.stream(Direction.values()).filter((wall -> !walls.contains(wall)))
            .collect(Collectors.toList());
  }
}
