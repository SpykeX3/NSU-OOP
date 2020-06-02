package ru.nsu.fit.chernikov.Task_2_2_1.UI;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Level;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Point;

/** Painter is used for painting level content on the canvas. */
public class Painter {
  private Level level;
  private final GraphicsContext gc;
  private final Controller ct;

  /**
   * Create new Painter object.
   *
   * @param level level to be drawn
   * @param gc context where to draw
   * @param ct controller of the scene
   */
  public Painter(Level level, GraphicsContext gc, Controller ct) {
    this.level = level;
    this.gc = gc;
    this.ct = ct;
  }

  /** Paint level content on the GC. */
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
