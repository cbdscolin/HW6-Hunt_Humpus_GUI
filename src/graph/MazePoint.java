package graph;

import java.util.Objects;

/**
 * Class that is used to represent a point in a maze graph with X and Y coordinates.
 */
public class MazePoint {

  private final int xCoordinate;

  private final int yCoordinate;

  public MazePoint(int xCoordinate, int yCoordinate) {
    this.xCoordinate = xCoordinate;
    this.yCoordinate = yCoordinate;
  }

  public int getXCoordinate() {
    return xCoordinate;
  }

  public int getYCoordinate() {
    return yCoordinate;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("(X, Y) = (" + xCoordinate + ", ");
    sb.append(yCoordinate + ")\n");
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MazePoint point = (MazePoint) o;
    return xCoordinate == point.xCoordinate
            && yCoordinate == point.yCoordinate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(xCoordinate, yCoordinate);
  }
}
