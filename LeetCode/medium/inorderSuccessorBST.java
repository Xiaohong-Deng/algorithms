package LeetCode.medium;

import LeetCode.medium.BSTIterator.TreeNode;

public class inorderSuccessorBST {
    // p is guaranteeed to be in the tree
    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        int val = p.val;
        TreeNode successor = null;

        return findSuccessor(root, val, successor);
    }

    private TreeNode findSuccessor(TreeNode tree, int val, TreeNode successor) {
        if (tree == null) {
            return successor;
        }

        if (tree.val <= val) {
            return findSuccessor(tree.right, val, successor);
        } else {
            // note if tree.val > val, successor might be null and we should assign tree to it
            // or it might be some tree (successor.val > val) and tree.val < successor.val
            // because after first assignment we go left, anything in the left < successor.val
            successor = tree;
            return findSuccessor(tree.left, val, successor);
        }

    }

    public static void main(String[] args) {
        TreeNode bst = new TreeNode(
            5,
            new TreeNode(
                3,
                new TreeNode(
                    2,
                    new TreeNode(1),
                    null
                ),
                new TreeNode(4)
            ),
            new TreeNode(6)
        );

        inorderSuccessorBST t = new inorderSuccessorBST();
        TreeNode successor = t.inorderSuccessor(bst, new TreeNode(6));
        System.out.println(successor);
        successor = t.inorderSuccessor(new TreeNode(2, new TreeNode(1), new TreeNode(3)), new TreeNode(1));
        System.out.println(successor.val);
    }
}
