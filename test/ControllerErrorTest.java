import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.Random;
import gamecontroller.IMazeController;
import gamecontroller.MazeController;
import gamemodel.GameModel;
import gamemodel.IGameModel;
import maze.IMaze;
import maze.NonWrappingRoomMaze;
import maze.WrappingRoomMaze;
import mazecreatures.CreatureType;
import player.PlayerKilledException;

/**
 * Class that is used to test various invalid scenarios that can happen when
 * the controller written in {@link gamecontroller.MazeController} runs.
 */
public class ControllerErrorTest {

  private IMaze simpleMaze;

  private Map<CreatureType, Integer> percentData;

  private IMaze mediumMaze;

  private IMaze largeMaze;

  @Before
  public void setUp() throws PlayerKilledException {
    simpleMaze = new NonWrappingRoomMaze(new Random(10000), new Random(1500),
            new Random(25000), 2,  2, 0);
    mediumMaze = new WrappingRoomMaze(new Random(12000), new Random(7900),
            new Random(14000), 3,  3, 2,
            1);
    largeMaze = new WrappingRoomMaze(new Random(41000), new Random(3400),
            new Random(15300), 5,  5, 4,
            5);
  }

  @Test
  public void testMazeCreationThroughController() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("30 50 5 N");
    IGameModel model = new GameModel(simpleMaze);
    IMazeController controller = new MazeController(in, out, model);
    controller.start();
    Assert.assertEquals(true, out.toString().contains("Player Lost !!!!\n"
            + "Player killed by Wumpus!\n"
            + "+---------------+---------------+\n"
            + "|           Bat                 |\n"
            + "+---------------+------   ------+\n"
            + "|     Wumpus  P                 |\n"
            + "+---------------+---------------+"));
    //System.out.println(out);
  }

  @Test
  public void tesPlayerKilledByPit() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("100 0 5 N");
    IGameModel model = new GameModel(mediumMaze);
    IMazeController controller = new MazeController(in, out, model);
    controller.start();
    Assert.assertEquals(true, out.toString().contains("Player Lost !!!!\n"
            + "Player killed by falling in pit!"));
    //System.out.println(out);
  }

  @Test
  public void testPlayerKilledOutofArrows() throws IOException {
    StringBuffer out = new StringBuffer();
    String inputData = "20 60 1 Y s N 3 s N 3\n";
    Reader in = new StringReader(inputData);

    IGameModel model = new GameModel(mediumMaze);
    IMazeController controller = new MazeController(in, out, model);
    controller.start();
    Assert.assertEquals(true, out.toString().contains("Player is killed since arrows"
            + " have expired"));
  }

  @Test
  public void testPlayerKillsWumpus() throws IOException {
    StringBuffer out = new StringBuffer();
    String inputData = "20 60 1 Y m W s N 1\n";
    Reader in = new StringReader(inputData);
    IGameModel model = new GameModel(mediumMaze);
    IMazeController controller = new MazeController(in, out, model);
    controller.start();
    System.out.println(out.toString());
    Assert.assertEquals(true, out.toString().contains("Player Wins !!!!\n"
            + "Player kills Wumpus !!!!"));
  }

  @Test
  public void testPlayerShootWalls() throws IOException {
    StringBuffer out = new StringBuffer();
    String inputData = "20 60 1 Y s S 1\n";
    Reader in = new StringReader(inputData);

    IGameModel model = new GameModel(largeMaze);
    IMazeController controller = new MazeController(in, out, model);
    try {
      controller.start();
    } catch (Exception ignored) {
    }
    //System.out.println(out.toString());
    Assert.assertEquals(true, out.toString().contains("Cannot shoot arrow in the "
            + "direction of a wall"));
  }

  @Test
  public void testPlayerWallAgainstWalls() throws IOException {
    StringBuffer out = new StringBuffer();
    String inputData = "20 60 1 Y m S\n";
    Reader in = new StringReader(inputData);

    IGameModel model = new GameModel(largeMaze);
    IMazeController controller = new MazeController(in, out, model);
    try {
      controller.start();
    } catch (Exception ignored) {
    }
    Assert.assertEquals(true, out.toString().contains("Cannot move player"
            + " in the direction specified"));
  }

  @Test
  public void testPlayerWhiskedByBats() throws IOException {
    StringBuffer out = new StringBuffer();
    String inputData = "20 60 1 Y m E\n";
    Reader in = new StringReader(inputData);

    IGameModel model = new GameModel(largeMaze);
    IMazeController controller = new MazeController(in, out, model);
    try {
      controller.start();
    } catch (Exception ignored) {
    }
    //System.out.println(out.toString());
    Assert.assertEquals(true, out.toString().contains("Player encountered bats\n"
            + "Player was carried by the bats!!!"));
  }

  @Test
  public void testPlayerKilledByWumpus() throws IOException {
    StringBuffer out = new StringBuffer();
    String inputData = "20 60 1 Y m W m N\n";
    Reader in = new StringReader(inputData);

    IGameModel model = new GameModel(mediumMaze);
    IMazeController controller = new MazeController(in, out, model);
    try {
      controller.start();
    } catch (Exception ignored) {
    }
    //System.out.println(out.toString());
    Assert.assertEquals(true, out.toString().contains("Player Lost !!!!\n"
            + "Player killed by Wumpus!"));
  }

  @Test
  public void testPlayerSenseDraft() throws IOException {
    StringBuffer out = new StringBuffer();
    String inputData = "20 10 2 Y m E\n";
    Reader in = new StringReader(inputData);

    IGameModel model = new GameModel(largeMaze);
    IMazeController controller = new MazeController(in, out, model);
    try {
      controller.start();
    } catch (Exception ignored) {
    }
    System.out.println(out.toString());
    Assert.assertEquals(true, out.toString().contains("Player can feel draft!"));
  }

  @Test
  public void testPlayerSmellWumpus() throws IOException {
    StringBuffer out = new StringBuffer();
    String inputData = "20 10 2 Y m W\n";
    Reader in = new StringReader(inputData);

    IGameModel model = new GameModel(mediumMaze);
    IMazeController controller = new MazeController(in, out, model);
    try {
      controller.start();
    } catch (Exception ignored) {
    }
    System.out.println(out.toString());
    Assert.assertEquals(true, out.toString().contains("Player can smell Wumpus!"));
  }
}
