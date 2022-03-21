package LeetCode.medium;
import LeetCode.medium.BSTIterator.TreeNode;

public class validBST {
    public boolean isValidBST(TreeNode root) {
        boolean isValidLeft = false;
        boolean isValidRight = false;
        boolean isValid = true;
        if (root == null) {
            isValid = true;
            return isValid;
        }

        isValidLeft = isValidBST(root.left);
        isValidRight = isValidBST(root.right);

        if (isValidLeft && isValidRight) {
            // now try to see if at the current node it is valid
            if (root.left != null && (root.left.val >= root.val || maxVal(root.left) >= root.val)) {
                isValid = false;
            }
            
            if (root.right != null && (root.right.val <= root.val || minVal(root.right) <= root.val)) {
                isValid = false;
            }

            return isValid;
        } else {
            return false;
        }
        
    }

    // assume node is root of a valid tree
    private int minVal(TreeNode node) {
        if (node.left == null) {
            return node.val;
        } else {
            return minVal(node.left);
        }
    }

    private int maxVal(TreeNode node) {
        if (node.right == null) {
            return node.val;
        } else {
            return maxVal(node.right);
        }
    }
}
