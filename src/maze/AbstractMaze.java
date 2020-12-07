package maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import disjointset.DisjointSet;
import graph.MazePoint;
import mazecreatures.BatAction;
import mazecreatures.CreatureType;
import mazecreatures.ICreature;
import mazecreatures.MazeCreature;
import mazecreatures.PitAction;
import mazecreatures.WumpusAction;
import mazeexceptions.RecoverableException;
import player.MazePlayer;
import player.PlayerKilledException;


/**
 * Class that is used to represent a general maze that can be perfect, non perfect (wrapping or
 * non-wrapping room maze).
 */
public abstract class AbstractMaze implements IMaze {

  private static final double GOLD_PROBABILITY = 0.2;

  private static final double THIEF_PROBABILITY = 0.1;

  protected final Cell[][] cells;

  protected final Random wallGenerator;

  private final Random adversaryGenerator;

  private final Random movementGenerator;

  private boolean wallsRemoved;

  protected final Wall[] walls;

  private List<MazePlayer> players;

  private int nextPlayerIndex;

  private final int playerCount;

  /**
   * Constructor that accepts random number generators, rows, column count in maze, start &
   * end co-ordinates. Initializes the number of cells in the maze as specified by rows and
   * columns.
   * @param extraWallsToRemove extra walls that have to be removed
   * @param wallGenerator random number generator for Kruskal's algorithm
   * @param movementGenerator  random number generator to help with bat moving a player
   *                            to random cell
   * @param totalColumns number of columns in maze
   * @param totalRows number of rows in maze
   * @param playerCount
   * @throws IllegalArgumentException thrown when invalid null generators, total rows & columns
   *        are used and when player count is less than 1
   */
  protected AbstractMaze(Random wallGenerator, Random adversaryGenerator,
                         Random movementGenerator, int totalColumns, int totalRows, int playerCount) throws
          IllegalArgumentException {
    if (totalColumns <= 0 || totalRows <= 0) {
      throw new IllegalArgumentException("Number of rows and columns in a maze can't be negative");
    }
    if (wallGenerator == null || adversaryGenerator == null || movementGenerator == null) {
      throw new IllegalArgumentException("Bat, adversary or movement Generators can't be null\n");
    }
    if (playerCount < 1) {
      throw new IllegalArgumentException("Number of players can't be less than zero");
    }
    this.wallGenerator = wallGenerator;
    this.adversaryGenerator = adversaryGenerator;
    this.movementGenerator = movementGenerator;
    this.cells = new Cell[totalRows][totalColumns];
    this.walls = new Wall[this.minimumWallsToRemove()];
    this.wallsRemoved = false;
    this.players = new ArrayList<>(playerCount);
    this.playerCount = playerCount;
    this.nextPlayerIndex = 0;
    initCells();
  }

  @Override
  public String printMaze(boolean showBarriers) {
    return MazeUtils.render(cells, players, showBarriers);
  }

