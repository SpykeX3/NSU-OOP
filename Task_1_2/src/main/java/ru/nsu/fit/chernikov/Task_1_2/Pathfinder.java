package ru.nsu.fit.chernikov.Task_1_2;

/**
 * Represents a graph. Used to calculate distance between nodes.
 * Nodes are numbered starting from zero.
 * Edges must be finite and positive.
 */
public class Pathfinder {
  private int nodeCount;
  private SquareMatrix matrix;
  private boolean calculated;

  /**
   * Create new Pathfinder object. Works with positive weights only.
   *
   * @param _nodeCount graph nodes count.
   */
  public Pathfinder(int _nodeCount) {
    nodeCount = _nodeCount;
    matrix = new SquareMatrix(nodeCount);
  }

  /**
   * Add edge to graph in pathfinder.
   *
   * @param out "from" node index
   * @param in "to" node index
   * @param weight Edge weight, must be positive
   */
  public void addEdge(int out, int in, int weight) {
    if (weight < 0) throw new IllegalArgumentException();
    if (in == out) return;
    matrix.setInteger(out, in, weight);
    calculated = false;
  }

  private void floydWarshall() {
    for (int k = 0; k < nodeCount; k++) {
      for (int i = 0; i < nodeCount; i++) {
        for (int j = 0; j < nodeCount; j++) {
          Distance ijDist = matrix.getDistance(i, j);
          Distance ikDist = matrix.getDistance(i, k);
          Distance kjDist = matrix.getDistance(k, j);
          matrix.setDistance(i, j, Distance.min(ijDist, ikDist.add(kjDist)));
        }
      }
    }
  }

  /**
   * Get calculated Distance from start to finish nodes.
   *
   * @param start starting node
   * @param finish finishing node
   * @return distance between start and finish
   */
  public Distance getDistance(int start, int finish) {
    if (!calculated) {
      floydWarshall();
      calculated = true;
    }
    return matrix.getDistance(start, finish);
  }

  public String toString() {
    return matrix.toString();
  }
}
