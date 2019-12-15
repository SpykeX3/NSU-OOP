package ru.nsu.fit.chernikov;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Utility class for checking if there are composite numbers using multiple threads. */
public class CompositeFinder {
  /**
   * Check if collection contains composite numbers.
   *
   * @param collection where to check elements. Negative numbers are considered composite.
   * @param threads number of threads to use for computation.
   * @return true if collection contains composite number.
   * @throws ExecutionException if exception was thrown while processing an element.
   * @throws InterruptedException if threads have been interrupted.
   */
  public static boolean hasComposite(Collection<Integer> collection, int threads)
      throws ExecutionException, InterruptedException {
    ArrayList<Future<Boolean>> answers = new ArrayList<>();
    ExecutorService pool = Executors.newFixedThreadPool(threads);
    for (int num : collection) {
      answers.add(pool.submit(new PrimeTask(num)));
    }
    for (Future<Boolean> isPrime : answers) {
      if (!isPrime.get()) {
        pool.shutdown();
        return true;
      }
    }
    return false;
  }
}
