package ru.nsu.fit.chernikov.Task_2_1_1;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SmartBQueue<T> extends BQueue<T> {
  private ReentrantLock lock;
  private Condition notEmpty;
  private Condition notFull;
  private long cap;
  private LinkedList<T> queue;
  private Duration waitingEmpty;
  private Duration waitingFull;

  /** Unbounded constructor. */
  public SmartBQueue() {
    cap = Long.MAX_VALUE;
    lock = new ReentrantLock();
    notFull = lock.newCondition();
    notEmpty = lock.newCondition();
    queue = new LinkedList<>();
    waitingEmpty = Duration.ofSeconds(0);
    waitingFull = Duration.ofSeconds(0);
  }

  /**
   * Bounded constructor.
   *
   * @param _cap capacity.
   */
  public SmartBQueue(long _cap) {
    cap = _cap;
    lock = new ReentrantLock();
    notEmpty = lock.newCondition();
    notFull = lock.newCondition();
    queue = new LinkedList<>();
    waitingEmpty = Duration.ofSeconds(0);
    waitingFull = Duration.ZERO;
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
        Instant timeAwaiting = Instant.now();
        notEmpty.await();
        waitingEmpty = waitingEmpty.plus(Duration.between(timeAwaiting, Instant.now()));
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
        Instant timeAwaiting = Instant.now();
        if (!notEmpty.await(millisec, TimeUnit.MILLISECONDS)) {
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
          waitingEmpty = waitingEmpty.plus(Duration.between(timeAwaiting, Instant.now()));
          return null;
        }
        waitingEmpty = waitingEmpty.plus(Duration.between(timeAwaiting, Instant.now()));
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
  @Override
  boolean put(T elem) {
    lock.lock();
    boolean res = true;
    try {
      while (queue.size() == cap) {
        Instant timeAwaiting = Instant.now();
        notFull.await();
        waitingFull = waitingFull.plus(Duration.between(timeAwaiting, Instant.now()));
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
    return false;
  }

  /**
   * Put element as last. Will wait until queue is not full for specified time.
   *
   * @param elem element to place.
   * @param millisec timeout in milliseconds.
   * @return true if placed successfully, false otherwise.
   */
  @Override
  boolean put(T elem, long millisec) {
    lock.lock();
    boolean res = true;
    Instant now = Instant.now();
    Instant then = Instant.ofEpochMilli(Instant.now().toEpochMilli() + millisec);
    try {
      while (queue.size() == cap) {
        Instant timeAwaiting = Instant.now();
        if (!notFull.await(
            then.toEpochMilli() - Instant.now().toEpochMilli(), TimeUnit.MILLISECONDS)) {
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
          waitingFull = waitingFull.plus(Duration.between(timeAwaiting, Instant.now()));
          return false;
        }
        waitingFull = waitingFull.plus(Duration.between(timeAwaiting, Instant.now()));
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
   * Get total duration of waiting for queue not to be empty.
   *
   * @return empty waiting duration.
   */
  public Duration getWaitingEmpty() {
    return waitingEmpty;
  }

  /**
   * Get total duration of waiting for queue not to be full.
   *
   * @return full waiting duration.
   */
  public Duration getWaitingFull() {
    return waitingFull;
  }
}
