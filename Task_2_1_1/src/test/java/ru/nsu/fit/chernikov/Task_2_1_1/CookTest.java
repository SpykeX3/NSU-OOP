package ru.nsu.fit.chernikov.Task_2_1_1;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class CookTest {

  @Test
  public void nameTest() {
    Cook c1 = new Cook("A", 1);
    Cook c2 = new Cook("B", 1);
    assertEquals("A", c1.getCName());
    assertEquals("B", c2.getCName());
  }

  @Test
  public void runTest() {
    ArrayList<Cook> cooks = new ArrayList<>();
    cooks.add(new Cook("A", 0.5));
    Pizzeria pz = new Pizzeria(cooks, new ArrayList<>(), 100, 0, 5);
    pz.addOrder(new Order(1, 1, 1, new Date(), 1, 1));
    pz.addOrder(new Order(2, 1, 1, new Date(), 1, 1));
    pz.addOrder(new Order(3, 1, 1, new Date(), 1, 1));
    pz.run();
    ArrayList<Order> al = pz.fillTrunk(3);
    assertEquals(3, (long) al.size());
    assertEquals(1, al.get(0).getComplexity(), 0.1);
    assertEquals(2, al.get(1).getComplexity(), 0.1);
    assertEquals(3, al.get(2).getComplexity(), 0.1);
  }
}