  private void initCells() {
    int rows = getTotalRows();
    int columns = getTotalColumns();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        double value = adversaryGenerator.nextInt((rows * columns));
        this.cells[i][j] = new Cell(i, j);
      }
    }
  }

  @Override
  public void initWallsForMaze() {
    int rows = getTotalRows();
    int columns = getTotalColumns();
    int wallCount = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (i < rows - 1) {
          this.walls[wallCount] = new Wall(i, j, false);
          wallCount += 1;
        }
        if (j < columns - 1) {
          this.walls[wallCount] = new Wall(i, j, true);
          wallCount += 1;
        }
      }
    }
  }

  @Override
  public boolean isWrappingMaze() {
    return false;
  }

  /**
   * Gets the next index for a given row. If the maze is wrapping then modulus is used.
   * @param row current index of row
   * @return  next index of row.
   */
  public int getNextRowIndex(int row) {
    int totalRows = getTotalRows();
    if (isWrappingMaze()) {
      row =  ( row + 1 ) % totalRows;
    } else {
      row = row + 1;
    }
    return row;
  }

  public String[][] getImagesToDisplayInCells(boolean showBarriers) {
    return MazeUtils.renderImages(cells, showBarriers, this, players);
  }

    /**
     * Gets the next index for a given column. If the maze is wrapping then modulus is used.
     * @param column current index of column
     * @return next index of column.
     */
  public int getNextColumnIndex(int column) {
    int totalCols = getTotalColumns();
    if (isWrappingMaze()) {
      column =  ( column + 1 ) % totalCols;
    } else {
      column = column + 1;
    }
    return column;
  }

  protected void removeWallHelper(Wall wallToBreak) throws IllegalStateException {
    int row = wallToBreak.getWallRowIndex();
    int col = wallToBreak.getWallColumnIndex();
    int nextColIndex = getNextColumnIndex(col);
    int nextRowIndex = getNextRowIndex(row);

    Cell currentCell = this.cells[row][col];
    Cell nextCell;
    if (wallToBreak.isVertical()) {
      nextCell = this.cells[row][nextColIndex];
    } else {
      nextCell = this.cells[nextRowIndex][col];
    }
    if (wallToBreak.isVertical()) {
      currentCell.removeWallInDirection(Direction.EAST);
      nextCell.removeWallInDirection(Direction.WEST);
    } else if (wallToBreak.isHorizontal()) {
      currentCell.removeWallInDirection(Direction.SOUTH);
      nextCell.removeWallInDirection(Direction.NORTH);
    }
    wallToBreak.removeWall();
  }

  private void initPlayerPosition(int arrowCount) throws IllegalStateException,
          IllegalArgumentException, PlayerKilledException {
    if (arrowCount <= 0) {
      throw new IllegalArgumentException("Arrow count cannot be less than zero");
    }
    int totalRow = getTotalRows();
    int totalCol = getTotalColumns();

    for (int iPlayerTurn = 0; iPlayerTurn < this.playerCount; iPlayerTurn++) {
      List<Cell> availableCells = getNonTunnelCells().stream()
              .filter(cell -> !Cell.canCellKillPlayer(cell))
              .collect(Collectors.toList());
      if (availableCells.size() <= 0) {
        throw new IllegalStateException("No cells present to place a player without killing"
                + " them");
      }

      int randomCellIndex = this.movementGenerator.nextInt(availableCells.size());
      Cell playerCell = availableCells.get(randomCellIndex);
      int playerStartX = playerCell.getRowPosition();
      int playerStartY = playerCell.getColumnPosition();

      if (playerStartX < 0 || playerStartX >= totalRow) {
        throw new IllegalStateException("X start co-ordinates for player are invalid");
      }
      if (playerStartY < 0 || playerStartY >= totalCol) {
        throw new IllegalStateException("Y start co-ordinates for player are invalid");
      }
      if (this.cells[playerStartX][playerStartY].isTunnel()) {
        throw new IllegalStateException("Cannot add player to a tunnel");
      }
      this.players.add(new MazePlayer(playerStartX, playerStartY, arrowCount, iPlayerTurn));
      this.performInitCellAction(playerCell);
    }
  }

  private void performInitCellAction(Cell playerCell) throws PlayerKilledException {
    playerCell.performCellActions(players.get(this.nextPlayerIndex), this);
    changePlayerTurn();
  }

  public boolean checkCreatureInAdjacentCells(MazePoint point, CreatureType creatureType,
                                              Direction exclude)
          throws IllegalStateException, IllegalArgumentException {
    return this.cellTraverseHelper(point, creatureType, exclude);
  }

  private boolean cellTraverseHelper(MazePoint point, CreatureType creatureType,
                                  Direction exclude) throws
          IllegalStateException, IllegalArgumentException {
    point = wrapPoint(point);
    Cell currentCell = this.cells[point.getXCoordinate()][point.getYCoordinate()];
    if (currentCell.isTunnel() && exclude == null) {
      throw new IllegalArgumentException("Checking daft/smell through a tunnel without excluding "
              + "default direction");
    }
    if (!currentCell.isTunnel() && (exclude != null)) {
      return currentCell.hasCreature(creatureType);
    }
    List<Direction> validDirections = currentCell.getSuggestionsForMovement();
    for (Direction dir: validDirections) {
      if (dir == exclude) {
        continue;
      }
      MazePoint nextPoint = dir.getNextPoint(point);
      try {
        nextPoint = wrapPoint(nextPoint);
      } catch (IllegalArgumentException excep) {
        throw new IllegalStateException("Exception in check daft/smell:: "
                + excep.getMessage());
      }
      if (cellTraverseHelper(nextPoint, creatureType,
              Direction.getInverseDirection(dir))) {
        return true;
      }
    }
    return false;
  }

  private Cell getCellAtDistance(MazePoint point,
                                 Direction arrowDirection, int distance, boolean markCellAsVisited) {
    Cell currentCell = this.cells[point.getXCoordinate()][point.getYCoordinate()];
    if (markCellAsVisited) {
      currentCell.markVisible();
    }
    if (!currentCell.isTunnel()) {
      distance = distance - 1;
    }
    if (distance == 0) {
      return currentCell;
    }
    List<Direction> validDirections = currentCell.getSuggestionsForMovement();
    MazePoint nextPoint;
    if (!currentCell.isTunnel()) {
      if (!validDirections.contains(arrowDirection)) {
        return null;
      }
      nextPoint = arrowDirection.getNextPoint(point);
      nextPoint = wrapPoint(nextPoint);
      return getCellAtDistance(nextPoint, arrowDirection, distance, markCellAsVisited);
    }
    Direction revDirection = Direction.getInverseDirection(arrowDirection);
    List<Direction> nextDirections = validDirections.stream()
            .filter(dir -> (!dir.equals(revDirection))).collect(Collectors.toList());
    if (nextDirections.size() != 1) {
      throw new IllegalStateException("Too many or too less direction present to continue "
              + "arrow movement through tunnel");
    }
    nextPoint = nextDirections.get(0).getNextPoint(point);
    nextPoint = wrapPoint(nextPoint);
    return getCellAtDistance(nextPoint, nextDirections.get(0), distance, markCellAsVisited);
  }

  private void changePlayerTurn() {
    this.nextPlayerIndex = (this.nextPlayerIndex + 1) % this.playerCount;
  }

  @Override
  public boolean shootArrow(MazePoint point, Direction dir, int power)
          throws IllegalStateException, IllegalArgumentException, RecoverableException,
          PlayerKilledException {
    Cell currentCell = this.cells[point.getXCoordinate()][point.getYCoordinate()];
    if (currentCell.isTunnel()) {
      throw new IllegalStateException("Cannot shoot arrow from a tunnel");
    }
    List<Direction> validDirections = currentCell.getSuggestionsForMovement();
    if (!validDirections.contains(dir)) {
      throw new RecoverableException("Cannot shoot arrow in the direction of a wall");
    }
    Cell finalCell = this.getCellAtDistance(point, dir, power + 1, false);
    if (finalCell != null && finalCell.hasCreature(CreatureType.WUMPUS)) {
      return true;
    }
    players.get(nextPlayerIndex).reduceArrowCount();
    changePlayerTurn();
    return false;
  }

  @Override
  public void removeWalls(Map<CreatureType, Integer> percentages, int totalPlayerArrows)
          throws UnsupportedOperationException,
          IllegalArgumentException, PlayerKilledException {
    if (this.wallsRemoved) {
      throw new UnsupportedOperationException("Remove walls should be called only once");
    }
    int totalRow = getTotalRows();
    int totalCol = getTotalColumns();

    int totalCells = totalRow * totalCol;
    DisjointSet disjointSet = new DisjointSet(totalCells);

    while (disjointSet.getUnsetParentCellCount() > 1) {
      Wall wallToBreak = this.walls[this.wallGenerator.nextInt(this.walls.length)];
      if (wallToBreak.isRemoved()) {
        continue;
      }

      int row = wallToBreak.getWallRowIndex();
      int col = wallToBreak.getWallColumnIndex();
      int cellOneIndex = row * totalCol + col;
      int cellTwoIndex;

      if (wallToBreak.isVertical()) {
        int nextColIndex = getNextColumnIndex(col);
        cellTwoIndex = row * totalCol + nextColIndex;
      } else {
        int nextRowIndex = getNextRowIndex(row);
        cellTwoIndex = nextRowIndex * totalCol + col;
      }
      if (disjointSet.find(cellOneIndex) != disjointSet.find(cellTwoIndex)) {
        removeWallHelper(wallToBreak);
        disjointSet.combine(cellOneIndex, cellTwoIndex);
      }
    }
    this.wallsRemoved = true;
  }

  @Override
  public List<Cell> getNonTunnelCells() {
    return getSpecificCells((Cell cell) -> !cell.isTunnel());
  }

  private List<Cell> getSpecificCells(Predicate<Cell> predicate) {
    int row = getTotalRows();
    List<Cell> nonTunnelCells = new ArrayList<>();
    for (int ii = 0; ii < row; ii++) {
      List<Cell> tempTunnelCells = Arrays.stream(this.cells[ii]).filter(predicate)
              .collect(Collectors.toList());
      nonTunnelCells.addAll(tempTunnelCells);
    }
    return nonTunnelCells;
  }

  private void addSingleCreature(CreatureType creatureToAdd,
                                 List<Cell> availableCells, int numCreaturesToAdd) {
    int currentAdded =  0;
    ICreature creatureInstance = null;
    while (currentAdded < numCreaturesToAdd) {
      int cellToAddTo = this.adversaryGenerator.nextInt(availableCells.size());
      Cell chosenCell = availableCells.remove(cellToAddTo);
      if (creatureToAdd == CreatureType.BAT) {
        creatureInstance = new MazeCreature(CreatureType.BAT, new BatAction());
      }
      else if (creatureToAdd == CreatureType.PIT) {
        creatureInstance = new MazeCreature(CreatureType.PIT, new PitAction());
      } else if (creatureToAdd == CreatureType.WUMPUS) {
        creatureInstance = new MazeCreature(CreatureType.WUMPUS, new WumpusAction());
      }
      chosenCell.addCreature(creatureInstance);
      currentAdded += 1;
    }
  }

  private void addWumpus() throws IllegalStateException {
    List<Cell> availableCells = getNonTunnelCells();
    if (availableCells.size() < CreatureType.WUMPUS.getCreatureTotalOccurrences()) {
      throw new IllegalStateException("No cells present where Wumpus can be added, game"
              + " cannot be played\n");
    }
    addSingleCreature(CreatureType.WUMPUS, availableCells,
            CreatureType.WUMPUS.getCreatureTotalOccurrences());
  }

  protected void addCreaturesToCells(Map<CreatureType, Integer> percentages, int arrowCount) throws
          IllegalArgumentException, UnsupportedOperationException, IllegalStateException,
          PlayerKilledException {
    if (percentages == null || percentages.isEmpty()) {
      addWumpus();
      initPlayerPosition(arrowCount);
      return;
    }
    for (Map.Entry<CreatureType, Integer> mapPair : percentages.entrySet()) {
      if (mapPair.getValue() < 0 || mapPair.getValue() > MazeUtils.MAX_PERCENT) {
        throw new IllegalArgumentException( mapPair.getKey().getCreatureName()
                + " percentage is not between 0 and 100");
      }
    }
    addWumpus();
    for (Map.Entry<CreatureType, Integer> mapPair : percentages.entrySet()) {
      List<Cell> availableCells = getNonTunnelCells();
      int numCreaturesToAdd = availableCells.size() *  mapPair.getValue() / MazeUtils.MAX_PERCENT;
      addSingleCreature(mapPair.getKey(), availableCells, numCreaturesToAdd);
    }
    initPlayerPosition(arrowCount);
  }

  @Override
  public int getTotalRows() {
    return this.cells.length;
  }

  @Override
  public int getTotalColumns() {
    return this.cells[0].length;
  }

  @Override
  public void movePlayerInDirection(Direction direction) throws IllegalArgumentException,
          IllegalStateException, PlayerKilledException, RecoverableException {
    MazePlayer playerToMove = players.get(nextPlayerIndex);
    MazePoint currentPoint = playerToMove.getCurrentCoordinates();
    Cell nextCell = this.getCellAtDistance(currentPoint, direction,2, true);
    if (nextCell == null) {
      throw new RecoverableException("Cannot move player in the direction specified");
    }
    MazePoint newPosition = new MazePoint(nextCell.getRowPosition(), nextCell.getColumnPosition());
    newPosition = wrapPoint(newPosition);
    playerToMove.setNewPosition(newPosition);
    Cell newCell = cells[newPosition.getXCoordinate()][newPosition.getYCoordinate()];
    newCell.performCellActions(playerToMove, this);
    changePlayerTurn();
  }

  private MazePoint wrapPoint(MazePoint point) throws IllegalStateException {
    if (!isWrappingMaze()) {
      if (point.getYCoordinate() < 0 || point.getYCoordinate() >= getTotalColumns()) {
        throw new IllegalArgumentException("Column beyond maze maximum column size while walking");
      }
      if (point.getXCoordinate() < 0 || point.getXCoordinate() >= getTotalRows()) {
        throw new IllegalArgumentException("Row beyond maze maximum row size while walking");
      }
    } else {
      int rows = getTotalRows();
      int cols = getTotalColumns();
      int newX = point.getXCoordinate() % getTotalRows();
      int newY = point.getYCoordinate() % getTotalColumns();
      while (newX < 0) {
        newX = (newX + rows) % rows;
      }
      while (newY < 0) {
        newY = (newY + cols) % cols;
      }
      return new MazePoint(newX, newY);
    }
    return point;
  }

  @Override
  public int minimumWallsToRemove() {
    int rows = getTotalRows();
    int cols = getTotalColumns();
    return (rows - 1) * cols + (cols - 1) * rows;
  }

  @Override
  public MazePoint getExpectedMovementPosition(MazePoint point, Direction dir) {
    if (cells[point.getXCoordinate()][point.getYCoordinate()].isTunnel()) {
      throw new UnsupportedOperationException("Cannot measure distance from a tunnel cell");
    }
    Cell cell = getCellAtDistance(point, dir, 2, false);
    if (cell != null) {
      return wrapPoint(new MazePoint(cell.getRowPosition(), cell.getColumnPosition()));
    }
    return null;
  }

  @Override
  public boolean resultingCellHasCreature(MazePoint point, Direction dir,
                                          CreatureType creatureType, int distance) {
    if (cells[point.getXCoordinate()][point.getYCoordinate()].isTunnel()) {
      throw new UnsupportedOperationException("Cannot find distance from a tunnel cell");
    }
    if (distance <= 0) {
      throw new UnsupportedOperationException("Distance from the current cell should"
              + " be non-zero");
    }
    if (dir == null) {
      throw new UnsupportedOperationException("Direction cannot be null when determining "
              + "distance from a position");
    }
    Cell cell = getCellAtDistance(point, dir, distance, false);
    return cell != null && cell.hasCreature(creatureType);
  }

  @Override
  public String toString() {
    return MazeUtils.render(cells, players, true);
  }

  public Random getMovementGenerator() {
    return this.movementGenerator;
  }

  @Override
  public boolean allRequestedWallsRemoved() {
    return wallsRemoved;
  }

  @Override
  public MazePoint getActivePlayerCoordinates() {
    return players.get(nextPlayerIndex).getCurrentCoordinates();
  }

  @Override
  public List<Direction> getValidDirectionsForMovement() {
    MazePoint playerPosition = this.players.get(nextPlayerIndex).getCurrentCoordinates();
    Cell currentCell = this.cells[playerPosition.getXCoordinate()][playerPosition.getYCoordinate()];
    return currentCell.getSuggestionsForMovement();
  }

  @Override
  public int getActivePlayerIndex() {
    return this.nextPlayerIndex;
  }
}
