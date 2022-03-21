package LeetCode.medium;

import java.util.HashMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class courseSchedule {
    private boolean isExplored[];
    private HashMap<Integer, ArrayList<Integer>> adjList;
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        isExplored = new boolean[numCourses];
        for (int i = 0; i < numCourses; i++) {
            isExplored[i] = false;
        }
        adjList = new HashMap<>();

        // directed graph, is[1] -> is[0], meaning b must be taken before a
        for (int[] is : prerequisites) {
            ArrayList<Integer> neighbors = adjList.getOrDefault(is[1], new ArrayList<>());
            neighbors.add(is[0]);
            adjList.put(is[1], neighbors);
        }

        Deque<Integer> recStack = new ArrayDeque<>();

        for (int i = 0; i < numCourses; i++) {
            if (hasCycle(i ,recStack)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasCycle(int node, Deque<Integer> recStack) {
        if (recStack.contains(node)) {
            return true;
        }

        if (isExplored[node]) {
            return false;
        }

        if (!adjList.containsKey(node)) {
            return false;
        }

        recStack.add(node);

        // if neighbors, continue dfs, after all dfs, mark node and return
        for (int i : adjList.get(node)) {
            if (!isExplored[i]) {
                if(hasCycle(i, recStack)) {
                    return true;
                }
            }
        }

        // the definition of explored is very important here we want mark it explored after we retreat from it
        // otherwise we can tell if an explored node is on the recStack
        isExplored[node] = true;
        recStack.removeLast();
        return false;
    }

    public static void main(String[] args) {
        int[][] courses= {{1,0}, {0,1}};
        courseSchedule t = new courseSchedule();
        System.out.println(t.canFinish(2, courses));
    }
}
