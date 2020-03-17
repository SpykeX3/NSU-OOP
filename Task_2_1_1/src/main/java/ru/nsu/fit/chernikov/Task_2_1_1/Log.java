package ru.nsu.fit.chernikov.Task_2_1_1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class for writing log with number of possible events. Log also collects statistics. Can write in
 * file or stdout.
 */
public class Log {

  static class CookStatistics {
    double totalComplexity;
    long ordersCompleted;
    long timeSpentWaiting;
    double profit;

    CookStatistics(
        double _totalComplexity, long _ordersCompleted, long _timeSpentWaiting, double _profit) {
      totalComplexity = _totalComplexity;
      ordersCompleted = _ordersCompleted;
      timeSpentWaiting = _timeSpentWaiting;
      profit = _profit;
    }
  }

  static class CourierStatistics {
    long totalDistance;
    long ordersCompleted;
    long timeSpentWaiting;
    double profit;

    CourierStatistics(
        long _totalDistance, long _ordersCompleted, long _timeSpentWaiting, double _profit) {
      totalDistance = _totalDistance;
      ordersCompleted = _ordersCompleted;
      timeSpentWaiting = _timeSpentWaiting;
      profit = _profit;
    }
  }

  private BufferedWriter file;
  private ConcurrentHashMap<Cook, CookStatistics> cookStat;
  private ConcurrentHashMap<Courier, CourierStatistics> courierStat;
  private List<Order> orders;
  private long exceptionCount;

  Log(String _filename) {
    try {
      file = new BufferedWriter(new FileWriter(_filename));
    } catch (IOException e) {
      System.err.println(e.toString());
      file = null;
    }
    cookStat = new ConcurrentHashMap<>();
    courierStat = new ConcurrentHashMap<>();
    orders = Collections.synchronizedList(new ArrayList<Order>());
  }

  Log() {
    orders = Collections.synchronizedList(new ArrayList<Order>());
    cookStat = new ConcurrentHashMap<>();
    courierStat = new ConcurrentHashMap<>();
  }

  private void print(String str) {
    try {
      if (file != null) {
        file.write(str + System.lineSeparator());
      }
    } catch (IOException e) {
      System.err.println(e.toString());
    }
    System.out.println(str);
    System.out.flush();
  }

  public void flush() {
    try {
      if (file != null) {
        file.flush();
      }
    } catch (IOException e) {
      System.err.println(e.toString());
    }
  }

  private synchronized void incExceptions() {
    exceptionCount++;
  }

  public long getExceptionCount() {
    return exceptionCount;
  }

  /**
   * Log occurred exception.
   *
   * @param e exception.
   */
  void logException(Exception e) {
    print("[" + new Date() + "] " + e.toString());
    incExceptions();
  }

  /**
   * Log start of work for cook.
   *
   * @param cook actor.
   */
  void logCookCame(Cook cook) {
    cookStat.putIfAbsent(cook, new CookStatistics(0, 0, 0, 0));
    print("[" + new Date() + "] Cook " + cook.getCName() + "\" came");
  }

  /**
   * Log start of work for courier.
   *
   * @param courier actor.
   */
  void logCourierCame(Courier courier) {
    courierStat.putIfAbsent(courier, new CourierStatistics(0, 0, 0, 0));
    print("[" + new Date() + "] Courier " + courier.getCName() + "\" came");
  }

  /**
   * Log that order was received.
   *
   * @param order new order.
   */
  void logOrderReceived(Order order) {
    print("[" + new Date() + "] Order " + order.getOrderId() + " war received");
    orders.add(order);
  }

