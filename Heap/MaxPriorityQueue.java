package Heap;

import java.util.ArrayList;
import java.util.Comparator;

public class MaxPriorityQueue<K,V> extends MaxAbstractPriorityQueue<K,V> { // exactly Max ordered form of Arraylist implementation
                                                                        // I write extra Classes and Interfaces but they are same with normal just names are different
  protected ArrayList<Entry<K,V>> heap = new ArrayList<>();

  public MaxPriorityQueue() { super(); }

  public MaxPriorityQueue(Comparator<K> comp) { super(comp); }


  public MaxPriorityQueue(K[] keys, V[] values) {
    super();
    for (int j=0; j < Math.min(keys.length, values.length); j++)
      heap.add(new PQEntry<>(keys[j], values[j]));
    heapify();
  }

  // protected utilities
  protected int parent(int j) { return (j-1) / 2; }     // truncating division
  protected int left(int j) { return 2*j + 1; }
  protected int right(int j) { return 2*j + 2; }
  protected boolean hasLeft(int j) { return left(j) < heap.size(); }
  protected boolean hasRight(int j) { return right(j) < heap.size(); }

  protected void swap(int i, int j) {
    Entry<K,V> temp = heap.get(i);
    heap.set(i, heap.get(j));
    heap.set(j, temp);
  }

  protected void upheap(int j) {
    while (j > 0) {            // continue until reaching root (or break statement)
      int p = parent(j);
      if (compare(heap.get(j), heap.get(p)) < 0) break; // heap property verified
      swap(j, p);
      j = p;                                // continue from the parent's location
    }
  }

  protected void downheap(int j) {
    while (hasLeft(j)) {               // continue to bottom (or break statement)
      int leftIndex = left(j);
      int maxChildIndex = leftIndex;     // although right may be max
      if (hasRight(j)) {
          int rightIndex = right(j);
          if (compare(heap.get(leftIndex), heap.get(rightIndex)) < 0)
            maxChildIndex = rightIndex;  // right child is bigger
      }
      if (compare(heap.get(maxChildIndex), heap.get(j)) < 0)
        break;                             // heap property has been restored
      swap(j, maxChildIndex);
      j = maxChildIndex;                 // continue at position of the child
    }
  }

  protected void heapify() {
    int startIndex = parent(size()-1);    // start at PARENT of last entry
    for (int j=startIndex; j >= 0; j--)   // loop until processing the root
      downheap(j);
  }



  @Override
  public int size() { return heap.size(); }

  public Entry<K,V> max() {
    if (heap.isEmpty()) return null;
    return heap.get(0);
  }


  @Override
  public Entry<K,V> insert(K key, V value) throws IllegalArgumentException {
    checkKey(key);      // auxiliary key-checking method (could throw exception)
    Entry<K,V> newest = new PQEntry<>(key, value);
    heap.add(newest);                      // add to the end of the list
    upheap(heap.size() - 1);               // upheap newly added entry
    return newest;
  }



  @Override
  public Entry<K,V> removeMax() {
    if (heap.isEmpty()) return null;
    Entry<K,V> answer = heap.get(0);
    swap(0, heap.size() - 1);              // put max item at the end
    heap.remove(heap.size() - 1);          // and remove it from the list;
    downheap(0);                           // then fix new root
    return answer;
  }
  private void sanityCheck() {
    for (int j=0; j < heap.size(); j++) {
      int left = left(j);
      int right = right(j);
      if (left < heap.size() && compare(heap.get(left), heap.get(j)) < 0)
        System.out.println("Invalid left child relationship");
      if (right < heap.size() && compare(heap.get(right), heap.get(j)) < 0)
        System.out.println("Invalid right child relationship");
    }
  }
}
