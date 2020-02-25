package ru.nsu.fit.chernikov.Task_2_1_1;

import java.util.Date;

/**
 * Order is a request to produce and deliver products of specified complexity and cost to specified
 * location.
 */
public class Order {

  private double complexity;
  private long cost;
  private long orderId;
  private Date orderDate;
  private int x, y;
  private Cook cook;
  private Courier courier;


  /**
   * Order constructor.
   *
   * @param _complexity complexity of order.
   * @param _cost cost of order.
   * @param _orderId identifier of order.
   * @param _orderDate time when order was made.
   * @param xCoord x coordinate of destination.
   * @param yCoord y coordinate of destination.
   */
  public Order(
      double _complexity, long _cost, long _orderId, Date _orderDate, int xCoord, int yCoord) {
    complexity = _complexity;
    cost = _cost;
    orderDate = _orderDate;
    orderId = _orderId;
    x = xCoord;
    y = yCoord;
  }

  public double getComplexity() {
    return complexity;
  }

  public long getCost() {
    return cost;
  }

  public long getOrderId() {
    return orderId;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Cook getCook() {
    return cook;
  }

  public void setCook(Cook cook) {
    this.cook = cook;
  }

  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }
}
