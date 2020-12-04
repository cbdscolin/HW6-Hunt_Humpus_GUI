package maze;

/**
 * Class represents a wall between any two cells. The wall can be either vertical or horizontal.
 */
public class Wall {

  private final int rowIndex;

  private final int columnIndex;

  private final boolean isVertical;

  private boolean isRemoved;

  /**
   * Constructor which accepts the wall co-ordinates and if the wall is horizontal or not.
   * @param rowIndex x-cordinate of the wall
   * @param columnIndex y-co-ordinate of the wall
   * @param isVertical true if the wall is vertical
   */
  public Wall(int rowIndex, int columnIndex, boolean isVertical) {
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
    this.isVertical = isVertical;
    this.isRemoved = false;
  }

  /**
   * Returns x-coordinates of the wall.
   * @return x-coordinates of the wall.
   */
  public int getWallRowIndex() {
    return this.rowIndex;
  }

  /**
   * Returns y-coordinates of the wall.
   * @return y-coordinates of the wall.
   */
  public int getWallColumnIndex() {
    return this.columnIndex;
  }

  /**
   * Returns if the wall is vertical or not.
   * @return true if wall is vertical.
   */
  public boolean isVertical() {
    return this.isVertical;
  }

  /**
   * Returns if the wall is vertical or not.
   * @return true if wall is horizontal.
   */
  public boolean isHorizontal() {
    return !this.isVertical;
  }

  /**
   * Marks that the current wall has been removed and is non-existent.
   * @throws IllegalStateException thrown if the wall is already removed
   */
  public void removeWall() throws IllegalStateException {
    if (this.isRemoved) {
      throw new IllegalStateException("Cannot remove already removed wall");
    }
    this.isRemoved = true;
  }

  /**
   * Returns if the wall is removed or not.
   * @return true if the wall is removed, false otherwise
   */
  public boolean isRemoved() {
    return isRemoved;
  }
}