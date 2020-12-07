package maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import graph.MazePoint;
import mazecreatures.CreatureType;
import player.MazePlayer;

/**
 * Container for function for rendering a grid of maze cells as ASCII art. The class also contains
 * some of the utility functions that can be used in generic scenarious. The maze is rendered
 * as a square with horizontal gaps of 3-4 spaces which represent a hallway/door. A pit in the maze
 * is printed by "PIT", bat by "BAT", Wumpus by "Wumpus" & plaeyer by "P".
 */
public class MazeUtils {

  public static final int CELL_LEFT_PADDING = 6;
  public static final int MIN_CELL_WIDTH = 4; // best to leave this at 4 or larger
  public static final int MAX_PERCENT = 100;
  public static final int BAT_PICK_PERCENTAGE = 50;

  public static String[][] renderImages(Cell[][] grid, boolean showBarriers, IMaze maze,
                                        List<MazePlayer> players) {
    String[][] cellImages = new String[grid.length][grid[0].length];
    for (int ii = 0; ii < grid.length; ii++) {
      for (int jj = 0; jj < grid[0].length; jj++) {
        Cell cell = grid[ii][jj];
        String imageUrl;
        if (!cell.isVisible() && !showBarriers) {
          imageUrl = MazeImageUtils.getCellNotVisitedImage();
        } else {
          MazePoint currentPoint = new MazePoint(ii, jj);
          if (players.stream().anyMatch(player -> player.getCurrentCoordinates()
                  .equals(currentPoint))) {
            imageUrl = MazeImageUtils.getCellPlayerImage();
          } else if (cell.isTunnel()) {
            imageUrl = MazeImageUtils.getImageForCellDirections(cell.getSuggestionsForMovement());
          } else if (cell.hasCreature(CreatureType.WUMPUS)) {
            imageUrl = MazeImageUtils.getCellWumpusImage();
          } else if (cell.hasCreature(CreatureType.PIT)) {
            imageUrl = MazeImageUtils.getCellPitImage();
          } else if (maze.checkCreatureInAdjacentCells(currentPoint, CreatureType.WUMPUS,
                  null)) {
            imageUrl = MazeImageUtils.getCellWumpusSmellImage();
          } else if (maze.checkCreatureInAdjacentCells(currentPoint, CreatureType.PIT,
                  null)) {
            imageUrl = MazeImageUtils.getCellPitSmellImage();
          } else {
            imageUrl = MazeImageUtils.getImageForCellDirections(cell.getSuggestionsForMovement());
          }
        }
        cellImages[ii][jj] = imageUrl;
      }
    }
    return cellImages;
  }

  /**
   * Renders a grid of maze cells as ASCII art. The grid is represented as an
   * array of array of Cell. Uses Cell equals method to determine players position in a maze.
   * @param grid the grid to render
   * @param players a cell representing player's current location, can be null if there is no players
   * @param showBarriers
   * @return the ASCII art representation of the maze
   */
  public static String render(Cell[][] grid, List<MazePlayer> players, boolean showBarriers) {
    if (grid == null || grid.length == 0 || grid[0].length == 0) {
      throw new IllegalArgumentException("Invalid grid");
    }
    int maxCellWidth = MIN_CELL_WIDTH;
    for (Cell[] row : grid) {
      for (Cell c : row) {
        maxCellWidth = Integer.max(maxCellWidth, c.toString().length());
      }
    }
    maxCellWidth += 6;
    int numRows = grid.length;
    int numCols = grid[0].length;
    StringBuilder sb = new StringBuilder();
    int cellWidth = maxCellWidth + CELL_LEFT_PADDING;
    // gap for opening in horizontal wall is 4 for even width, 3 for odd width
    int midLen = 4 - cellWidth % 2;
    int sideLen = (cellWidth - midLen) / 2;
    for (Cell[] cells : grid) {
      sb.append("+");
      sb.append(Arrays.stream(cells).map(c ->
          "-".repeat(sideLen)
              + (!c.hasNorth() ? " " : "-").repeat(midLen)
              + "-".repeat(sideLen)
      ).collect(Collectors.joining("+")));
      sb.append("+\n");
      for (int c = 0; c < cells.length; c++) {
        StringBuilder newSb = new StringBuilder();

        Cell cell = cells[c];
        String cellContent = cell.toString();
        MazePoint curPointNow = new MazePoint(cell.getRowPosition(), cell.getColumnPosition());
        List<MazePlayer> matchingPlayers = (players != null ? players.stream().filter(player ->
                player.getCurrentCoordinates().equals(curPointNow)).collect(Collectors.toList()) :
                new ArrayList<>());
        for (MazePlayer matchPlayer: matchingPlayers) {
          cellContent += " P" + (matchPlayer.getPlayerIndex() + 1) + " ";
        }
        newSb.append(!cell.hasWest() ? " " : "|").append(" ".repeat(CELL_LEFT_PADDING));
        String paddedCellString;
        try {
          paddedCellString = " ".repeat(maxCellWidth).substring(cellContent.length())
                  + cellContent;
        } catch (StringIndexOutOfBoundsException exp) {
          throw exp;
        }
        StringBuilder cellTemp = new StringBuilder(paddedCellString);
        newSb.append(cellTemp);
        if (c == numCols - 1 && cell.hasEast()) {
          newSb.append("|");
        }
        if (showBarriers || cell.isVisible()) {
          sb.append(newSb.toString());
        } else {
          sb.append("/".repeat(newSb.toString().length()));
        }
      }
      sb.append("\n");
    }
    sb.append("+");
    sb.append(Arrays.stream(grid[numRows - 1]).map(c ->
        "-".repeat(sideLen)
            + (!c.hasSouth() ? " " : "-").repeat(midLen)
            + "-".repeat(sideLen)
    ).collect(Collectors.joining("+"))).append("+");
    return sb.toString();
  }

  /**
   * Removes white spaces & new lines from a string.
   * @param str the string to be cleaned
   * @return the response string without whitespaces & newlines.
   */
  public static String cleanString(String str) {
    str = str.replaceAll("[\\n\\t ]", "");
    return str;
  }

  /**
   * Converts a string to int. Ex: "677" becomes 677.
   * @param inp the input string
   * @return the response.
   * @throws NumberFormatException thrown when the string cannot be converted to an integer.
   */
  public static int parseInt(String inp) throws NumberFormatException {
    return Integer.parseInt(inp);
  }
}

