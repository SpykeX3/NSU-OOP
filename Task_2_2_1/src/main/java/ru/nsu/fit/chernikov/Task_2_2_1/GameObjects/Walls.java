package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/** Walls is a container of points on the field. */
public class Walls {
  private List<Point> points;
  private int height, width;
  private Color color;

  /**
   * Create empty Walls object.
   *
   * @param height field height
   * @param width field width
   * @param color color of the walls
   */
  public Walls(int height, int width, Color color) {
    this.color = color;
    this.height = height;
    this.width = width;
    points = new ArrayList<>();
  }

  /**
   * Add a single point to the Walls.
   *
   * @param p new wall
   * @return true if position was accessible and wall was added.
   */
  public boolean addWall(Point p) {
    int x = p.getX();
    int y = p.getY();
    p.setColor(color);
    if (x >= width || x < 0 || y < 0 || y >= height) {
      return false;
    }
    if (points.stream().noneMatch(p::equals)) {
      points.add(p);
      return true;
    }
    return false;
  }

  /**
   * Get all wall points.
   *
   * @return list of all walls
   */
  public List<Point> getPoints() {
    return points;
  }
}
