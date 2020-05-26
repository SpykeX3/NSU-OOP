package ru.nsu.fit.chernikov.Task_2_2_1;

import javafx.scene.canvas.GraphicsContext;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Level;
import ru.nsu.fit.chernikov.Task_2_2_1.UI.Painter;

public class GameTask extends Thread {
  private Level level;
  private Painter painter;
  private boolean finished;

  public GameTask(Level level, Painter painter, GraphicsContext gc) {
    this.level = level;
    this.painter = painter;
    finished = false;
  }

  @Override
  public void run() {
    while (!finished) {
      System.out.println("Next");
      painter.paint();
      level.step();
      try {
        sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void finish() {
    finished = true;
  }
}
