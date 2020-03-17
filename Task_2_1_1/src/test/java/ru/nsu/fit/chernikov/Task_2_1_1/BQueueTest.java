package ru.nsu.fit.chernikov.Task_2_1_1;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class BQueueTest {
  private class Putter implements Runnable {
    int waitingTime;
    int cnt;
    BQueue<Integer> q;

    Putter(BQueue<Integer> queue, int milisec, int _cnt) {
      waitingTime = milisec;
      q = queue;
      cnt = _cnt;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(waitingTime);
      } catch (InterruptedException e) {
        fail();
      }
      for (int i = 0; i < cnt; i++) {
        q.put(i);
      }
    }
  }

  private class Taker implements Runnable {
    int waitingTime;
    int cnt;
    BQueue<Integer> q;

    Taker(BQueue<Integer> queue, int milisec, int _cnt) {
      waitingTime = milisec;
      q = queue;
      cnt = _cnt;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(waitingTime);
      } catch (InterruptedException e) {
        fail();
      }
      for (int i = 0; i < cnt; i++) {
        assertEquals(i, (int) q.take());
      }
    }
  }

  @Test
  public void putTakeTest() {
    BQueue<Integer> bq = new BQueue<>();
    bq.put(1);
    bq.put(2);
    bq.put(3);
    for (int i = 1; i < 4; i++) {
      int k = bq.take();
      assertEquals(i, k);
    }
  }

  @Test
  public void emptyTake() {
    BQueue<Integer> q = new BQueue<>();
    ExecutorService exec = Executors.newFixedThreadPool(2);
    exec.submit(new Putter(q, 2000, 10));
    exec.submit(new Taker(q, 0, 10));
    try {
      exec.shutdown();
      Thread.sleep(3000);
      assertTrue(exec.isTerminated());
    } catch (InterruptedException ignore) {
      fail();
    }
  }

  @Test
  public void fullPut() {
      BQueue<Integer> q = new BQueue<>(10);
      ExecutorService exec = Executors.newFixedThreadPool(2);
      exec.submit(new Putter(q, 0, 50));
      exec.submit(new Taker(q, 2000, 50));
      try {
          exec.shutdown();
          Thread.sleep(5000);
          assertTrue(exec.isTerminated());
      } catch (InterruptedException ignore) {
          fail();
      }
  }
}
