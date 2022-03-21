package LeetCode.easy;

public class MyHashMap {
    private int size = 99971;
    private Node[] buckets;
    public static class Node {
        private int key;
        private int value;
        private Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public MyHashMap() {
        buckets = new Node[size];
    }
    
    public void put(int key, int value) {
        int idx = key % size;
        if (buckets[idx] == null) {
            buckets[idx] = new Node(key, value);
        } else {
            Node current = buckets[idx];
            Node prev = null;
            while (current != null) {
                if (current.key == key) {
                    current.value = value;
                    return;
                }
                prev = current;
                current = current.next;
            }

            prev.next = new Node(key, value);
        }
    }
    
    public int get(int key) {
        int idx = key % size;
        if (buckets[idx] == null) {
            return -1;
        } else {
            Node current = buckets[idx];
            while (current != null) {
                if (current.key == key) {
                    return current.value;
                }
            }

            return -1;
        }
    }
    
    public void remove(int key) {
        int idx = key % size;
        if (buckets[idx] != null) {
            Node current = buckets[idx];

            if (current.key == key) {
                buckets[idx] = null;
            } else {
                Node prev = current;
                current = current.next;
                while (current != null) {
                    if (current.key == key) {
                        prev.next = current.next;
                    }

                    prev = current;
                    current = current.next;
                }
            }
        }
    }
}
