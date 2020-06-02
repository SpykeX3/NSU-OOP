package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/** Snake can move, eat and die. Is controlled by changing the direction while moving. */
public class Snake {

  private LinkedList<DirectedPoint> points;
  private int foodInside;
  private Color color;
  private boolean dead;
  private int height, width;

  /**
   * Snake constructor.
   *
   * @param color Color of this snake.
   * @param x x coord
   * @param y y coord
   * @param dir initial direction
   * @param height field height
   * @param width width height
   */
  public Snake(Color color, int x, int y, Direction.Values dir, int height, int width) {
    this.width = width;
    this.height = height;
    this.color = color;
    points = new LinkedList<>();
    points.add(new DirectedPoint(x, y, color, dir));
    foodInside = 0;
    dead = false;
  }

  /**
   * Feed snake some food to make it grow.
   *
   * @param foodCount number of new cells for the snakes.
   */
  public void feed(int foodCount) {
    foodInside += foodCount;
  }

  /** Move alive snake by one point in current direction. */
  public void move() {
    if (!dead) {
      DirectedPoint head = points.peekFirst();
      if (head == null) return;
      int x = head.getX();
      int y = head.getY();
      switch (head.getDir()) {
        case UP:
          y--;
          break;
        case DOWN:
          y++;
          break;
        case LEFT:
          x--;
          break;
        case RIGHT:
          x++;
          break;
      }
      DirectedPoint newHead = new DirectedPoint(x, y, color, head.getDir());
      points.addFirst(newHead);
      if (foodInside == 0) {
        points.removeLast();
      } else {
        foodInside--;
      }
      if (points.stream().filter(newHead::equals).count() > 1) {
        kill();
      }
    }
  }

  /**
   * Rotate the head if the snake. Allows controlling its movement.
   *
   * @param dir new direction
   * @return true
   */
  public boolean rotate(Direction.Values dir) {
    DirectedPoint head = points.peekFirst();
    if (head == null) return false;
    // if (Direction.distance(head.getDir(), dir) <= 1) {
    head.setDir(dir);
    return true;
    // }
    // return false;
  }

  /**
   * Checks is snake is dead.
   *
   * @return true if dead. false if not.
   */
  public boolean isDead() {
    return dead;
  }

  /** Make the snake dead. */
  public void kill() {
    dead = true;
    for (Point p : points) {
      p.setColor(new Color(1, 0, 0, 1));
    }
  }

  /**
   * Get all points of the snake.
   *
   * @return list of all points.
   */
  public List<Point> getPoints() {
    return points.stream().map(x -> (Point) x).collect(Collectors.toList());
  }

  /**
   * Get point representing snake head.
   *
   * @return snake head
   */
  public Point getHead() {
    return points.getFirst();
  }
}
