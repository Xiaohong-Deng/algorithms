package LeetCode.hard;

import java.util.HashMap;
/*
The idea is to have a hash table <key, nodeA> and a hash table <freq, list of key nodes>
nodeA has reference to key node in list so when a key is touched, key node needs to be update in freq, moving to freq + 1 list
*/
import java.util.TreeSet;
public class LFUCache {
    int cap;
    long time;
    HashMap<Integer, Node> dict;
    TreeSet<Node> treeSet;

    private static class Node implements Comparable<Node> {
        private int key;
        private int value;
        private int freq;
        private long time;
        
        public Node(int key, int value, int freq, long time) {
            this.key = key;
            this.value = value;
            this.freq = freq;
            this.time = time;
        }

        @Override
        public int compareTo(Node that) {
            if (this.freq > that.freq) {
                return 1;
            }

            if (this.freq == that.freq) {
                return (int) (this.time - that.time);
            }

            return -1;
        }
    }

    public LFUCache(int capacity) {
        cap = capacity;
        time = 0;
        dict = new HashMap<>();
        treeSet = new TreeSet<>();
    }
    
    public int get(int key) {
        if (dict.containsKey(key)) {
            Node res = dict.get(key);
            treeSet.remove(res);
            res.freq += 1;
            res.time = time++;
            treeSet.add(res);
            return res.value;
        } else {
            return -1;
        }
        
    }
    
    public void put(int key, int value) {

        // if reference content is modified in treeSet must remove it and insert it back
        // I think the content in treeSet is still modified but only when it is inserted it is compared and placed to the right place
        // So since its content is different we need to trigger the comparisons again to place it to the correct position
        if (dict.containsKey(key)) {
            Node n = dict.get(key);
            treeSet.remove(n);
            n.value = value;
            n.time = time++;
            n.freq = n.freq + 1;
            treeSet.add(n);
        } else {
            if (cap == 0) {
                return;
            }
            if (dict.size() >= cap) {
                Node removed = treeSet.first();
                treeSet.remove(removed);
                dict.remove(removed.key);
            }

            Node n = new Node(key, value, 1, time++);
            dict.put(key, n);
            treeSet.add(n);
        }
    }

    public static void main(String[] args) {
        LFUCache t = new LFUCache(2);
        t.put(1, 1);
        t.put(2, 2);
        t.get(1);
        t.put(3, 3);
        System.out.println(t.get(2));
    }
}
