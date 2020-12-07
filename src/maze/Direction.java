package maze;

import java.util.Arrays;
import java.util.List;

import graph.MazePoint;

/**
 * Class that is used to represent the 4 directions - North, East, South and West.
 */
public enum Direction {

  NORTH("North", -1, 0),
  EAST("East", 0, 1),
  SOUTH("South", 1, 0),
  WEST("West", 0, -1);

  private final String directionString;

  private final MazePoint movementPoint;

  Direction(String dir, int xMovement, int yMovement) {
    this.directionString = dir;
    this.movementPoint = new MazePoint(xMovement, yMovement);
  }

  /**
   * Returns the co-ordinates to the current points after moving by 1 step in a specified
   * direction.
   * @param point old point
   * @return the final point after movement
   */
  public MazePoint getNextPoint(MazePoint point) {
    MazePoint newPoint = new MazePoint(point.getXCoordinate() + this.getMovementPoint()
            .getXCoordinate(),
            point.getYCoordinate() + this.getMovementPoint()
                    .getYCoordinate());
    return newPoint;

  }

  public MazePoint getMovementPoint() {
    return movementPoint;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Direction: ");
    sb.append(directionString);
    return sb.toString();
  }

  public String getDirectionString() {
    return directionString;
  }

  /**
   * Returns the opposite direction. For instance if the direction in parameter is North, the
   * returned direction is south, similarly if the direction in parameter is East, the returned
   * direction is West.
   * @param direction the direction to be reversed
   * @return the inverse direction
   */
  public static Direction getInverseDirection(Direction direction) {
    List<Direction> allDirections = Arrays.asList(Direction.values());
    int pos = allDirections.indexOf(direction);
    pos = (pos + 2) % Direction.values().length;
    return allDirections.get(pos);
  }
}
