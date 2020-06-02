package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Direction;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Point;

/** Class for storing Point and Direction. */
public class DirectedPoint extends Point {
  private Direction.Values dir;

  /**
   * Constructoor without color
   *
   * @param x x coord
   * @param y y coord
   * @param dir direction
   */
  public DirectedPoint(int x, int y, Direction.Values dir) {
    super(x, y);
    this.dir = dir;
  }

  /**
   * Constructor with color
   *
   * @param x x coord
   * @param y y coord
   * @param color color of point
   * @param dir direction
   */
  public DirectedPoint(int x, int y, Color color, Direction.Values dir) {
    super(x, y, color);
    this.dir = dir;
  }

  /**
   * Getter for direction
   *
   * @return direction
   */
  public Direction.Values getDir() {
    return dir;
  }

  /**
   * Setter of direction
   *
   * @param dir new direction
   */
  public void setDir(Direction.Values dir) {
    this.dir = dir;
  }
}
