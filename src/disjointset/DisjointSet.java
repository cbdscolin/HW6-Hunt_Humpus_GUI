package disjointset;

/**
 * Class that is used to represent the parent cell a particular cell belongs to. Two edges having
 * same parents cannot be used to create a perfect-maze.
 */
public class DisjointSet {

  private final int[] cellParent;

  /**
   * Creates an object which stores the relationship between a node and the group that is belongs
   * to in Kruskal's modified perfect maze generation algorithm.
   * @param k Number of sets to be formed
   * @throws IllegalArgumentException when number of edges passed is invalid
   */
  public DisjointSet(int k) throws IllegalArgumentException {
    if (k <= 0) {
      throw new IllegalArgumentException("Cannot create disjoint set with zero or negative "
              + "arguments");
    }
    this.cellParent = new int[k];
    for (int i = 0; i < k; i++) {
      this.cellParent[i] = -1;
    }
  }

  /**
   * Returns number of cells with no parents.
   * @return number of cells without parents
   */
  public int getUnsetParentCellCount() {
    int count = 0;
    for (int i = 0; i < this.cellParent.length; i++) {
      if (this.cellParent[i] < 0) {
        count++;
      }
    }
    return count;
  }

  /**
   * Finds a parent of a particular node in the maze.
   * @param i the node whose parent has to be found
   * @return
   */
  public int find(int i) {
    if (this.cellParent[i] < 0) {
      return i;
    }
    this.cellParent[i] = this.find(this.cellParent[i]);
    return this.cellParent[i];
  }

  /**
   * Combines two disjoint cells to have a same parent.
   * @param leftCell the first node to be combined
   * @param rightCell the second node to be combined
   */
  public void combine(int leftCell, int rightCell) {
    int leftRootCell = this.find(leftCell);
    int rightRootCell = this.find(rightCell);
    int leftWeight = this.cellParent[leftRootCell];
    int rightWeight = this.cellParent[rightRootCell];
    if (leftWeight < rightWeight) {
      this.cellParent[leftRootCell] = rightRootCell;
      this.cellParent[rightRootCell] += leftWeight;
    } else {
      this.cellParent[rightRootCell] = leftRootCell;
      this.cellParent[leftRootCell] += rightWeight;
    }
  }
}

