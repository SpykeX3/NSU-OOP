package ru.nsu.fit.chernikov.Task_2_1_1;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Courier extends Thread {
  private double speed;
  private int capacity, x, y;
  private Pizzeria workplace;
  private String name;
  private ArrayList<Order> trunk;

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

  public void setWorkplace(Pizzeria workplace) {
    this.workplace = workplace;
  }

  private int getDistance(Order order) {
    return (Math.abs(order.getX() - x) + Math.abs(order.getY() - y));
  }

  private int getDistance() {
    return (Math.abs(x) + Math.abs(y));
  }

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
        if (workplace.getDateDiff(closest.getOrderDate(), delDate, TimeUnit.MILLISECONDS)
            <= workplace.getDelayLimit()) {
          workplace.checkout(closest.getCost());
          workplace.log.logDelivered(this, closest,true);
        } else {
          workplace.log.logDelivered(this, closest,false);
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

  public String getCName() {
    return name;
  }
}