  /**
   * Log that order is being processed.
   *
   * @param cook actor.
   * @param order product.
   */
  void logOrderStart(Cook cook, Order order) {
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " is being cooked by \""
            + cook.getCName()
            + "\"");
  }

  /**
   * Log that cook has finished cooking.
   *
   * @param cook actor.
   * @param order product.
   */
  void logDoneCooking(Cook cook, Order order) {
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " was cooked by \""
            + cook.getCName()
            + "\"");
  }

  /**
   * Log that cook has put order in warehouse.
   *
   * @param cook actor.
   * @param order product.
   */
  void logPutInWarehouse(Cook cook, Order order) {
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " was stored by \""
            + cook.getCName()
            + "\"");
  }

  /**
   * Log that courier has taken an order for delivery.
   *
   * @param courier actor.
   * @param order product.
   */
  void logDeliveryTaken(Courier courier, Order order) {
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " was taken for delivery by \""
            + courier.getCName()
            + "\"");
  }

  /**
   * Log that order was delivered by courier. Delivery can be late or on time.
   *
   * @param courier actor.
   * @param order product.
   * @param onTime true if on time.
   */
  void logDelivered(Courier courier, Order order, Boolean onTime) {
    String status;
    double profit = 0;
    if (onTime) {
      status = "on time";
      profit = order.getCost();
    } else {
      status = "late";
    }
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " was delivered "
            + status
            + " by \""
            + courier.getCName()
            + "\"");
    double finalProfit = profit;
    if (order.getCook() != null) {
      cookStat.computeIfPresent(
          order.getCook(),
          (k, v) ->
              new CookStatistics(
                  v.totalComplexity + order.getComplexity(),
                  v.ordersCompleted + 1,
                  v.timeSpentWaiting,
                  v.profit + finalProfit));
    }
    courierStat.computeIfPresent(
        order.getCourier(),
        (k, v) ->
            new CourierStatistics(
                v.totalDistance + Math.abs(order.getX()) + Math.abs(order.getY()),
                v.ordersCompleted + 1,
                v.timeSpentWaiting,
                v.profit + finalProfit));
  }

  /**
   * Log that cook has finished shift and left.
   *
   * @param cook actor.
   */
  void logCookShiftEnd(Cook cook) {
    print("[" + new Date() + "] Cook \"" + cook.getCName() + "\" has left");
  }

  /**
   * Log that courier has finished shift and left.
   *
   * @param courier actor.
   */
  void logCourierShiftEnd(Courier courier) {
    print("[" + new Date() + "] Courier \"" + courier.getCName() + "\" has left");
  }

  /** Print shift statistics. Should be done after shift has finished. */
  void logStatistics() {
    print("Cooks:");
    print(String.format("%15s %10s %7s %8s", "Name", "Complexity", "Orders", "Profit"));
    for (Entry<Cook, CookStatistics> entry : cookStat.entrySet()) {
      Cook cook = entry.getKey();
      CookStatistics stat = entry.getValue();
      print(
          String.format(
              "%15s %10.2f %7d %8.2f",
              cook.getCName(), stat.totalComplexity, stat.ordersCompleted, stat.profit));
    }
    print(System.lineSeparator() + "Couriers:");
    print(String.format("%15s %10s %7s %8s", "Name", "Distance", "Orders", "Profit"));
    for (Entry<Courier, CourierStatistics> entry : courierStat.entrySet()) {
      Courier courier = entry.getKey();
      CourierStatistics stat = entry.getValue();
      print(
          String.format(
              "%15s %10d %7d %8.2f",
              courier.getCName(), stat.totalDistance, stat.ordersCompleted, stat.profit));
    }
    for (Order ord : orders) {
      if (ord.getCook() == null) {
        print("Order " + ord.getOrderId() + " was ignored");
      } else if (ord.getCourier() == null) {
        print("Order " + ord.getOrderId() + " was cooked but not delivered");
      } else {
        print("Order " + ord.getOrderId() + " was delivered");
      }
    }
  }

  public ConcurrentHashMap<Cook, CookStatistics> getCookStat() {
    return cookStat;
  }

  public ConcurrentHashMap<Courier, CourierStatistics> getCourierStat() {
    return courierStat;
  }

  public List<Order> getOrders() {
    return orders;
  }
}
