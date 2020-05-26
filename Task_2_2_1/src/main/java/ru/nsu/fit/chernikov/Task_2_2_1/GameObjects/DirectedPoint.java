package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Direction;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Point;

public class DirectedPoint extends Point {
  private Direction.Values dir;

  public DirectedPoint(int x, int y, Direction.Values dir) {
    super(x, y);
    this.dir = dir;
  }

  public DirectedPoint(int x, int y, Color color, Direction.Values dir) {
    super(x, y, color);
    this.dir = dir;
  }

  public Direction.Values getDir() {
    return dir;
  }

  public void setDir(Direction.Values dir) {
    this.dir = dir;
  }
}
