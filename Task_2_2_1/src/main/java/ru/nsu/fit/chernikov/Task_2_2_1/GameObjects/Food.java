package ru.nsu.fit.chernikov.Task_2_2_1.GameObjects;

public class Food extends Point {
  private int foodValue;

  public Food(Point p, int foodValue) {
    super(p.getX(), p.getY(), p.getColor());
    this.foodValue = foodValue;
  }


  public int getFoodValue() {
    return foodValue;
  }

  public void setFoodValue(int foodValue) {
    this.foodValue = foodValue;
  }
}
