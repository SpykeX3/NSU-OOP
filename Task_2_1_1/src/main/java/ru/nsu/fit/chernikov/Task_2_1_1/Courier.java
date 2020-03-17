package ru.nsu.fit.chernikov.Task_2_1_1;

import java.util.ArrayList;
import java.util.Date;

/** Courier is a Thread that takes orders from Pizzeria warehouse and delivers it to customers. */
public class Courier extends Thread {
  private double speed;
  private int capacity, x, y;
  private Pizzeria workplace;
  private String name;
  private ArrayList<Order> trunk;

  /**
   * Courier constructor.
   *
   * @param _name name of the courier.
   * @param _speed distance per second.
   * @param cap maximum possible number of taken orders.
   */
  public Courier(String _name, double _speed, int cap) {
    if (speed < 0) {
      throw new IllegalArgumentException("speed must be positive");
    }
    if (cap < 0) {
      throw new IllegalArgumentException("capacity must be positive");
    }
    speed = _speed;
    name = _name;
    capacity = cap;
  }

  /**
   * Set Pizzeria where courier is working.
   *
   * @param workplace Pizzeria to set.
   */
  public void setWorkplace(Pizzeria workplace) {
    this.workplace = workplace;
  }

  private int getDistance(Order order) {
    return (Math.abs(order.getX() - x) + Math.abs(order.getY() - y));
  }

  private int getDistance() {
    return (Math.abs(x) + Math.abs(y));
  }

  /** Start making deliveries until end of shift. */
  @Override
  public void run() {

    // Take pizzas
    // Deliver closest pizza while not empty
    // Return to pizzeria

    workplace.log.logCourierCame(this);
    Date now = new Date();
    while (now.before(workplace.getShiftEnd())) {
      x = 0;
      y = 0;
      // Take pizzas
      trunk = workplace.fillTrunk(capacity);
      if (trunk.isEmpty()) {
        break;
      }
      for (Order order : trunk) {
        order.setCourier(this);
        workplace.log.logDeliveryTaken(this, order);
      }
      // Deliver closest pizza while not empty
      while (!trunk.isEmpty()) {
        Order closest = trunk.get(0);
        int dist = getDistance(closest);
        for (Order order : trunk) {
          int curDist = getDistance(order);
          if (curDist < dist) {
            dist = curDist;
            closest = order;
          }
        }
        try {
          Thread.sleep((long) (1000 * dist / speed));
        } catch (InterruptedException e) {
          workplace.log.logException(e);
        }
        x = closest.getX();
        y = closest.getY();
        Date delDate = new Date();
        if (workplace.getDateDiff(closest.getOrderDate(), delDate)
            <= workplace.getDelayLimit()) {
          workplace.checkout(closest.getCost());
          workplace.log.logDelivered(this, closest, true);
        } else {
          workplace.log.logDelivered(this, closest, false);
        }
        trunk.remove(closest);
      }
      // Return
      try {
        Thread.sleep((long) (1000 * getDistance() / speed));
      } catch (InterruptedException e) {
        workplace.log.logException(e);
      }
    }
    workplace.log.logCourierShiftEnd(this);
  }

  /**
   * Get courier name.
   *
   * @return name.
   */
  public String getCName() {
    return name;
  }
}
