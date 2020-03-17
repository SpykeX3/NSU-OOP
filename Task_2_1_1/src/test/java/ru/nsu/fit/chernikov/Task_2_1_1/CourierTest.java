package ru.nsu.fit.chernikov.Task_2_1_1;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CourierTest {
  @Test
  public void nameTest() {
    Courier c1 = new Courier("X", 1, 1);
    Courier c2 = new Courier("Y", 1, 1);
    assertEquals("X", c1.getCName());
    assertEquals("Y", c2.getCName());
  }

  @Test
  public void runTest() {
    ArrayList<Courier> couriers = new ArrayList<>();
    Courier courier = new Courier("C", 100, 1);
    couriers.add(courier);
    Pizzeria pz = new Pizzeria(new ArrayList<>(), couriers, 100, 5000, 2);
    Order o1 = new Order(1, 1, 1, new Date(), 1, 1);
    Order o2 = new Order(2, 2, 2, new Date(), -1, 1);
    Order o3 = new Order(3, 3, 3, new Date(), 1, -1);
    pz.log.logOrderReceived(o1);
    pz.log.logOrderReceived(o2);
    pz.log.logOrderReceived(o3);
    pz.putInWarehouse(o1);
    pz.putInWarehouse(o2);
    pz.putInWarehouse(o3);
    pz.run();
    Log.CourierStatistics cs = pz.log.getCourierStat().get(courier);
    assertEquals(3, cs.ordersCompleted);
    assertEquals(6, cs.profit, 0.1);
  }
}
