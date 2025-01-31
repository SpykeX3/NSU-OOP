/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.nsu.fit.chernikov.Task_1_3;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class SubstringSearch {

  /**
   * Search for substring in a file. Will return ArrayList of all occurrences.
   *
   * @param filename where to look for substring occurrences
   * @param substr what to look for, can not be empty
   * @return ArrayList of all substring occurrences in string
   * @throws IOException if IOException occurred during IO calls
   * @throws IllegalArgumentException if substr is empty
   */
  public static ArrayList<Long> search(String filename, String substr) throws IOException {
    int len = substr.length();
    if (len == 0) {
      throw new IllegalArgumentException("empty substring");
    }
    Reader reader = new FileReader(filename);
    ArrayList<Long> result = new ArrayList<>();
    int bufferSize = Math.max(len * 2, 256); // double substr size or 256 chars
    char[] buffer = new char[bufferSize];
    int substrPos = 0;
    int count = reader.read(buffer, 0, bufferSize);
    long filePos = 0;
    int usedSpace = count;
    while (count != -1) {
      for (int bufferPos = 0;
          bufferPos + len <= bufferSize && bufferPos + len <= usedSpace;
          bufferPos++) {
        boolean found = true;
        for (substrPos = 0; substrPos < len; substrPos++) {
          if (buffer[bufferPos + substrPos] != substr.charAt(substrPos)) { // if not substring
            found = false;
            break;
          }
        }
        if (found) {
          result.add(filePos + bufferPos);
        }
      }
      if (filePos == 0) {
        filePos -= len - 1; // first time no overlap
      }
      filePos += count; // move position in file
      System.arraycopy(buffer, bufferSize - len + 1, buffer, 0, len - 1); // copy overlapping part
      count = reader.read(buffer, len - 1, bufferSize - len + 1); // read in buffer
      usedSpace = count + len - 1; // used buffer part
    }
    reader.close();
    return result;
  }
}
