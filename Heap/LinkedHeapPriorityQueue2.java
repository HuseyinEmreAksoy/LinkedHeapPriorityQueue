
package Heap;

import java.util.Comparator;

public class LinkedHeapPriorityQueue2<K, V> extends AbstractPriorityQueue<K, V> {//I do like arraylist implementation like 1 is root,  2*j+1 left child
                                                                                // I mean 1 root 2 is left child 3 is right 3,4 child of 2 ...
    int size = 0;
    protected DoublyLinkedList<Entry<K, V>> heap = new DoublyLinkedList<>();// we want to circular(differant from original circular) therefore dll

    DoublyLinkedList.Node<Entry<K, V>> last = heap.header;


    public LinkedHeapPriorityQueue2() {
        super();
    }

    public LinkedHeapPriorityQueue2(Comparator<K> comp) {
        super(comp);
    }

    protected int parent(int j) {
        return (j - 1) / 2;
    }     // truncating division

    protected int left(int j) {
        return 2 * j + 1;
    }

    protected int right(int j) {
        return 2 * j + 2;
    }

    protected boolean hasLeft(int j) {
        return left(j) < heap.size();
    }

    protected boolean hasRight(int j) {
        return right(j) < heap.size();
    }

    protected void swap(DoublyLinkedList.Node<Entry<K, V>> p2, DoublyLinkedList.Node<Entry<K, V>> p1) {// change the nodes place
        DoublyLinkedList.Node<Entry<K, V>> temp = new DoublyLinkedList.Node<>(p1.getElement(), p1.getPrev(), p1.getNext());

        if (p1.getNext() == p2) { //Swap with change references
            p1.getPrev().setNext(p2);
            p1.setNext(p2.getNext());
            p1.setPrev(p2);

            p2.getNext().setPrev(p1);
            p2.setNext(p1);
            p2.setPrev(temp.getPrev());

        } else {// if they are not side by side
            p1.getPrev().setNext(p2);
            p1.getNext().setPrev(p2);

            p1.setNext(p2.getNext());
            p1.setPrev(p2.getPrev());

            p2.getNext().setPrev(p1);
            p2.getPrev().setNext(p1);

            p2.setNext(temp.getNext());
            p2.setPrev(temp.getPrev());
        }
        temp.setPrev(null);
        temp.setNext(null);

    }


    protected void upheap(DoublyLinkedList.Node<Entry<K, V>> a, int j) {
        boolean flag = true;//check for last because last can be just change 1 times at 1 time
        while (j > 0) {            // continue until reaching root (or break statement)
            int p = parent(j);
            DoublyLinkedList.Node<Entry<K, V>> par = heap.header.getNext();
            int i = 0;
            while (p != i) {//go to parent node
                par = par.getNext();
                i++;
            }
            if (compare(a.getElement(), par.getElement()) >= 0)
                break; // heap property verified
            if (flag)
                last = par;
            flag = false;
            swap(a, par);
            j = p;                                // continue from the parent's location
        }
    }

    protected void downheap(DoublyLinkedList.Node<Entry<K, V>> a, int j) {
        while (hasLeft(j)) {               // continue to bottom (or break statement)
            int leftIndex = left(j);
            int smallChildIndex2 = leftIndex;
            DoublyLinkedList.Node<Entry<K, V>> left = a;
            DoublyLinkedList.Node<Entry<K, V>> smallChildIndex = left;// although right may be smaller
            int count = j;
            while (leftIndex != count) {//go to left child
                left = left.getNext();
                count++;
            }
            count = j;
            if (hasRight(j)) {
                int rightIndex = right(j);
                DoublyLinkedList.Node<Entry<K, V>> right = a;
                while (rightIndex != count) {//go to right child
                    right = right.getNext();
                    count++;
                }

                if (compare(left.getElement(), right.getElement()) > 0) {
                    smallChildIndex2 = rightIndex;
                    smallChildIndex = right;  // right child is smaller
                }
            }
            if (compare(smallChildIndex.getElement(), a.getElement()) >= 0)
                break;                             // heap property has been restored
            swap(a, smallChildIndex);
            j = smallChildIndex2;                 // continue at position of the child
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Entry<K, V> min() {
        if (size == 0) return null;
        return heap.header.getNext().getElement();
    }

    DoublyLinkedList.Node<Entry<K, V>> leaf = heap.header;

    @Override
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key);      // auxiliary key-checking method (could throw exception)
        Entry<K, V> newest = new AbstractPriorityQueue.PQEntry<>(key, value);
        DoublyLinkedList.Node<Entry<K, V>> newestNode = new DoublyLinkedList.Node<>(newest, null, null);


        if (size() == 0) {// special case
            heap.header.setNext(newestNode);
            newestNode.setPrev(heap.header);
            newestNode.setNext(newestNode);
            leaf = newestNode;
            last = newestNode;

        } else if (right(parent(size() - 1)) == size()) {// right is empty

            newestNode.setPrev(last);
            leaf = leaf.getNext();// our parent is full for now therefore I can go next and I can make circular
            newestNode.setNext(leaf);//because of making circular
            last.setNext(newestNode);
            upheap(newestNode, size());//heap
            last = last.getNext();//update our lastNode


        } else {
            newestNode.setPrev(last);
            newestNode.setNext(last.getNext());//because of making circular
            last.setNext(newestNode);
            upheap(newestNode, size());//heap
            last = last.getNext();//update our lastNode

        }
        size++;
        return newest;
    }

    @Override
    public Entry<K, V> removeMin() {
        if (size == 0) return null;
        Entry<K, V> answer = heap.header.getNext().getElement();

        heap.header.getNext().getNext().setPrev(heap.header);
        heap.header.setNext(heap.header.getNext().getNext());         // and remove it from the list;

        downheap(heap.header.getNext(), 0);                           // then fix new root
        size--;
        return answer;
    }
}
