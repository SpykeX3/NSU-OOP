package ru.nsu.fit.chernikov.Task_2_2_1;

import javafx.scene.canvas.GraphicsContext;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Level;
import ru.nsu.fit.chernikov.Task_2_2_1.UI.Painter;

/** Thread that is processes level state and calls painter. */
public class GameTask extends Thread {
  private Level level;
  private Painter painter;
  private boolean finished;
  private boolean paused;

  public GameTask(Level level, Painter painter, GraphicsContext gc) {
    this.level = level;
    this.painter = painter;
    finished = false;
    paused = true;
  }

  /** Runs the GameTask. */
  @Override
  public void run() {
    while (!finished) {
      if (!paused) {
        painter.paint();
        level.step();
      }
      try {
        sleep(level.getInterval());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** Finishes the thread. */
  public void finish() {
    finished = true;
  }

  /** Pauses the game. */
  public void pause() {
    paused = true;
  }

  /** Unpauses the game. */
  public void unpause() {
    paused = false;
  }
}
