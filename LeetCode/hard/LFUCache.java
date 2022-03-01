package LeetCode.hard;

import java.util.HashMap;
/*
The idea is to have a hash table <key, nodeA> and a hash table <freq, list of key nodes>
nodeA has reference to key node in list so when a key is touched, key node needs to be update in freq, moving to freq + 1 list
*/
public class LFUCache {
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

    public LFUCache(int capacity) {
        
    }
    
    public int get(int key) {
        
    }
    
    public void put(int key, int value) {
        
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
