package ru.nsu.fit.chernikov.Task_1_2;

import org.junit.Test;
import static org.junit.Assert.*;

public class DistanceTest {

  @Test
  public void assignmentTest() {
    Distance d = new Distance();
    Distance d1 = new Distance(1);
    assertTrue(d.isInf());
    assertFalse(d1.isInf());
    assertEquals(1, d1.getWeight());
    d.setInf(false);
    assertFalse(d.isInf());
    d.setInf(true);
    assertTrue(d.isInf());
    d.setWeight(d1.getWeight());
    assertFalse(d.isInf());
    assertEquals(1, d.getWeight());
  }

  @Test
  public void comparisonTest() {
    Distance d = new Distance();
    Distance dInf = new Distance(d);
    Distance d1 = new Distance(1);
    Distance d2 = new Distance(2);
    Distance d2_2 = new Distance(2);
    assertTrue(d.compareTo(d1) > 0);
    assertTrue(d1.compareTo(d) < 0);
    assertTrue(d1.compareTo(d2) < 0);
    assertTrue(d2.compareTo(d1) > 0);
    assertEquals(0, d.compareTo(dInf));
    assertEquals(0, dInf.compareTo(d));
    assertEquals(0, d2.compareTo(d2_2));
    assertEquals(0, d2_2.compareTo(d2));
  }

  @Test
  public void toStringTest() {
    Distance d = new Distance();
    Distance d1 = new Distance(1);
    Distance d2 = new Distance(2);
    Distance d100500 = new Distance(100500);
    assertEquals("inf", d.toString());
    assertEquals("1", d1.toString());
    assertEquals("2", d2.toString());
    assertEquals("100500", d100500.toString());
  }

  @Test
  public void minMaxTest() {
    Distance d = new Distance();
    Distance d1 = new Distance(1);
    Distance d2 = new Distance(2);
    Distance dMin, dMax;
    dMin = Distance.min(d, d1);
    dMax = Distance.max(d, d1);
    assertEquals(d1, dMin);
    assertEquals(d, dMax);
    dMin = Distance.min(d1, d);
    dMax = Distance.max(d1, d);
    assertEquals(d1, dMin);
    assertEquals(d, dMax);
    dMin = Distance.min(d1, d2);
    dMax = Distance.max(d1, d2);
    assertEquals(d1, dMin);
    assertEquals(d2, dMax);
  }

  @Test
  public void addTest() {
    Distance d = new Distance();
    Distance d1 = new Distance(1);
    Distance d2 = new Distance(2);
    Distance d3 = new Distance(3);
    Distance sum;
    sum = d.add(d1);
    assertEquals(0, d.compareTo(sum));
    sum = d1.add(d);
    assertEquals(0, d.compareTo(sum));
    sum = d1.add(d2);
    assertEquals(0, d3.compareTo(sum));
  }
}
