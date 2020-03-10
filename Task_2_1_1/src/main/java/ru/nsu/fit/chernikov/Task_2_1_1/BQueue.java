package ru.nsu.fit.chernikov.Task_2_1_1;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BQueue<T> {
  private ReentrantLock lock;
  private Condition notEmpty;
  private Condition notFull;
  private long cap;
  private LinkedList<T> queue;

  public BQueue() {
    cap = Long.MAX_VALUE;
    lock = new ReentrantLock();
    notFull = lock.newCondition();
    notEmpty = lock.newCondition();
  }

  public BQueue(long _cap) {
    cap = _cap;
    lock = new ReentrantLock();
    notEmpty = lock.newCondition();
    notFull = lock.newCondition();
  }

  T take() {
    lock.lock();
    T elem;
    try {
      while (queue.size() == 0) {
        notEmpty.await();
      }
      elem = queue.getLast();
      notFull.signalAll();
    } catch (InterruptedException ignore) {
      return null;
    } finally {
      lock.unlock();
    }
    return elem;
  }

  boolean put(T elem) {
    lock.lock();
    try {
      while (queue.size() == cap) {
        notFull.await();
      }
      queue.addFirst(elem);
      notFull.signalAll();
    } catch (InterruptedException ignore) {
      return false;
    } finally {
      lock.unlock();
    }
    return true;
  }
}
