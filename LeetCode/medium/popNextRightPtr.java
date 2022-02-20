package LeetCode.medium;

import java.util.ArrayDeque;
import java.util.Deque;

public class popNextRightPtr {
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;
    
        public Node() {}
        
        public Node(int _val) {
            val = _val;
        }
    
        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }

    // because tree is perfect balanced a node either is the leaf, or has both left and right child nodes
    public Node connect(Node root) {
        if (root == null) {
            return root;
        }
        Deque<Node> q = new ArrayDeque<>();
        Deque<Integer> layers = new ArrayDeque<>();
        q.add(root);
        layers.add(0);

        while (!q.isEmpty()) {
            Node n = q.poll();
            int layer = layers.poll();
            if (!layers.isEmpty() && layers.peek() == layer) {
                n.next = q.peek();
            }
            // if one child is null both are null we do not add null
            if (n.left != null) {
                q.add(n.left);
                q.add(n.right);
                layers.add(layer + 1);
                layers.add(layer + 1);
            }
        }

        return root;
    }
}

