package Heap;


import java.util.Comparator;

public class LinkedHeapPriorityQueue<K,V> extends AbstractPriorityQueue<K,V>  { // I do with last variable which is last element of heap

    protected LinkedBinaryTree<Entry<K,V>> heap = new LinkedBinaryTree<>();
    Position<Entry<K,V>> last = heap.root();

    public LinkedHeapPriorityQueue() { super(); }

    public LinkedHeapPriorityQueue(Comparator<K> comp) { super(comp); }


    public LinkedHeapPriorityQueue(K[] keys, V[] values) {
        super();
        for (int j=0; j < Math.min(keys.length, values.length); j++)
            //heap.add(new PQEntry<>(keys[j], values[j]));
        downheap(heap.root());
    }

    protected void swap(Position<Entry<K,V>> p1, Position<Entry<K,V>> p2) {
        Entry<K,V> temp = p1.getElement();
        heap.set(p1, p2.getElement());
        heap.set(p2, temp);
    }

    protected void upheap(Position<Entry<K,V>> j) {
        while (j != heap.root()) {            // continue until reaching root (or break statement)
            Position<Entry<K,V>> p = heap.parent(j);
            if (compare(j.getElement(), p.getElement()) >= 0) break; // heap property verified
            swap(j, p);
            j = p;                                // continue from the parent's location
        }
    }

    protected void downheap(Position<Entry<K,V>> p) {
        while (heap.left(p) != null) {               // continue to bottom (or break statement)
            Position<Entry<K,V>> leftIndex = heap.left(p);
            Position<Entry<K,V>> smallChildIndex = leftIndex;     // although right may be smaller
            if (heap.right(p) != null) {
                Position<Entry<K,V>> rightIndex = heap.right(p);
                if (compare(heap.left(p).getElement(), heap.right(p).getElement()) > 0)
                    smallChildIndex = rightIndex;  // right child is smaller
            }
            if (compare(smallChildIndex.getElement(), p.getElement()) >= 0)
                break;                             // heap property has been restored
            swap(p, smallChildIndex);
            p = smallChildIndex;                 // continue at position of the child
        }
    }

    @Override
    public int size() { return heap.size(); }

    @Override
    public Entry<K,V> min() {
        if (heap.isEmpty()) return null;
        return heap.root().getElement();
    }


    @Override
    public Entry<K,V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key);      // auxiliary key-checking method (could throw exception)
        Entry<K,V> newest = new PQEntry<>(key, value);

        if (heap.size() == 0)
            heap.addRoot(newest);
        else if (heap.size() == 1)// special case we need to directly add left of root
        {
            heap.addLeft(heap.root(),newest);
            last = heap.left(heap.root());
            upheap(last);
        }
        else if (heap.sibling(last) == null) {// if our sibling is empty we can add directly
            heap.addRight(heap.parent(last), newest);
            last = heap.sibling(last);
            upheap(last);
        }
        else if (heap.size() == (Math.pow(2, heap.depth(last)) * 2 -1))// directly left of tree because all parents are full
        {
            Position<Entry<K,V>> newEnt = getPos(heap.root());// work with < O(logn + 1)
            heap.addLeft(newEnt, newest);
            last = heap.left(newEnt);
            upheap(last);
        }
        else
            newest = insertAux(key, value, last);// recursive method
        return newest;
    }
    public Entry<K,V> insertAux(K key, V value,Position<Entry<K,V>> p)// if our parent is not left child we go to parent of parent until left child
    {                                                                  // after that go to leftest child of sibling
        Entry<K,V> newest = new PQEntry<>(key, value);
        if (heap.parent(p) !=  heap.left(heap.parent(heap.parent(p))))
            insertAux(key,value,heap.parent(p));
        else {
            heap.addLeft(getPos(heap.sibling(heap.parent(p))),newest);
            last = heap.left(getPos(heap.sibling(heap.parent(p))));
        }
        upheap(last);               // upheap newly added entry
        return newest;
    }
    private Position<Entry<K,V>> getPos(Position<Entry<K,V>> p){//go to leftest element
        if (heap.left(p) != null && heap.right(p) != null)
            return getPos(heap.left(p));
        return p;
    }

    @Override
    public Entry<K,V> removeMin() {
        if (heap.isEmpty()) return null;
        Entry<K,V> answer = heap.root().getElement();
        swap(heap.root(), last);              // put minimum item at the end
        heap.remove(last);          // and remove it from the list;
        downheap(heap.root());                           // then fix new root
        return answer;
    }
}
