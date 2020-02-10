package ru.nsu.fit.chernikov.Task_2_1_1;

import java.util.Date;

public class Cook extends Thread {

  private String name;
  private double timePerUnit;
  private Date shiftEnd;
  private Pizzeria workplace;

  public Cook(String _name, Pizzeria _workplace, double _timePerUnit) {
    timePerUnit = _timePerUnit;
    workplace = _workplace;
    name = _name;
  }

  @Override
  public void run() {

    // Take order
    // Process order
    // Put pizza in the warehouse

    workplace.log.logCookCame(this);
    Date now = new Date();
    while (now.before(workplace.getShiftEnd())) {
      Order task = workplace.takeCookingOrder();
      if (task == null) {
        break;
      }
      task.setCook(this);
      workplace.log.logOrderStart(this, task);
      try {
        Thread.sleep((long) (1000 * task.getComplexity() * timePerUnit));
      } catch (InterruptedException e) {
        workplace.log.logException(e);
      }
      workplace.log.logDoneCooking(this, task);
      if (!workplace.putInWarehouse(task)) {
        break;
      }
      workplace.log.logPutInWarehouse(this, task);
      now = new Date();
    }
    workplace.log.logCookShiftEnd(this);
  }

  public String getCName() {
    return name;
  }
}
