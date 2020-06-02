package ru.nsu.fit.chernikov.Task_2_2_1;

import org.junit.Test;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Direction;

import static org.junit.Assert.*;

public class DirectionTest {
  @Test
  public void distanceTest() {
    Direction.Values up, down, left, right;
    up = Direction.Values.UP;
    down = Direction.Values.DOWN;
    left = Direction.Values.LEFT;
    right = Direction.Values.RIGHT;
    assertEquals(2,Direction.distance(up,down));
    assertEquals(1,Direction.distance(up,left));
    assertEquals(1,Direction.distance(up,right));
    assertEquals(2,Direction.distance(left,right));
    assertEquals(0,Direction.distance(down,down));
  }
}
