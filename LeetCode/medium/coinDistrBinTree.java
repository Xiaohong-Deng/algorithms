package LeetCode.medium;

import javax.swing.tree.TreeCellEditor;

import LeetCode.medium.BSTIterator.TreeNode;

public class coinDistrBinTree {
    int numMoves;
    // n coins and n nodes, at beginning any node can hold 0 to n coins
    // dfs idea: ask left child how many coins do you want or send, so does right node. Add answers to total
    // now based on the coins you will receive and give to your child nodes, how many coins you need from or send to your parent
    // for root if it has excessive coins that have no place to go then numCoins must be > numNodes
    public int distributeCoins(TreeNode root) {
        this.numMoves = 0;
        int leftMoves = dfs(root.left);
        int rightMoves = dfs(root.right);
        this.numMoves += Math.abs(leftMoves) + Math.abs(rightMoves);

        return this.numMoves;
    }

    private int dfs(TreeNode node) {
        if (node == null) return 0;

        int leftMoves = dfs(node.left);
        int rightMoves = dfs(node.right);
        this.numMoves += Math.abs(leftMoves) + Math.abs(rightMoves);
        return node.val - 1 + leftMoves + rightMoves;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(0, new TreeNode(3), new TreeNode(0));

        coinDistrBinTree t = new coinDistrBinTree();

        System.out.println(t.distributeCoins(root));
    }
}
