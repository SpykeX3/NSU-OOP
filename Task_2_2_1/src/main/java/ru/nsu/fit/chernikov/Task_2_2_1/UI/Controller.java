package ru.nsu.fit.chernikov.Task_2_2_1.UI;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Direction;
import ru.nsu.fit.chernikov.Task_2_2_1.GameObjects.Level;
import ru.nsu.fit.chernikov.Task_2_2_1.GameTask;


public class Controller {

  private Level level;
  private Painter paint;
  private GraphicsContext context;
  private boolean paused;
  private GameTask gameTask;

  @FXML private Canvas gameCanvas;
  @FXML private Label score;

  @FXML
  public void initialize() {
    context = gameCanvas.getGraphicsContext2D();
    gameCanvas.setFocusTraversable(true);
    gameCanvas.setOnKeyPressed(this::keyHandler);
    level = new Level(48, 72, 3,200);
    paint = new Painter(level, context, this);
    gameTask = new GameTask(level, paint, context);
    paint.paint();
    gameTask.start();
  }

  private void keyHandler(KeyEvent e) {
    Direction.Values dir = null;
    switch (e.getCode()) {
      case UP:
        dir = Direction.Values.UP;
        break;
      case DOWN:
        dir = Direction.Values.DOWN;
        break;
      case LEFT:
        dir = Direction.Values.LEFT;
        break;
      case RIGHT:
        dir = Direction.Values.RIGHT;
        break;
      case SPACE:
        gameTask.pause();
        return;
      case ENTER:
        level = new Level(48, 72, 3,200);
        paint = new Painter(level, context, this);
        gameTask.finish();
        gameTask = new GameTask(level, paint, context);
        paint.paint();
        gameTask.start();
        return;
      case ESCAPE:
        gameTask.finish();
        System.exit(0);
      default:
        return;
    }
    gameTask.unpause();
    level.rotate(dir);
  }

  public Label getScoreLabel() {
    return score;
  }
}
