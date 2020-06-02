package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

/** Utility class to work with Directions. */
public class Direction {
  public enum Values {
    UP,
    DOWN,
    LEFT,
    RIGHT
  }

  /**
   * Cast to int. Mostly used to calculate the distance.
   *
   * @param a Direction
   * @return integer representation
   */
  public static int toInt(Values a) {
    switch (a) {
      case UP:
        return 0;
      case DOWN:
        return 2;
      case LEFT:
        return 3;
      case RIGHT:
        return 1;
    }
    return -1;
  }

  /**
   * Get distance between two directions. How many 90 degrees turn need to be done.
   *
   * @param a first Direction
   * @param b second Direction
   * @return distance
   */
  public static int distance(Values a, Values b) {
    int dist = Math.abs(toInt(a) - toInt(b)) % 4;
    if (dist == 3) {
      dist = 1;
    }
    return dist;
  }
}
