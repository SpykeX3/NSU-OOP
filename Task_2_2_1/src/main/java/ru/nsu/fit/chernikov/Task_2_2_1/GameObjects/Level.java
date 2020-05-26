package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Level extends Thread {
  private static final Color snakeColor = new Color(0.7, 0, 0.7, 1);
  private static final Color foodColor = new Color(0.2, 1, 0.2, 1);
  private static final Color wallColor = new Color(0.1, 0.1, 0.1, 1);
  public static final Color backgroundColor = new Color(0.5, 0.5, 0.5, 1);
  private Snake player;
  private List<Food> foods;
  private Walls walls;
  private int height, width;
  private int score, interval;
  private Lock gameLock;
  private Lock readyLock;
  private boolean ready;

  public Level(int height, int width, int foodCount, int interval) {
    this.interval = interval;
    this.height = height;
    this.width = width;
    walls = new Walls(height, width, wallColor);
    walls.generateWalls((height * width) / 12);
    this.gameLock = new ReentrantLock();
    this.readyLock = new ReentrantLock();
    ready = false;
    foods = new ArrayList<>();
    player = new Snake(snakeColor, width / 2, height / 2, Direction.Values.UP, height, width);
    addFoods(foodCount);
  }

  private void addFoods(int count) {
    List<Point> usedSpace = player.getPoints();
    usedSpace.addAll(foods);
    usedSpace.addAll(walls.getPoints());
    Random rnd = new Random();
    for (int i = 0; i < count; i++) {
      Food f;
      do {
        int x = rnd.nextInt(width);
        int y = rnd.nextInt(height);
        f = new Food(new Point(x, y, foodColor), 1);
      } while (usedSpace.stream().anyMatch(f::equals));
      foods.add(f);
      usedSpace.add(f);
    }
  }

  public void step() {
    if (player.isDead()) {
      return;
    }
    gameLock.lock();
    player.move();
    Point head = player.getHead();
    if (walls.getPoints().stream().anyMatch(head::equals)) {
      player.kill();
    }
    if (foods.stream().anyMatch(head::equals)) {
      List<Food> gained = foods.stream().filter(head::equals).collect(Collectors.toList());
      for (Food f : gained) {
        player.feed(f.getFoodValue());
        score += f.getFoodValue();
      }

      foods.removeIf(head::equals);
      addFoods(1);
    }
    setReady(true);
    gameLock.unlock();
  }

  boolean finished() {
    return player.isDead();
  }

  public boolean isReady() {
    boolean res;
    // readyLock.lock();
    res = ready;
    // readyLock.unlock();
    return res;
  }

  public void setReady(boolean value) {
    // readyLock.lock();
    ready = value;
    // readyLock.unlock();
  }

  public List<Point> getPoints() {
    gameLock.lock();
    List<Point> result = player.getPoints();
    result.addAll(walls.getPoints());
    result.addAll(foods);
    gameLock.unlock();
    return result;
  }

  public int getSize() {
    return 10;
  }

  public int score() {
    return score;
  }

  public void rotate(Direction.Values dir) {
    gameLock.lock();
    player.rotate(dir);
    gameLock.unlock();
  }

  public Lock getGameLock() {
    return gameLock;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  @Override
  public void run() {
    while (isReady()) {}
  }
}
