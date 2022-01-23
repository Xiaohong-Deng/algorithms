package LeetCode.medium;

import java.util.List;
import java.util.ArrayList;

public class BSTIterator {

    private List<Integer> orderedTree;
    private int index;

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
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
    }
    
    public int next() {
        return orderedTree.get(index++);
    }
    
    public boolean hasNext() {
        return index < orderedTree.size();
    }

    private void orderTree(TreeNode node) {
        if (node == null) {
            return;
        }
        orderTree(node.left);

        this.orderedTree.add(node.val);

        orderTree(node.right);
    }
}
