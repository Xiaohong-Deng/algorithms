package LeetCode.medium;

import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

public class courseSchedule2 {
    private int label;
    private int[] topoOrder;
    private boolean isExplored[];
    private HashMap<Integer, ArrayList<Integer>> adjList;
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        topoOrder = new int[numCourses];
        isExplored = new boolean[numCourses];
        for (int i = 0; i < numCourses; i++) {
            isExplored[i] = false;
        }
        label = numCourses - 1;
        adjList = new HashMap<>();

        for (int[] is : prerequisites) {
            ArrayList<Integer> neighbors = adjList.getOrDefault(is[1], new ArrayList<>());
            neighbors.add(is[0]);
            adjList.put(is[1], neighbors);
        }

        Deque<Integer> recStack = new ArrayDeque<>();

        for (int i = 0; i < numCourses; i++) {
            boolean hasCycle = dfs(i, recStack);
            if (hasCycle) {
                return new int[0];
            }
        }

        return topoOrder;
    }

    private boolean dfs(int node, Deque<Integer> recStack) {
        if (recStack.contains(node)) {
            return true;
        }
        // if explored do nothing
        if (isExplored[node]) {
            return false;
        }
        // if no neighbor, mark the node and return
        if (!adjList.containsKey(node)) {
            topoOrder[label] = node;
            label--;
            isExplored[node] = true;
            return false;
        }

        recStack.add(node);

        // if neighbors, continue dfs, after all dfs, mark node and return
        for (int i : adjList.get(node)) {
            if (!isExplored[i]) {
                boolean hasCycle = dfs(i, recStack);
                if (hasCycle) {
                    return true;
                }
            }
        }

        topoOrder[label] = node;
        label--;
        isExplored[node] = true;
        recStack.removeLast();
        return false;
    }

    public static void main(String[] args) {
        int numCourses = 4;
        int[][] prereqs = {{1,0}, {2,0}, {3,1}, {3,2}};
        int num = 2;
        int[][] prereqs2 = {{1, 0}, {0, 1}};
        int num1 = 1;
        int[][] prereqs3 = {};
        courseSchedule2 t = new courseSchedule2();
        System.out.println(Arrays.toString(t.findOrder(numCourses, prereqs)));
        System.out.println(Arrays.toString(t.findOrder(num, prereqs2)));
        System.out.println(Arrays.toString(t.findOrder(num1, prereqs3)));
    }
}
