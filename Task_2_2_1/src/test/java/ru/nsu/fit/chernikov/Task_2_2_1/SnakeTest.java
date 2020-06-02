package ru.nsu.fit.chernikov.Task_2_2_1;

import javafx.scene.paint.Color;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Direction;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Snake;

public class SnakeTest {
  @Test
  public void aliveTest() {
    Snake snake = new Snake(new Color(1, 1, 1, 1), 5, 5, Direction.Values.UP, 10, 10);
    assertFalse(snake.isDead());
    snake.kill();
    assertTrue(snake.isDead());
  }

  @Test
  public void moveTest() {
    Snake snake = new Snake(new Color(1, 1, 1, 1), 5, 5, Direction.Values.UP, 10, 10);
    assertEquals(1, snake.getPoints().size());
    snake.move();
    assertEquals(1, snake.getPoints().size());
    assertEquals(5, snake.getPoints().get(0).getX());
    assertEquals(4, snake.getPoints().get(0).getY());
  }

  @Test
  public void feedTest() {
    Snake snake = new Snake(new Color(1, 1, 1, 1), 5, 5, Direction.Values.UP, 10, 10);
    assertEquals(1, snake.getPoints().size());
    snake.feed(2);
    snake.move();
    snake.move();
    assertEquals(3, snake.getPoints().size());
    assertEquals(5, snake.getHead().getX());
    assertEquals(3, snake.getHead().getY());
  }

  @Test
  public void RotateTest(){
    Snake snake = new Snake(new Color(1, 1, 1, 1), 5, 5, Direction.Values.UP, 10, 10);
    assertEquals(1, snake.getPoints().size());
    snake.feed(2);
    snake.move();
    snake.rotate(Direction.Values.RIGHT);
    snake.move();
    assertEquals(3, snake.getPoints().size());
    assertEquals(6, snake.getHead().getX());
    assertEquals(4, snake.getHead().getY());
  }
}
