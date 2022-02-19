package LeetCode.medium;

import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class BSTIterator {

    private List<Integer> orderedTree;
    private int index;

    private Deque<TreeNode> stack;

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode() {}
        public TreeNode(int val) { this.val = val; }
        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public BSTIterator(TreeNode root) {
        // you can flatten the BST here
        this.orderedTree = new ArrayList<>();
        this.index = 0;
        orderTree(root);

        stack = new ArrayDeque<>();

        orderUntilLeftMost(root);
    }
    
    public int next() {
        return orderedTree.get(index++);
    }
    
    public boolean hasNext() {
        return index < orderedTree.size();
    }

    public int next2() {
        TreeNode ans = stack.pollLast();

        if (ans.right != null) {
            orderUntilLeftMost(ans.right);
        }

        return ans.val;
    }

    public boolean hasNext2() {
        return this.stack.size() > 0;
    }

    private void orderTree(TreeNode node) {
        if (node == null) {
            return;
        }
        orderTree(node.left);

        this.orderedTree.add(node.val);

        orderTree(node.right);
    }

    private void orderUntilLeftMost(TreeNode node) {
        // we can use if but that causes recursion
        while (node != null) {
            stack.addLast(node);
            node = node.left;
        }
    }
}
