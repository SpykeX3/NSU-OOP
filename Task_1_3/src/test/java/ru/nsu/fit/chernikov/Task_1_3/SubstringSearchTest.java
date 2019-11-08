/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.nsu.fit.chernikov.Task_1_3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubstringSearchTest {

  private static String readFileAsString(String fileName) throws IOException {
    return new String(Files.readAllBytes(Paths.get(fileName)));
  }

  private void check(String str, String substr, ArrayList<Long> positions) {
    int sslen = substr.length();
    for (long pos : positions) {
      assertEquals(
          "failed finding '" + substr + "'", substr, str.substring((int) pos, (int) pos + sslen));
    }
  }

  @Test
  public void testOnFile() {
    String data = null;
    String[] checkList = {
      "1", "123", "假借字", " ", "абра", "aaa", "qwerty", "333", "I am a long test!"
    };
    int[] expectedCount = {2, 1, 4, 9, 8, 18, 1, 0, 1};

    try {
      data = readFileAsString("src/test/resources/input.txt");
    } catch (IOException e) {
      System.err.println(e);
      fail();
    }
    for (int i = 0; i < checkList.length; i++) {
      try {
        ArrayList<Long> res = SubstringSearch.search("src/test/resources/input.txt", checkList[i]);
        assertEquals(
            "wrong substring count for '" + checkList[i] + "': " + i, expectedCount[i], res.size());
        check(
            data,
            checkList[i],
            SubstringSearch.search("src/test/resources/input.txt", checkList[i]));
      } catch (IOException e) {
        System.err.println(e + "\nException while checking '" + checkList[i] + "'");
        fail();
      }
    }
  }
}
