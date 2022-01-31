package LeetCode.easy;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class invertBinaryTree {
  public static class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
  }
  public TreeNode invertTree(TreeNode root) {
    if (root != null) {
      TreeNode temp = root.left;
      root.left = root.right;
      root.right = temp;
      root.left = invertTree(root.left);
      root.right = invertTree(root.right);
    }
    return root;
  }
}