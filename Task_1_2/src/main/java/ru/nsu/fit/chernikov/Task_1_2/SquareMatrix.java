package ru.nsu.fit.chernikov.Task_1_2;

import java.util.Arrays;

/**
 * Represents a square matrix of distances.
 * Provides storage and interface for accessing contained distances.
 * Diagonal distances equal zero by defauld.
 */
public class SquareMatrix {
  private int size;
  private Distance[][] matrix;

  /**
   * Create SquareMatrix with size sz. By default all distances ate
   *
   * @param sz size of matrix
   */
  SquareMatrix(int sz) {
    size = sz;
    matrix = new Distance[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        matrix[i][j] = new Distance();
      }
    }
    for (int i = 0; i < size; i++) {
      matrix[i][i].setWeight(0);
    }
  }

  /**
   * Get matrix size.
   *
   * @return size of matrix
   */
  public int getSize() {
    return size;
  }

  /**
   * Copy internal values from other matrix.
   *
   * @param srcMatrix source matrix
   */
  public void copyFrom(SquareMatrix srcMatrix) {
    size = srcMatrix.size;
    matrix = new Distance[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        matrix[i][j] = new Distance(srcMatrix.getDistance(i, j));
      }
    }
  }

  /**
   * Set [i][j] distance to finite value.
   *
   * @param i "from" index
   * @param j "to" index
   * @param value length of Edge
   */
  public void setInteger(int i, int j, int value) {
    matrix[i][j].setWeight(value);
  }

  /**
   * set [i][j] distance to custom value.
   *
   * @param i "from" index
   * @param j "to" index
   * @param d distance to set
   */
  public void setDistance(int i, int j, Distance d) {
    matrix[i][j] = new Distance(d);
  }

  /**
   * Get [i][j] distance
   *
   * @param i "from" index
   * @param j "to" index
   * @return distance from i to j
   */
  public Distance getDistance(int i, int j) {
    return matrix[i][j];
  }

  public String toString() {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < size; i++) {
      res.append(Arrays.toString(matrix[i])).append("\n");
    }
    return res.toString();
  }
}
