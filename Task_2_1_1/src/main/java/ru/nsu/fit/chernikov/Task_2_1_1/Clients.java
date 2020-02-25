package ru.nsu.fit.chernikov.Task_2_1_1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

/** Client is a Thread for making predefined orders on specified time. */
public class Clients extends Thread {
  private static class OrderConfig implements Comparable<OrderConfig> {
    double complexity;
    long cost;
    int x, y;
    long timeOfOrder;

    OrderConfig(double _complexity, long _cost, int _x, int _y, long _time) {
      complexity = _complexity;
      cost = _cost;
      x = _x;
      y = _y;
      timeOfOrder = _time;
    }

    @Override
    public int compareTo(OrderConfig o) {
      return (int) (timeOfOrder - o.timeOfOrder);
    }
  }

  private Pizzeria pizzeria;
  private ArrayList<OrderConfig> oc;
  private long nextId;

  /**
   * Constructor that parses FileReader as json configuration file.
   *
   * @param _pizzeria Pizzeria, where orders should be made.
   * @param fr FileReader with json config.
   */
  public Clients(Pizzeria _pizzeria, FileReader fr) {
    Gson gson = new Gson();
    oc = gson.fromJson(fr, new TypeToken<ArrayList<OrderConfig>>() {}.getType());
    pizzeria = _pizzeria;
    nextId = 0;
  }

  private static OrderConfig generateExample(Random ran, long offset, long dayLen) {
    long time;
    do {
      time = (long) (ran.nextGaussian() * 2500) + offset;
    } while (time < 0 || time > dayLen);
    return new OrderConfig(
        ran.nextDouble() * 5,
        ran.nextInt(301) + 100,
        ran.nextInt(41) - 20,
        ran.nextInt(41) - 20,
        time);
  }

  /**
   * Generate example configuration.
   *
   * @return config example.
   */
  public static String generate() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    ArrayList<OrderConfig> orderConfigs = new ArrayList<>();
    long dayLen = 20000;
    long lunchTime = dayLen / 3;
    long dinnerTime = 2 * lunchTime;
    Random ran = new Random();
    for (int i = 0; i < 30; i++) {
      orderConfigs.add(generateExample(ran, lunchTime, dayLen));
    }
    for (int i = 0; i < 35; i++) {
      orderConfigs.add(generateExample(ran, lunchTime, dayLen));
    }
    Collections.sort(orderConfigs);
    return gson.toJson(orderConfigs);
  }

  /** Start making orders in Pizzeria. */
  @Override
  public void run() {
    Instant startTime = Instant.now();
    for (OrderConfig ord : oc) {
      try {
        sleep(ord.timeOfOrder - Duration.between(startTime, Instant.now()).toMillis());
      } catch (InterruptedException e) {
        pizzeria.log.logException(e);
      }
      pizzeria.addOrder(new Order(ord.complexity, ord.cost, nextId++, new Date(), ord.x, ord.y));
    }
  }
}
