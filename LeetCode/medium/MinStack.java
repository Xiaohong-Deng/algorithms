package LeetCode.medium;

import java.util.Stack;

public class MinStack {
    private Stack<Integer> main;
    private Stack<Integer> minTracker;

    public MinStack() {
        main = new Stack<>();
        minTracker = new Stack<>();
    }
    
    public void push(int val) {
        main.push(val);

        if (minTracker.empty() || val <= minTracker.peek()) {
            System.out.println("pushing " + val);
            minTracker.push(val);
        }
        
    }
    
    public void pop() {
        // if (main.peek().equals(minTracker.peek())) {
        //     minTracker.pop();
        // }

        if (main.peek().intValue() == minTracker.peek().intValue()) {
            minTracker.pop();
        }

        main.pop();
    }
    
    public int top() {
        return main.peek();
    }
    
    public int getMin() {
        return minTracker.peek();
    }

    public static void main(String[] args) {
        MinStack test = new MinStack();
        test.push(512);
        test.push(-1024);
        test.push(-1024);
        test.push(512);
        test.pop();
        test.pop();
        test.pop();
    }
}
