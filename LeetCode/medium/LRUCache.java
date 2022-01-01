package LeetCode.medium;

import java.util.HashMap;

public class LRUCache {
    int cap;
    HashMap<Integer, Node> dict;
    Node head;
    Node tail;
    
    private static class Node {
        private int key;
        private int value;
        private Node prev;
        private Node next;
        
        public Node(int key, int value, Node prev, Node next) {
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    public LRUCache(int capacity) {
        this.cap = capacity;
        head = new Node(-1, -1, null, null);
        tail = new Node(-1, -1, head, null);
        head.next = tail;
        tail.prev = head;
        dict = new HashMap<>();
    }
    
    public int get(int key) {
        Node target = dict.get(key);
        if (target == null) {
            return -1;
        } else {
            moveToEnd(target);
            return target.value;
        }
    }
    
    public void put(int key, int value) {
        Node target = dict.get(key);
        if (target == null) {
            Node new_node = new Node(key, value, null,null);
            appendLast(new_node);
            dict.put(key, new_node);
            if (dict.size() > cap) {
                dict.remove(head.next.key);
                evictFirst();
            }
        } else {
            target.value = value;
            moveToEnd(target);
        }
        
    }
    
    private void moveToEnd(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
        appendLast(n);
    }
    
    private void evictFirst() {
        head.next = head.next.next;
        head.next.prev = head;
    }
    
    private void appendLast(Node n) {
        Node old_last = tail.prev;
        old_last.next = n;
        n.prev = old_last;
        n.next = tail;
        tail.prev = n;
    }
}
