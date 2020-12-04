package mazetest;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import maze.Cell;
import maze.Direction;

/**
 * Unit tests the functionality present in {@link Cell} class.
 */
public class CellTest {

  @Test
  public void testGetSuggestionsSomeAvailable() {
    Cell cell = new Cell(0, 1);
    cell.removeWallInDirection(Direction.NORTH);

    List<Direction> validMoves = cell.getSuggestionsForMovement();
    Assert.assertEquals(1, validMoves.size());
    Assert.assertTrue(validMoves.contains(Direction.NORTH));
  }

  @Test
  public void testGetSuggestionsNoneAvailable() {
    Cell cell = new Cell(0, 1);

    List<Direction> validMoves = cell.getSuggestionsForMovement();
    Assert.assertEquals(0, validMoves.size());
  }

  @Test
  public void testGetSuggestionsAllAvailable() {
    Cell cell = new Cell(0, 1);
    cell.removeWallInDirection(Direction.NORTH);
    cell.removeWallInDirection(Direction.SOUTH);
    cell.removeWallInDirection(Direction.EAST);
    cell.removeWallInDirection(Direction.WEST);

    List<Direction> validMoves = cell.getSuggestionsForMovement();
    Assert.assertEquals(4, validMoves.size());
    validMoves.containsAll(Arrays.asList(Direction.values()));
  }

}