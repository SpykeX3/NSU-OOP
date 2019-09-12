package Task_1_1;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class HeapTest {
  @Test
  public void testArrListIntSortReversed() {
    ArrayList<Integer> arr1 = new ArrayList<>();
    ArrayList<Integer> arr2 = new ArrayList<>();
    for (int i = 100; i > 0; i--) {
      arr1.add(i);
      arr2.add(i);
    }
    Collections.sort(arr2);
    Heap.heapsort(arr1);
    assertEquals(arr1.size(), arr2.size());
    for (int i = 0; i < arr1.size(); i++) {
      assertEquals(arr1.get(i), arr2.get(i));
    }
  }

  @Test
  public void testArrListIntSortSorted() {
    ArrayList<Integer> arr1 = new ArrayList<>();
    ArrayList<Integer> arr2 = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      arr1.add(i);
      arr2.add(i);
    }
    Collections.sort(arr2);
    Heap.heapsort(arr1);
    assertEquals(arr1.size(), arr2.size());
    for (int i = 0; i < arr1.size(); i++) {
      assertEquals(arr1.get(i), arr2.get(i));
    }
  }

  @Test
  public void testArrListFloatSortReversed() {
    ArrayList<Float> arr1 = new ArrayList<>();
    ArrayList<Float> arr2 = new ArrayList<>();
    for (int i = 100; i > 0; i--) {
      arr1.add((float) i);
      arr2.add((float) i);
    }
    Collections.sort(arr2);
    Heap.heapsort(arr1);
    assertEquals(arr1.size(), arr2.size());
    for (int i = 0; i < arr1.size(); i++) {
      assertEquals(arr1.get(i), arr2.get(i));
    }
  }

  @Test
  public void testArrListFloatSortSorted() {
    ArrayList<Float> arr1 = new ArrayList<>();
    ArrayList<Float> arr2 = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      arr1.add((float) i);
      arr2.add((float) i);
    }
    Collections.sort(arr2);
    Heap.heapsort(arr1);
    assertEquals(arr1.size(), arr2.size());
    for (int i = 0; i < arr1.size(); i++) {
      assertEquals(arr1.get(i), arr2.get(i));
    }
  }
}
