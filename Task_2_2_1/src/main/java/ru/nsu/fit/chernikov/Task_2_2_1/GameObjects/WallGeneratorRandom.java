package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

import javafx.scene.paint.Color;

import java.util.Random;

/** Generates random walls. */
public class WallGeneratorRandom implements WallGenerator {

  private int wallCount;
  private Color wallColor;

  /**
   * Generator constructor.
   *
   * @param wallCount number of walls to be placed.
   * @param wallColor color of walls to be created.
   */
  public WallGeneratorRandom(int wallCount, Color wallColor) {
    this.wallCount = wallCount;
    this.wallColor = wallColor;
  }

  /**
   * Generates Walls object.
   *
   * @param height field height
   * @param width field width
   * @param startX snake x coord
   * @param startY snake y coord
   * @return generated Walls
   */
  @Override
  public Walls generate(int height, int width, int startX, int startY) {
    Random rnd = new Random();
    Walls result = new Walls(height, width, wallColor);
    for (int i = 0; i < wallCount; i++) {
      Point p;
      int x, y;
      do {
        do {
          x = rnd.nextInt(width);
          y = rnd.nextInt(height);
        } while (x == startX && y == startY);
        p = new Point(x, y, wallColor);

      } while (!result.addWall(p));
    }
    return result;
  }
}
