package maze;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maze.Direction;

/**
 * Class that has functionality related to identifying the specific image which has to be used
 * for a particular cell.
 */
public class MazeImageUtils {

  private static final String ROOT_IMAGES_PATH = "/resources/htw_images/";

  private static Map<Set<Direction>, String> wallImages;

  private static void initNonTunnelImages() {
    wallImages = new HashMap<>();
    wallImages.put(Set.of(Direction.NORTH), ROOT_IMAGES_PATH + "N.png");
    wallImages.put(Set.of(Direction.SOUTH), ROOT_IMAGES_PATH + "S.png");
    wallImages.put(Set.of(Direction.WEST), ROOT_IMAGES_PATH + "W.png");
    wallImages.put(Set.of(Direction.EAST), ROOT_IMAGES_PATH + "E.png");
    wallImages.put(Set.of(Direction.EAST, Direction.WEST), ROOT_IMAGES_PATH + "EW.png");
    wallImages.put(Set.of(Direction.NORTH, Direction.SOUTH), ROOT_IMAGES_PATH + "NS.png");
    wallImages.put(Set.of(Direction.NORTH, Direction.EAST), ROOT_IMAGES_PATH + "NE.png");
    wallImages.put(Set.of(Direction.SOUTH, Direction.EAST), ROOT_IMAGES_PATH + "SE.png");
    wallImages.put(Set.of(Direction.SOUTH, Direction.WEST), ROOT_IMAGES_PATH + "SW.png");
    wallImages.put(Set.of(Direction.WEST, Direction.NORTH), ROOT_IMAGES_PATH + "NW.png");
    wallImages.put(Set.of(Direction.NORTH, Direction.WEST, Direction.EAST),
            ROOT_IMAGES_PATH + "NEW.png");
    wallImages.put(Set.of(Direction.NORTH, Direction.EAST, Direction.SOUTH),
            ROOT_IMAGES_PATH + "NSE.png");
    wallImages.put(Set.of(Direction.EAST, Direction.SOUTH, Direction.WEST),
            ROOT_IMAGES_PATH + "SEW.png");
    wallImages.put(Set.of(Direction.SOUTH, Direction.WEST, Direction.NORTH),
            ROOT_IMAGES_PATH + "NSW.png");
    wallImages.put(Set.of(Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH),
            ROOT_IMAGES_PATH + "NSEW.png");
  }

  public static String getImageForCellDirections(List<Direction> directions) {
    Set<Direction> sortedDirections = Set.copyOf(directions);
    if (wallImages == null) {
      initNonTunnelImages();
    }
    if (wallImages.containsKey(sortedDirections)) {
      return wallImages.get(sortedDirections);
    }
    return null;
  }
}
