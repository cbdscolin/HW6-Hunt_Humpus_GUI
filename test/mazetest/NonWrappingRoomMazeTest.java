package mazetest;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import graph.MazePoint;
import maze.Direction;
import maze.IMaze;
import maze.NonWrappingRoomMaze;
import mazecreatures.CreatureType;
import mazeexceptions.RecoverableException;
import player.PlayerKilledException;
import player.PlayerKillsWumpusException;

/**
 * Class that is used to test {@link NonWrappingRoomMaze} class.
 */
public class NonWrappingRoomMazeTest {

  @Rule
  public final ExpectedException expect = ExpectedException.none();

  @Test
  public void testPerfectRoomMaze() throws PlayerKilledException {
    IMaze maze = new NonWrappingRoomMaze(new Random(1000), new Random(7000),
            new Random(12000), 4,  6, 0);
    maze.removeWalls(null, 1);
    String res = maze.printMaze();
    Assert.assertEquals("+---------------+---------------+---------------+---------------+\n"
            + "|             P |               |                               |\n"
            + "+------   ------+------   ------+------   ------+---------------+\n"
            + "|               |                                               |\n"
            + "+------   ------+---------------+------   ------+---------------+\n"
            + "|               |                                        Wumpus |\n"
            + "+------   ------+------   ------+------   ------+---------------+\n"
            + "|                               |               |               |\n"
            + "+------   ------+---------------+---------------+------   ------+\n"
            + "|                               |                               |\n"
            + "+------   ------+---------------+------   ------+------   ------+\n"
            + "|                                               |               |\n"
            + "+---------------+---------------+---------------+---------------+", res);
  }

  @Test
  public void testRemoveTooManyWalls() throws PlayerKilledException {
    expect.expect(IllegalArgumentException.class);
    expect.expectMessage("Not enough walls exist that can be removed");
    IMaze maze = new NonWrappingRoomMaze(new Random(1000), new Random(), new Random(),
            4, 7, 19);
    maze.removeWalls(null, 0);
  }

  @Test
  public void testGenerateMazeWithoutInternalWalls() throws PlayerKilledException {
    IMaze maze = new NonWrappingRoomMaze(new Random(1000), new Random(7000),
            new Random(2500), 4,  6, 15);
    maze.removeWalls(null, 1);
    String res = maze.printMaze();
    Assert.assertEquals("+---------------+---------------+---------------+-----------"
            + "----+\n"
            + "|                                                               |\n"
            + "+------   ------+------   ------+------   ------+------   ------+\n"
            + "|                                                               |\n"
            + "+------   ------+------   ------+------   ------+------   ------+\n"
            + "|        Wumpus                                                 |\n"
            + "+------   ------+------   ------+------   ------+------   ------+\n"
            + "|                                                               |\n"
            + "+------   ------+------   ------+------   ------+------   ------+\n"
            + "|                                                               |\n"
            + "+------   ------+------   ------+------   ------+------   ------+\n"
            + "|                                             P                 |\n"
            + "+---------------+---------------+---------------+---------------+", res);
  }

  @Test
  public void testCheckDaftBehaviour() throws PlayerKilledException {
    IMaze maze = new NonWrappingRoomMaze(new Random(5000), new Random(1000),
            new Random(600), 3, 8, 9);
    Map<CreatureType, Integer> percentData = new LinkedHashMap<>();
    percentData.put(CreatureType.BAT, 10);
    percentData.put(CreatureType.PIT, 40);
    maze.removeWalls(percentData, 1);
    Boolean[] expected = {false, true, false, true, true, true, false, false, true,
      false, true, false, false, true, false, true, true, false, true, true, true, false,
      false, false
    };
    List<Boolean> actual = new ArrayList<>();
    for (int ii = 0; ii < 8; ii++) {
      for (int jj = 0; jj < 3; jj++) {
        try {
          actual.add(maze.checkCreatureInAdjacentCells(new MazePoint(ii, jj),
                  CreatureType.PIT, null));
        } catch (Exception ex) {
          actual.add(Boolean.FALSE);
        }
      }
    }
    Assert.assertEquals(Arrays.asList(expected), actual);
  }

  @Test
  public void testShootArrow() throws PlayerKilledException,
          PlayerKillsWumpusException, RecoverableException {
    IMaze maze = new NonWrappingRoomMaze(new Random(5000), new Random(2000),
            new Random(600), 4, 5, 9);
    Map<CreatureType, Integer> percentData = new LinkedHashMap<>();
    percentData.put(CreatureType.BAT, 10);
    percentData.put(CreatureType.PIT, 40);
    maze.removeWalls(percentData, 1);
    System.out.println(maze.printMaze());
    Assert.assertTrue(maze.shootArrow(new MazePoint(3, 3),
            Direction.SOUTH, 3));
    Assert.assertTrue(maze.shootArrow(new MazePoint(0, 1),
            Direction.WEST, 3));
  }

  @Test
  public void testShootArrowOnWall() throws PlayerKillsWumpusException,
          PlayerKilledException, RecoverableException {
    expect.expect(RecoverableException.class);
    expect.expectMessage("Cannot shoot arrow in the direction of a wall");
    IMaze maze = new NonWrappingRoomMaze(new Random(5000), new Random(2000),
            new Random(600), 4, 5, 9);
    Map<CreatureType, Integer> percentData = new LinkedHashMap<>();
    percentData.put(CreatureType.BAT, 10);
    percentData.put(CreatureType.PIT, 40);
    maze.removeWalls(percentData, 1);
    Assert.assertTrue(maze.shootArrow(new MazePoint(2, 1),
            Direction.SOUTH, 1));
  }

  @Test
  public void testShootArrowFromTunnel() throws PlayerKillsWumpusException,
          PlayerKilledException, RecoverableException {
    expect.expect(IllegalStateException.class);
    expect.expectMessage("Cannot shoot arrow from a tunnel");
    IMaze maze = new NonWrappingRoomMaze(new Random(5000), new Random(2000),
            new Random(600), 4, 5, 9);
    Map<CreatureType, Integer> percentData = new LinkedHashMap<>();
    percentData.put(CreatureType.BAT, 10);
    percentData.put(CreatureType.PIT, 40);
    maze.removeWalls(percentData, 1);
    Assert.assertFalse(maze.shootArrow(new MazePoint(3, 1),
            Direction.WEST, 1));
  }

}