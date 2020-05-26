package ru.nsu.fit.chernikov.Task_2_2_1.UI;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Level;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Point;

public class Painter {
  private Level level;
  private final GraphicsContext gc;
  private final Controller ct;

  public Painter(Level level, GraphicsContext gc, Controller ct) {
    this.level = level;
    this.gc = gc;
    this.ct = ct;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

  public void paint() {
    Platform.runLater(
        () -> {
          int size = level.getSize();
          ct.getScoreLabel().setText("Score: " + level.score());
          gc.setFill(Level.backgroundColor);
          gc.fillRect(0, 0, level.getWidth() * size, level.getHeight() * size);
          for (Point p : level.getPoints()) {
            paintPoint(p);
          }
        });
  }

  private void paintPoint(Point point) {
    int size = level.getSize();
    gc.setFill(point.getColor());
    gc.fillRoundRect(point.getX() * size, point.getY() * size, size, size, 1, 1);
  }
}
