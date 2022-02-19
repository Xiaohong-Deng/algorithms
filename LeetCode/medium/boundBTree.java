package LeetCode.medium;

import java.util.ArrayList;
import java.util.List;
import LeetCode.medium.BSTIterator.TreeNode;

public class boundBTree {
    public List<Integer> boundaryOfBinaryTree(TreeNode root) {
        List<Integer> boundary = new ArrayList<>();

        if (root == null) {
            return boundary;
        }

        boundary.add(root.val);
        if (root.left != null) {
            addLeft(boundary, root.left);
            addLeaf(boundary, root.left);
        }
            
        if (root.right != null) {
            addLeaf(boundary, root.right);
            addRight(boundary, root.right);
        }
            
        return boundary;
    }

    private void addLeft(List<Integer> boundary, TreeNode node) {
        if (node.left == null && node.right == null) {
            return;
        }

        boundary.add(node.val);
        // if my left is null, then my right is in left boundary
        // if my left is not null, my right can't be in the left boundary
        if (node.left != null)   
            addLeft(boundary, node.left);
        else
            addLeft(boundary, node.right);
    }

    private void addLeaf(List<Integer> boundary, TreeNode node) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            boundary.add(node.val);
            return;
        }

        addLeaf(boundary, node.left);
        addLeaf(boundary, node.right);
    }

    private void addRight(List<Integer> boundary, TreeNode node) {
        if (node.left == null && node.right == null) {
            return;
        }

        if (node.right != null) 
            addRight(boundary, node.right);
        else
            addRight(boundary, node.left);
        boundary.add(node.val);
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(
            1, new TreeNode(
                2, new TreeNode(4), new TreeNode(5, new TreeNode(7), new TreeNode(8))
                ), 
                new TreeNode(
                    3, new TreeNode(6, new TreeNode(9), new TreeNode(10)), null)
        );
        boundBTree t = new boundBTree();
        List<Integer> boundary = t.boundaryOfBinaryTree(root);
        for (int i : boundary) {
            System.out.println(i);
        }
    }
}
