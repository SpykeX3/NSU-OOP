package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;

/** Point is a combination of position and optional color. */
public class Point {
  int x, y;
  Color color;

  /**
   * Constructor without color.
   *
   * @param x x coord
   * @param y y coord
   */
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Constructor with color.
   *
   * @param x x coord
   * @param y y coord
   * @param color color of the point
   */
  public Point(int x, int y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Compares positions of two points.
   *
   * @param other another point
   * @return true if their corresponding coordinates are equal.
   */
  public boolean equals(Point other) {
    return x == other.x && y == other.y;
  }
}
