package LeetCode.contests.Mar5;

import java.util.HashMap;
import java.util.Map;

import LeetCode.medium.BSTIterator.TreeNode;

public class btreeDescription {
    public TreeNode createBinaryTree(int[][] descriptions) {
        Map<Integer, TreeNode> map = new HashMap<>();

        Map<Integer, Integer> parentMap = new HashMap<>();

        for (int[] is : descriptions) {
            int isLeft = is[2];
            int pVal = is[0];
            int cVal = is[1];

            TreeNode parent, child;

            if (map.containsKey(cVal)) {
                child = map.get(cVal);
            } else {
                child = new TreeNode(cVal, null, null);
            }

            if (map.containsKey(pVal)) {
                parent = map.get(pVal);
            } else {
                parent = new TreeNode(pVal, null, null);
            }

            if (isLeft == 1) {
                parent.left = child;
            } else {
                parent.right = child;
            }

            if (!map.containsKey(pVal))
                map.put(pVal, parent);
            if (!map.containsKey(cVal))
                map.put(cVal, child);

            parentMap.put(cVal, pVal);
        }

        for (Integer i : map.keySet()) {
            if (!parentMap.containsKey(i)) {
                return map.get(i);
            }
        }

        return null;
    }
}
