package ru.nsu.fit.chernikov.Task_2_1_1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
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
    long profitableOrdersCompleted;
    long timeSpentWaiting;
    double profit;

    CourierStatistics(
        long _totalDistance,
        long _ordersCompleted,
        long _profitableOrdersCompleted,
        long _timeSpentWaiting,
        double _profit) {
      totalDistance = _totalDistance;
      ordersCompleted = _ordersCompleted;
      profitableOrdersCompleted = _profitableOrdersCompleted;
      timeSpentWaiting = _timeSpentWaiting;
      profit = _profit;
    }
  }

  private BufferedWriter file;
  private ConcurrentHashMap<Cook, CookStatistics> cookStat;
  private ConcurrentHashMap<Courier, CourierStatistics> courierStat;
  private List<Order> orders;
  private long exceptionCount;
  Duration waitingForNewOrders;
  Duration waitingForNotEmptyWarehouse;
  Duration waitingForNotFullWarehouse;

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
    courierStat.putIfAbsent(courier, new CourierStatistics(0, 0, 0, 0, 0));
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
      double finalProfit1 = profit;
      courierStat.computeIfPresent(
          order.getCourier(),
          (k, v) ->
              new CourierStatistics(
                  v.totalDistance + Math.abs(order.getX()) + Math.abs(order.getY()),
                  v.ordersCompleted + 1,
                  v.profitableOrdersCompleted + 1,
                  v.timeSpentWaiting,
                  v.profit + finalProfit1));
    } else {
      status = "late";
      double finalProfit2 = profit;
      courierStat.computeIfPresent(
          order.getCourier(),
          (k, v) ->
              new CourierStatistics(
                  v.totalDistance + Math.abs(order.getX()) + Math.abs(order.getY()),
                  v.ordersCompleted + 1,
                  v.profitableOrdersCompleted,
                  v.timeSpentWaiting,
                  v.profit + finalProfit2));
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
    if (order.getCook() != null) {
      double finalProfit = profit;
      cookStat.computeIfPresent(
          order.getCook(),
          (k, v) ->
              new CookStatistics(
                  v.totalComplexity + order.getComplexity(),
                  v.ordersCompleted + 1,
                  v.timeSpentWaiting,
                  v.profit + finalProfit));
    }
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
  void logStatistics(Duration shiftLen) {
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
    if (waitingForNewOrders != null
        && waitingForNotEmptyWarehouse != null
        && waitingForNotFullWarehouse != null) {
      print("Orders statistics:");
      print("\tWaiting for new orders: " + waitingForNewOrders.toMillis() + "ms");
      print("Warehouse statistics:");
      print("\tCouriers waiting for deliveries:\t" + waitingForNotEmptyWarehouse.toMillis() + "ms");
      print("\tCooks waiting for free space:\t" + waitingForNotFullWarehouse.toMillis() + "ms");
    }

    print("Orders:");
    for (Order ord : orders) {
      if (ord.getCook() == null) {
        print("Order " + ord.getOrderId() + " was ignored");
      } else if (ord.getCourier() == null) {
        print("Order " + ord.getOrderId() + " was cooked but not delivered");
      } else {
        print("Order " + ord.getOrderId() + " was delivered");
      }
    }
    print("Advise:");
    logCookAdvice();
    logCourierAdvice();
    logWarehouseAdvice(shiftLen);

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

  public void setWaitingForNewOrders(Duration waitingForNewOrders) {
    this.waitingForNewOrders = waitingForNewOrders;
  }

  public void setWaitingForNotEmptyWarehouse(Duration waitingForNotEmptyWarehouse) {
    this.waitingForNotEmptyWarehouse = waitingForNotEmptyWarehouse;
  }

  public void setWaitingForNotFullWarehouse(Duration waitingForNotFullWarehouse) {
    this.waitingForNotFullWarehouse = waitingForNotFullWarehouse;
  }

  private void logCookAdvice(){
    double totalComplexity =
            orders.stream().mapToDouble(Order::getComplexity).reduce(0, Double::sum);
    double managedComplexity =
            cookStat.values().stream().mapToDouble(cs -> cs.totalComplexity).reduce(0, Double::sum);
    double mvpComplexity =
            cookStat.values().stream().mapToDouble(cs -> cs.totalComplexity).max().orElse(0);
    if (cookStat.size() == 0) {
      print("Come on, you don't even have cooks. Hire someone.");
    } else {
      Entry<Cook, CookStatistics> mvp =
              cookStat.entrySet().stream()
                      .filter(e -> e.getValue().totalComplexity == mvpComplexity)
                      .findFirst()
                      .orElse(null);
      if (totalComplexity == 0) {
        print("You really don't need your cooks. Even a monkey would manage.");
      } else {
        double cookEff = managedComplexity / totalComplexity;
        if (cookEff == 1) {
          print("You may fire some cooks or change them for more cheap ones.");
        } else if (cookEff > 0.9) {
          print("Cooks are fine.");
        } else if (cookEff > 0.7) {
          print("Hire some new cooks.");
        } else {
          print("You really need more cooks.");
        }
      }
    }
  }

  private void logCourierAdvice(){
    double managedDelivery =
            courierStat.values().stream().mapToDouble(cs -> cs.ordersCompleted).sum();
    double profitDelivery = courierStat.values().stream().mapToDouble(cs -> cs.profit).sum();
    double profitDeliveryCount = courierStat.values().stream().mapToDouble(cs -> cs.profit).sum();
    double totalDistance =
            orders.stream().mapToDouble(s -> Math.sqrt(s.getX() ^ 2 + s.getY() ^ 2)).sum();
    double totalDistanceOfCooked =
            orders.stream()
                    .filter(s -> s.getCook() != null)
                    .mapToDouble(s -> Math.sqrt(s.getX() ^ 2 + s.getY() ^ 2))
                    .sum();
    long cookedOrders = orders.stream().filter(s -> s.getCook() != null).count();
    if (totalDistance == 0) {
      print("All orders were made within pizzeria. Leave one mots cheap courier.");
    } else {
      double courEff = totalDistanceOfCooked / totalDistance;
      if (courEff == 1) {
        print("You have enough couriers. Maybe even too many.");
      } else if (courEff > 0.8) {
        print("Hire some more couriers");
      } else {
        print("You need to increase delivery capabilities!");
      }
      double profitEff = (double)profitDeliveryCount/cookedOrders;
      if(profitEff==1){
        print("Your couriers are fast enough");
      }else if(profitEff>0.9){
        print("Train a couple couriers and cooks to be more efficient.");
      }else if(profitEff>0.7){
        print("You loose profit. Make your deliveries faster.");
      }else{
        print("Your system is not fine. Cooks and Couriers are not able to get profit. Maybe you should increase time window?");
      }
    }
  }

  private void logWarehouseAdvice(Duration sl){
    double waitingE = waitingForNotEmptyWarehouse.toMillis()/(double)sl.toMillis();
    double waitingF = waitingForNotFullWarehouse.toMillis()/(double)sl.toMillis();
    if(waitingE>1){
      print("Couriers have spent a lot of time in pizzeria waiting for new deliveries. Try improving cooks' productivity or invest in advertisement of your pizzeria if cooks are fine.");
    }else if(waitingE>0.5){
      print("Couriers have spent some time in pizzeria waiting for new deliveries. Train a couple of cooks or spend some money advertising of your pizzeria if cooks are fine.");
    }else if(waitingE>0.2){
      print("Couriers have spent a little time in pizzeria waiting for new deliveries. No need for heavy improvement.");
    }else{
      print("Couriers have spent very little time in pizzeria waiting for new deliveries. Balance seems fine.");
    }

    if(waitingF>1){
      print("Cooks have spent a lot of time waiting for space in the warehouse. Critical need for more/better couriers or increase in storage capacity.");
    }else if(waitingF>0.5){
      print("Cooks have spent some time waiting for space in the warehouse. You should improve couriers or increase storage.");
    }else if(waitingF>0.1){
      print("Cooks have spent a little time waiting for space in the warehouse. No need for heavy improvement, but larger warehouse wouldn't hurt.");
    }else{
      print("Cooks have spent very little time waiting for space in the warehouse. Balance seems fine.");
    }
  }
}
