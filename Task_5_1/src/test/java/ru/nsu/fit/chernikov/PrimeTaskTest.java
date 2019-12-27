/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.nsu.fit.chernikov;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class PrimeTaskTest {
  private static final Logger LOGGER = Logger.getLogger(PrimeTaskTest.class.getName());

  static ArrayList<Integer> primes;
  static ArrayList<Integer> compInEnd;
  static ArrayList<Integer> compInMid;
  static FileWriter log;

  ArrayList<Integer> readListFromFile(String path) throws IOException {
    ArrayList<Integer> result = new ArrayList<Integer>();
    File file = new File(path);
    BufferedReader reader = null;
    reader = new BufferedReader(new FileReader(file));
    String text = null;
    while ((text = reader.readLine()) != null) {
      result.add(Integer.parseInt(text));
    }
    reader.close();
    return result;
  }

  static void writeLog(String str, boolean newLine) {
    try {
      log.write(str);
      System.out.println(str);
      if (newLine) {
        log.write(System.lineSeparator());
      }
      log.flush();
    } catch (IOException e) {
      System.err.println(e);
      System.err.println("Couldn't write string: " + str);
    }
  }

  @BeforeClass
  public static void init() {
    // 1000000007
    try {
      log = new FileWriter(new File("src/test/resources/log.txt"));
    } catch (IOException e) {
      System.err.println(e);
    }
    int examplePrime = 1000000007;
    int exampleComp = 1000000000;
    primes = new ArrayList<>();
    compInMid = new ArrayList<>();
    compInEnd = new ArrayList<>();
    for (int i = 0; i < 4000; i++) {
      primes.add(examplePrime);
      compInEnd.add(examplePrime);
      compInMid.add(examplePrime);
    }
    compInMid.add(exampleComp);
    for (int i = 0; i < 4000; i++) {
      primes.add(examplePrime);
      compInEnd.add(examplePrime);
      compInMid.add(examplePrime);
    }
    compInEnd.add(exampleComp);
    writeLog("" + primes.size() + " primes in list", true);
  }

  public static void setLog(String path) {
    try {
      log = new FileWriter(new File(path));
    } catch (IOException e) {
      System.err.println(e.toString());
    }
  }

  @Test
  public void testComposite() {
    for (int i = 1000; i < 1050; i++) {
      for (int j = 2000; j < 2050; j++) {
        PrimeTask task = new PrimeTask(i * j);
        assertFalse(task.call());
      }
    }
  }

  @Test
  public void testPrime() {
    long start = System.currentTimeMillis();
    for (int prime : primes) {
      PrimeTask task = new PrimeTask(prime);
      assertTrue(task.call());
    }
    long end = System.currentTimeMillis();
    writeLog("One by one time:\t" + ((double) end - start) / 1000, true);
  }

  @Test
  public void testMultipleThreads() {
    int cores = Runtime.getRuntime().availableProcessors();
    for (int j = 1; j <= cores + 2; j++) {
      long start;
      long end;
      long total = 0;
      try {
        start = System.currentTimeMillis();
        assertFalse(CompositeFinder.hasComposite(primes, j));
        end = System.currentTimeMillis();
        writeLog("" + j + " threads time for 'primes':\t" + ((double) end - start) / 1000, true);
        total += end - start;

        start = System.currentTimeMillis();
        assertTrue(CompositeFinder.hasComposite(compInEnd, j));
        end = System.currentTimeMillis();
        writeLog(
            "" + j + " threads time for 'composite in the end':\t" + ((double) end - start) / 1000,
            true);
        total += end - start;

        start = System.currentTimeMillis();
        assertTrue(CompositeFinder.hasComposite(compInMid, j));
        end = System.currentTimeMillis();
        writeLog(
            ""
                + j
                + " threads time for 'composite in the middle':\t"
                + ((double) end - start) / 1000,
            true);
        total += end - start;

        writeLog("" + j + " threads total time':\t" + ((double) total) / 1000, true);
      } catch (Exception e) {
        fail(e.toString());
      }
    }
  }

  static void main() {
    setLog("log.txt");
    PrimeTaskTest tst = new PrimeTaskTest();
    tst.testMultipleThreads();
    tst.testMultipleThreads();
    tst.testMultipleThreads();
  }
}
