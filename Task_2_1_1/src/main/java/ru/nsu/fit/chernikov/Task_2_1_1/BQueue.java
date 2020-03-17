package ru.nsu.fit.chernikov.Task_2_1_1;

import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
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
    queue = new LinkedList<>();
  }

  public BQueue(long _cap) {
    cap = _cap;
    lock = new ReentrantLock();
    notEmpty = lock.newCondition();
    notFull = lock.newCondition();
    queue = new LinkedList<>();
  }

  T take() {
    lock.lock();
    T elem;
    try {
      while (queue.size() == 0) {
        notEmpty.await();
      }
      elem = queue.pollLast();
      notFull.signalAll();
    } catch (InterruptedException ignore) {
      elem = null;
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
    return elem;
  }

  T take(long millisec) {
    lock.lock();
    T elem = null;
    try {
      while (queue.size() == 0) {
        if (!notEmpty.await(millisec, TimeUnit.MILLISECONDS)) {
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
          return null;
        }
      }
      elem = queue.pollLast();
      notFull.signalAll();
    } catch (InterruptedException ignore) {
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
    return elem;
  }

  boolean put(T elem) {
    lock.lock();
    boolean res = true;
    try {
      while (queue.size() == cap) {
        notFull.await();
      }
      queue.addFirst(elem);
      notEmpty.signal();
    } catch (InterruptedException ignore) {
      res = false;
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
    return res;
  }

  boolean put(T elem, long millisec) {
    lock.lock();
    boolean res = true;
    Instant then = Instant.ofEpochMilli(Instant.now().toEpochMilli() + millisec);
    try {
      while (queue.size() == cap) {
        if (!notFull.await(
            then.toEpochMilli() - Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)) {
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
          return false;
        }
      }
      queue.addFirst(elem);
      notEmpty.signal();
    } catch (InterruptedException ignore) {
      res = false;
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
    return res;
  }
}
