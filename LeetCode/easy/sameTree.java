package LeetCode.easy;

import LeetCode.easy.invertBinaryTree.TreeNode;;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 **/
public class sameTree {
  public boolean isSameTree(TreeNode p, TreeNode q) {
    if (p == null) {
      if (q == null) return true;
      else return false;
    } else if (q == null) {
      return false;
    }
    if (p.val == q.val) {
      return (isSameTree(p.left, q.left) && isSameTree(p.right, q.right));
    }
    return false;
  }
}