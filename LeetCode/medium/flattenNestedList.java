package LeetCode.medium;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class flattenNestedList implements Iterator<Integer> {
    private Node head;  // head is a placeholder with which we can go back to the first node
    private Node current;
    private int size;

    private static class Node {
        private int value;
        private Node next;

        public Node(int value) {
            this.value = value;
            next = null;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    public NestedIterator(List<NestedInteger> nestedList) {
        head = new Node(0);
        current = head;
        size = 0;

        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                int value = ni.getInteger();
                Node n = new Node(value);
                current.setNext(n);
                current = n;
                size++;
            } else {
                flattenNestedList flattenedNested = new flattenNestedList(ni.getList());
                for (Integer i : flattenedNested) {
                    Node n = new Node(i);
                    current.setNext(n);
                    current = n;
                    size++;
                }
            }
        }
        current = head.next;
    }

    @Override
    public Integer next() {
        if (current == null) {
            throw new NoSuchElementException("No more items to return!");
        }
        int ans = current.value;
        current = current.next;
        return ans;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }
}
