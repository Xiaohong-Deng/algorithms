package LeetCode.easy;

import LeetCode.easy.invertBinaryTree.TreeNode;
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class maxDepthBinaryTree {
  public int maxDepth(TreeNode root) {
    return maxDepth(root, 0);
  }
  
  private int maxDepth(TreeNode t, int currentDepth) {
    if (t == null) return currentDepth;
    int leftDepth = maxDepth(t.left, currentDepth + 1);
    int rightDepth = maxDepth(t.right, currentDepth + 1);
    if (leftDepth > rightDepth) return leftDepth;
    return rightDepth;
  }
}