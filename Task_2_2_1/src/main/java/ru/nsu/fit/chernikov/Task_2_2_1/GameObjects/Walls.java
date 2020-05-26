package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Walls {
  private List<Point> points;
  private int height, width;
  private Color color;

  public Walls(int height, int width, Color color) {
    this.color = color;
    this.height = height;
    this.width = width;
    points = new ArrayList<>();
  }

  public void addWall(Point p) {
    int x = p.getX();
    int y = p.getY();
    p.setColor(color);
    if (x >= width || x < 0 || y < 0 || y >= height) {
      return;
    }
    if (points.stream().noneMatch(p::equals)) {
      points.add(p);
    }
  }

  public void generateWalls(int wallCount) {
    Random rnd = new Random();
    for (int i = 0; i < wallCount; i++) {
      Point p;
      do {
        int x = rnd.nextInt(width);
        int y = rnd.nextInt(height);
        p = new Point(x, y, color);
      } while (points.stream().anyMatch(p::equals)
          || (p.getX() == width / 2 && p.getY() == height / 2));
      points.add(p);
    }
  }

  public List<Point> getPoints() {
    return points;
  }
}
