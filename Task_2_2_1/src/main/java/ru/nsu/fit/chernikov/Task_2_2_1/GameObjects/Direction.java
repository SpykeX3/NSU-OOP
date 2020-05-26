package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

public class Direction {
  public enum Values {
    UP,
    DOWN,
    LEFT,
    RIGHT
  }

  public static int toInt(Values a) {
    switch (a) {
      case UP:
        return 0;
      case DOWN:
        return 1;
      case LEFT:
        return 2;
      case RIGHT:
        return 3;
    }
    return -1;
  }

  public static int distance(Values a, Values b) {
    return (Math.abs(toInt(a) - toInt(b))) % 4;
  }
}
