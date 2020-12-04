package mazetest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import maze.IMaze;
import maze.WrappingRoomMaze;
import mazecreatures.CreatureType;
import player.PlayerKilledException;

/**
 * Class that is used to test wrapping room maze creation related functionality in
 * {@link maze.WrappingRoomMaze} class.
 */
public class WrappingRoomMazeTest {

  @Rule
  public final ExpectedException expect = ExpectedException.none();

  @Test
  public void testRemoveTooManyWalls() {
    expect.expect(IllegalArgumentException.class);
    expect.expectMessage("Trying to remove too many border walls");
    IMaze maze = new WrappingRoomMaze(new Random(1000), new Random(800),
            new Random(300), 3, 3, 3,
            7);
  }

  @Test
  public void testCreateValidMaze() throws PlayerKilledException {
    IMaze maze = new WrappingRoomMaze(new Random(1000), new Random(300),
            new Random(7000), 5, 3, 0,
            2);
    maze.removeWalls(null, 1);
    String res = maze.printMaze();
    Assert.assertEquals("+------   ------+---------------+---------------+---------"
            + "------+---------------+\n"
            + "|               |                                                             P |\n"
            + "+------   ------+------   ------+---------------+---------------+---------------+\n"
            + "|                                        Wumpus                                 |\n"
            + "+------   ------+---------------+------   ------+---------------+------   ------+\n"
            + "                                |               |                               \n"
            + "+------   ------+---------------+---------------+---------------+---------------+",
            res);
  }

  @Test
  public void testCreateValidMazeWithPits() throws PlayerKilledException {
    IMaze maze = new WrappingRoomMaze(new Random(5000), new Random(1000),
            new Random(6500), 4, 3, 1,
            0);
    Map<CreatureType, Integer> percentData = new LinkedHashMap<>();
    percentData.put(CreatureType.BAT, 25);
    percentData.put(CreatureType.PIT, 25);
    maze.removeWalls(percentData, 1);
    String res = maze.printMaze();
    Assert.assertEquals("+-------------------+-------------------+-----------------"
            + "--+-------------------+\n"
            + "|               Pit |                   |                                       |\n"
            + "+--------   --------+--------   --------+--------   --------+--------   --------+\n"
            + "|            Bat  P                 Bat |        Wumpus Pit                     |\n"
            + "+--------   --------+--------   --------+--------   --------+--------   --------+\n"
            + "|                   |                                       |                   |\n"
            + "+-------------------+-------------------+-------------------+-------------------+",
            res);
  }

}