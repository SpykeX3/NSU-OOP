package ru.nsu.fit.chernikov.Task_2_1_1;

import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Concurrent queue container. Can de bounded or unbounded.
 *
 * @param <T> type to store.
 */
public class BQueue<T> {
  private ReentrantLock lock;
  private Condition notEmpty;
  private Condition notFull;
  private long cap;
  private LinkedList<T> queue;

  /** Unbounded constructor. */
  public BQueue() {
    cap = Long.MAX_VALUE;
    lock = new ReentrantLock();
    notFull = lock.newCondition();
    notEmpty = lock.newCondition();
    queue = new LinkedList<>();
  }

  /**
   * Bounded constructor.
   *
   * @param _cap capacity.
   */
  public BQueue(long _cap) {
    cap = _cap;
    lock = new ReentrantLock();
    notEmpty = lock.newCondition();
    notFull = lock.newCondition();
    queue = new LinkedList<>();
  }

  /**
   * Take first element, removing it. Will wait until not empty.
   *
   * @return first element or null if interrupted.
   */
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

  /**
   * Take first element, removing it. Will wait until not empty but only for specified time.
   *
   * @param millisec maximum wait time in milliseconds.
   * @return first element and null if interrupted or if time is out.
   */
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

  /**
   * Put element as last. Will wait until queue is not full.
   *
   * @param elem element to place.
   * @return true if placed successfully, false otherwise.
   */
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

  /**
   * Put element as last. Will wait until queue is not full for specified time.
   *
   * @param elem element to place.
   * @param millisec timeout in milliseconds.
   * @return true if placed successfully, false otherwise.
   */
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
