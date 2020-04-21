package ru.nsu.fit.chernikov.Task_2_1_1;

import com.google.gson.annotations.Expose;

import java.util.Date;

/** Cook is a Thread that extracts orders from Pizzeria, processes it and puts it in warehouse. */
public class Cook extends Thread {
  private String name;
  private double timePerUnit;
  private Pizzeria workplace;

  /**
   * Cook constructor.
   *
   * @param _name name of the cook.
   * @param _timePerUnit time to process 1 unit of order complexity.
   */
  public Cook(String _name, double _timePerUnit) {
    timePerUnit = _timePerUnit;
    name = _name;
  }

  /**
   * Set Pizzeria, where cook is working. Must be set before thread is started.
   *
   * @param workplace workplace to set.
   */
  public void setWorkplace(Pizzeria workplace) {
    this.workplace = workplace;
  }

  /** Start taking and processing orders. Workplace must be already set. */
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
        System.out.println("No tasks for cook " + name);
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
        System.out.println("Cook " + name + " Failed to put in warehouse ");
        break;
      }
      workplace.log.logPutInWarehouse(this, task);
      now = new Date();
    }
    workplace.log.logCookShiftEnd(this);
  }

  /**
   * Get cook name.
   *
   * @return name of the cook.
   */
  public String getCName() {
    return name;
  }
}
