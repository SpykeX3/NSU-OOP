package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

/** Food is a Point with food value. */
public class Food extends Point {
  private int foodValue;

  /**
   * Food constructor
   *
   * @param p point
   * @param foodValue food value
   */
  public Food(Point p, int foodValue) {
    super(p.getX(), p.getY(), p.getColor());
    this.foodValue = foodValue;
  }

  /**
   * Getter for food value
   *
   * @return food value
   */
  public int getFoodValue() {
    return foodValue;
  }

  /**
   * Setter for food value
   *
   * @param foodValue food value
   */
  public void setFoodValue(int foodValue) {
    this.foodValue = foodValue;
  }
}
