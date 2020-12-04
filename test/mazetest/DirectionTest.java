package mazetest;


import org.junit.Assert;
import org.junit.Test;

import maze.Direction;

/**
 * Class that is used to unit test the behaviour written in {@link maze.Direction}  class.
 */
public class DirectionTest {

  @Test
  public void testDirectionSequence() {
    Direction[] directions = Direction.values();
    Assert.assertEquals(4, directions.length);
    Assert.assertEquals(Direction.NORTH, directions[0]);
    Assert.assertEquals(Direction.EAST, directions[1]);
    Assert.assertEquals(Direction.SOUTH, directions[2]);
    Assert.assertEquals(Direction.WEST, directions[3]);
  }

  @Test
  public void testGetOppositeDirection() {
    Assert.assertEquals(Direction.WEST, Direction.getInverseDirection(Direction.EAST));
    Assert.assertEquals(Direction.EAST, Direction.getInverseDirection(Direction.WEST));
    Assert.assertEquals(Direction.NORTH, Direction.getInverseDirection(Direction.SOUTH));
    Assert.assertEquals(Direction.SOUTH, Direction.getInverseDirection(Direction.NORTH));
  }

}