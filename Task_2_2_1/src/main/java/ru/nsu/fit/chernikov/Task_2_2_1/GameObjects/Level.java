package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/** Level class is used to store and process the state of the game. */
public class Level {
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

  /**
   * Level constructor.
   *
   * @param height height of the game field
   * @param width width of the game field
   * @param foodCount food number to exist simultaneously
   * @param interval time between frames
   */
  public Level(int height, int width, int foodCount, int interval) {
    this.interval = interval;
    this.height = height;
    this.width = width;
    int startX = width / 2;
    int startY = height / 2;
    WallGenerator generator = new WallGeneratorRandom((height * width) / 11, wallColor);
    walls = generator.generate(height, width, startX, startY);
    this.gameLock = new ReentrantLock();
    this.readyLock = new ReentrantLock();
    ready = false;
    foods = new ArrayList<>();
    player = new Snake(snakeColor, startX, startY, Direction.Values.UP, height, width);
    addFoods(foodCount);
  }

  /**
   * Generate food on the map.
   *
   * @param count number of food points to be generated.
   */
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

  /** Simulate a single game step. */
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
    if (head.getX() < 0 || head.getX() >= width || head.getY() < 0 || head.getY() >= height) {
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
    gameLock.unlock();
  }

  /**
   * Tells if game is finished.
   *
   * @return true if finished, false otherwise.
   */
  boolean finished() {
    return player.isDead();
  }

  /**
   * Get all points on the map.
   *
   * @return list of all points.
   */
  public List<Point> getPoints() {
    gameLock.lock();
    List<Point> result = player.getPoints();
    result.addAll(walls.getPoints());
    result.addAll(foods);
    gameLock.unlock();
    return result;
  }

  /**
   * Get size of one point.
   *
   * @return point size
   */
  public int getSize() {
    return 10;
  }

  /**
   * Get current player score.
   *
   * @return game score
   */
  public int score() {
    return score;
  }

  /**
   * Rotate the player snake.
   *
   * @param dir new direction
   */
  public void rotate(Direction.Values dir) {
    gameLock.lock();
    player.rotate(dir);
    gameLock.unlock();
  }

  /**
   * Get game lock. Can be used to pause the game.
   *
   * @return game lock
   */
  public Lock getGameLock() {
    return gameLock;
  }

  /**
   * Get field height.
   *
   * @return field height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get field width.
   *
   * @return field width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get time in ms between frames.
   *
   * @return time interval between frames
   */
  public int getInterval() {
    return interval;
  }
}
