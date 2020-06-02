package ru.nsu.fit.chernikov.Task_2_2_1;

import javafx.scene.paint.Color;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Point;

public class PointTest {
  @Test
  public void sanityCheck() {
    Point x = new Point(0, 0, new Color(1, 1, 0, 1));
    Point y = new Point(2, 1, new Color(0, 1, 1, 1));
    Point z = new Point(2, 1, new Color(0, 1, 1, 1));

    assertEquals(0, x.getX());
    assertEquals(2, y.getX());
    assertEquals(0, x.getY());
    assertEquals(1, y.getY());

    x.setX(5);
    assertEquals(5, x.getX());
    x.setY(3);
    assertEquals(3, x.getY());

    assertFalse(x.equals(y));
    assertTrue(z.equals(y));
  }
}
