package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Snake {

  private LinkedList<DirectedPoint> points;
  private int foodInside;
  private Color color;
  private boolean dead;
  private int height, width;

  public Snake(Color color, int x, int y, Direction.Values dir, int height, int width) {
    this.width = width;
    this.height = height;
    this.color = color;
    points = new LinkedList<>();
    points.add(new DirectedPoint(x, y,color, dir));
    foodInside = 0;
    dead = false;
  }

  public void feed(int foodCount) {
    foodInside += foodCount;
  }

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
      DirectedPoint newHead = new DirectedPoint(x, y, color,head.getDir());
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

  public boolean rotate(Direction.Values dir) {
    DirectedPoint head = points.peekFirst();
    if (head == null) return false;
    //if (Direction.distance(head.getDir(), dir) <= 1) {
      head.setDir(dir);
      return true;
   // }
    //return false;
  }

  public boolean isDead() {
    return dead;
  }

  public void kill() {
    dead = true;
    for (Point p: points) {
        p.setColor(new Color(1,0,0,1));
    }
  }

  public List<Point> getPoints() {
    return points.stream().map(x -> (Point) x).collect(Collectors.toList());
  }

  public Point getHead() {
    return points.getFirst();
  }
}
