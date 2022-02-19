package LeetCode.medium;

import LeetCode.medium.BSTIterator.TreeNode;

public class countGoodNodeBTree {
    private int goodCount;
    public int goodNodes(TreeNode root) {
        goodCount = 0;

        if (root == null) {
            return goodCount;
        }

        countGood(root.val, root);

        return goodCount;
    }

    private void countGood(int maxVal, TreeNode node) {
        if (node == null) {
            return;
        }

        if (node.val >= maxVal) {
            goodCount++;
            maxVal = node.val;
        }

        countGood(maxVal, node.left);
        countGood(maxVal, node.right);
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(
            3,
            new TreeNode(
                1,
                new TreeNode(3),
                null
            ),
            new TreeNode(
                4,
                new TreeNode(1),
                new TreeNode(5)
            )
        );
        countGoodNodeBTree t = new countGoodNodeBTree();
        int goodCount = t.goodNodes(root);
        System.out.println(goodCount);
    }
}
