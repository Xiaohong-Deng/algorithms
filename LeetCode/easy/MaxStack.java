package LeetCode.easy;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MaxStack {
    private LinkedList<Node> dll;
    private TreeMap<Integer, List<Node>> map;
    public static class Node {
        private int val;

        public Node(int val) {
            this.val = val;
        }
    }
    public MaxStack() {
        dll = new LinkedList<Node>();
        map = new TreeMap<>();
    }
    
    public void push(int x) {
        Node n = new Node(x);
        dll.add(n);
        if (!map.containsKey(x)) {
            map.put(x, new ArrayList<>());
        }
        map.get(x).add(n);
    }
    
    public int pop() {
        Node lastN = dll.removeLast();
        int lastV = lastN.val;
        map.get(lastV).remove(lastN);
        if (map.get(lastV).isEmpty())
            map.remove(lastV);
        return lastV;
    }
    
    public int top() {
        return dll.peekLast().val;
    }
    
    public int peekMax() {
        return this.map.lastKey();  // can't be Map, must be TreeMap
    }
    
    public int popMax() {
        int maxVal = this.map.lastKey();
        List<Node> ns = this.map.get(maxVal);
        Node maxN = ns.remove(ns.size() - 1);
        if (map.get(maxVal).isEmpty()) {
            map.remove(maxVal);
        }
        this.dll.remove(maxN);
        return maxVal;
    }

    public static void main(String[] args) {
        MaxStack ms = new MaxStack();
        ms.push(5);
        ms.push(1);
        ms.push(5);
        ms.popMax();
        System.out.println(ms.pop());
        System.out.println(ms.pop());
    }
}
