package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

public interface WallGenerator {
  /**
   * Generates Walls object.
   *
   * @param height field height
   * @param width field width
   * @param startX snake x coord
   * @param startY snake y coord
   * @return generated Walls
   */
  Walls generate(int height, int width, int startX, int startY);
}
