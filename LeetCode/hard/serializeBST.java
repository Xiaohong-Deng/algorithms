package LeetCode.hard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import LeetCode.medium.BSTIterator;
import LeetCode.medium.BSTIterator.TreeNode;

public class serializeBST {
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serialize(root, sb);
        return sb.toString();
    }

    public TreeNode deserialize(String data) {
        String[] strArray = data.split(",");
        List<String> strList = new ArrayList<>(Arrays.asList(strArray));
        return deserialize(strList);
    }

    private void serialize(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("null,");
        } else {
            sb.append(String.valueOf(node.val) + ",");
            serialize(node.left, sb);
            serialize(node.right, sb);
        }
    }

    private TreeNode deserialize(List<String> strList) {
        // if null must remove it from the list because the null might be a child indicating no more DFS in this direction
        if (strList.get(0).equals("null")) {
            strList.remove(0);
            return null;
        }

        TreeNode node = new TreeNode(Integer.parseInt(strList.get(0)));
        strList.remove(0);
        node.left = deserialize(strList);
        node.right = deserialize(strList);
        return node;
    }
}