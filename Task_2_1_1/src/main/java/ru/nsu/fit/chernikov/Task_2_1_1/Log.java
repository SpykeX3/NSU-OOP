package ru.nsu.fit.chernikov.Task_2_1_1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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

  Log(String _filename) {
    try {
      file = new BufferedWriter(new FileWriter(_filename));
    } catch (IOException e) {
      System.err.println(e.toString());
      file = null;
    }
    cookStat = new ConcurrentHashMap<>();
    courierStat = new ConcurrentHashMap<>();
  }

  Log() {
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

  public void logException(Exception e) {
    print("[" + new Date() + "] " + e.toString());
  }

  public void logCookCame(Cook cook) {
    cookStat.putIfAbsent(cook, new CookStatistics(0, 0, 0, 0));
    print("[" + new Date() + "] Cook " + cook.getCName() + "\" came");
  }

  public void logCourierCame(Courier courier) {
    courierStat.putIfAbsent(courier, new CourierStatistics(0, 0, 0, 0));
    print("[" + new Date() + "] Courier " + courier.getCName() + "\" came");
  }

  public void logOrderStart(Cook cook, Order order) {
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " is being cooked by \""
            + cook.getCName()
            + "\"");
  }

  public void logDoneCooking(Cook cook, Order order) {
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " was cooked by \""
            + cook.getCName()
            + "\"");
  }

  public void logPutInWarehouse(Cook cook, Order order) {
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " was stored by \""
            + cook.getCName()
            + "\"");
  }

  public void logDeliveryTaken(Courier courier, Order order) {
    print(
        "["
            + new Date()
            + "] Order "
            + order.getOrderId()
            + " was taken for delivery by \""
            + courier.getCName()
            + "\"");
  }

  public void logDelivered(Courier courier, Order order, Boolean onTime) {
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
    cookStat.computeIfPresent(
        order.getCook(),
        (k, v) ->
            new CookStatistics(
                v.totalComplexity + order.getComplexity(),
                v.ordersCompleted + 1,
                v.timeSpentWaiting,
                v.profit + finalProfit));
    courierStat.computeIfPresent(
        order.getCourier(),
        (k, v) ->
            new CourierStatistics(
                v.totalDistance + Math.abs(order.getX()) + Math.abs(order.getY()),
                v.ordersCompleted + 1,
                v.timeSpentWaiting,
                v.profit + finalProfit));
  }

  public void logCookShiftEnd(Cook cook) {
    print("[" + new Date() + "] Cook \"" + cook.getCName() + "\" has left");
  }

  public void logCourierShiftEnd(Courier courier) {
    print("[" + new Date() + "] Courier \"" + courier.getCName() + "\" has left");
  }

  public void logStatistics() {
    print("Cooks:");
    print(String.format("%15s %10s %7s %8s","Name","Complexity","Orders","Profit"));
    for (Entry<Cook, CookStatistics> entry : cookStat.entrySet()) {
      Cook cook = entry.getKey();
      CookStatistics stat = entry.getValue();
      print(
          String.format("%15s %10.2f %7d %8.2f",cook.getCName(), stat.totalComplexity,stat.ordersCompleted, stat.profit));
    }
    print(System.lineSeparator()+"Couriers:");
    print(String.format("%15s %10s %7s %8s","Name","Distance","Orders","Profit"));
    for (Entry<Courier, CourierStatistics> entry : courierStat.entrySet()) {
      Courier courier = entry.getKey();
      CourierStatistics stat = entry.getValue();
      print(
          String.format("%15s %10d %7d %8.2f",courier.getCName(), stat.totalDistance,stat.ordersCompleted, stat.profit));
    }
  }
}
