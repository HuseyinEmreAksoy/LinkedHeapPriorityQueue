package Heap;

public class Driver {
    public static void main(String[] args) {
        LinkedHeapPriorityQueue<Integer,Integer> a = new LinkedHeapPriorityQueue<>();
        a.insert(1,1);
        a.insert(3,8);
        a.insert(5,4);
        a.insert(2,9);
        a.insert(4,4);
        a.insert(7,9);

        MaxPriorityQueue<Integer,Integer> b = new MaxPriorityQueue<>();
        b.insert(1,1);
        b.insert(3,8);
        b.insert(5,4);
        b.insert(2,9);
        b.insert(4,4);
        b.insert(8,9);
        System.out.println("max b: " + b.max().getKey());

        b.removeMax();

        System.out.println("min a: " + a.min().getKey());

        a.removeMin();

        LinkedHeapPriorityQueue2<Integer,Integer> c = new LinkedHeapPriorityQueue2<>();
        c.insert(1,1);
        c.insert(3,8);
        c.insert(5,4);
        c.insert(2,9);
        DoublyLinkedList.Node<Entry<Integer,Integer>> temp = c.heap.header.getNext();

        for (int i = 0; i < c.size();i++) {
            System.out.println(temp.getElement().getKey());
            temp = temp.getNext();

        }
        System.out.println("last: " + c.last.getElement().getKey());
        System.out.println("------------");
        c.insert(4,4);
        c.insert(7,9);

        System.out.println("min c: " + c.min().getKey());
        temp = c.heap.header.getNext();
        for (int i = 0; i < c.size();i++) {
            System.out.println(temp.getElement().getKey());
            temp = temp.getNext();
        }
        System.out.println("last: " + c.last.getElement().getKey());


        System.out.println("remove min: " + c.removeMin().getKey());
        System.out.println("remove min: " + c.removeMin().getKey());

        System.out.println("------------");
        temp = c.heap.header.getNext();
        for (int i = 0; i < c.size();i++) {
            System.out.println(temp.getElement().getKey());
            temp = temp.getNext();
        }
        System.out.println("last: " + c.last.getElement().getKey());

    }
}
