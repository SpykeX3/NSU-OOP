package Task_1_1;

import java.util.Collections;
import java.util.List;

public class Heap {

  private static <T extends Comparable<T>> void heapify(List<T> arr, int size, int index) {
    int max = index;
    int left = 2 * index + 1;
    int right = 2 * index + 2;
    if (right < size && arr.get(right).compareTo(arr.get(max)) > 0) {
      max = right;
    }
    if (left < size && arr.get(left).compareTo(arr.get(max)) > 0) {
      max = left;
    }
    if (max != index) {
      Collections.swap(arr, index, max);
      heapify(arr, size, max);
    }
  }

  public static <T extends Comparable<T>> void heapsort(List<T> arr) {
    for (int i = arr.size() / 2 - 1; i >= 0; i--) {
      heapify(arr, arr.size(), i);
    }
    for (int i = arr.size() - 1; i >= 0; i--) {
      Collections.swap(arr, 0, i);
      heapify(arr, i, 0);
    }
  }
}
