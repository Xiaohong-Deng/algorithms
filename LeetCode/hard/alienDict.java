package LeetCode.hard;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;

public class alienDict {
    private int label;
    //  1 <= words.length <= 100
    // 1 <= words[i].length <= 100
    // words[i] consists of only lowercase English letters.

    // a -> b -> c vs. a -> b -> c, a -> c generate the same topo order 
    public String alienOrder(String[] words) {
        HashMap<Character, HashSet<Character>> adjList = new HashMap<>();

        HashSet<Character> nodes = new HashSet<>();

        buildNodes(words, nodes);

        boolean followedByPrefix = buildGraph(words, adjList);

        if (followedByPrefix) {
            return "";
        }

        if (hasCycle(nodes, adjList)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        int[] labels = new int[26];

        char[] alphabet = new char[nodes.size()];

        topoSort(nodes, adjList, labels);

        for (char c : nodes) {
            alphabet[labels[c - 'a'] - 1] = c;
        }

        for (char c : alphabet) {
            sb.append(c);
        }

        return sb.toString();
    }

    private void buildNodes(String[] words, HashSet<Character> nodes) {
        for (int i = 0; i < words.length; i++) {
            for (char c : words[i].toCharArray()) {
                nodes.add(c);
            }
        }
    }

    private boolean buildGraph(String[] words, HashMap<Character, HashSet<Character>> adjList) {
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];

            if (w1.length() > w2.length() && w1.startsWith(w2)) {
                return true;
            }

            int commLen = Math.min(w1.length(), w2.length());

            for (int j = 0; j < commLen; j++) {
                char c1 = w1.charAt(j);
                char c2 = w2.charAt(j);

                if (c1 == c2) {
                    continue;
                }

                // the first time we encounter different chars it is the only factor that decides the order
                // so after that we break;

                if (adjList.containsKey(c1)) {
                    adjList.get(c1).add(c2);
                } else {
                    HashSet<Character> neighbors = new HashSet<>();
                    neighbors.add(c2);
                    adjList.put(c1, neighbors);
                }

                break;
            }
        }

        return false;
    }

    private void topoSort(HashSet<Character> nodes, HashMap<Character, HashSet<Character>> adjList, int[] labels) {
        label = nodes.size();

        boolean[] explored = new boolean[26];
        for (char c : nodes) {
            dfs(c, adjList, explored, labels);
        }
    }

    private void dfs(char c, HashMap<Character, HashSet<Character>> adjList, boolean[] explored, int[] labels) {
        if (explored[c - 'a']) {
            return;
        }

        explored[c - 'a'] = true;

        if (adjList.containsKey(c)) {
            for (char nb : adjList.get(c)) {
                dfs(nb, adjList, explored, labels);
            }
        }
        
        labels[c - 'a'] = label--;

    }

    private boolean hasCycle(HashSet<Character> nodes, HashMap<Character, HashSet<Character>> adjList) {
        Deque<Character> recStack = new ArrayDeque<>();
        boolean[] explored = new boolean[26];
        for (Character c : nodes) {
            if (dfs(c, adjList, recStack, explored)) {
                return true;
            }
        }

        return false;
    }

    private boolean dfs(char c, HashMap<Character, HashSet<Character>> adjList, Deque<Character> recStack, boolean[] explored) {
        if (recStack.contains(c)) {
            return true;
        }

        if (explored[c - 'a']) {
            return false;
        }

        recStack.add(c);
        explored[c - 'a'] = true;

        if (adjList.containsKey(c)) {
            HashSet<Character> neighbors = adjList.get(c);
            for (Character nb : neighbors) {
                if (dfs(nb, adjList, recStack, explored)) {
                    return true;
                }
            }
        }

        recStack.removeLast();

        return false;
    }

    public static void main(String[] args) {
        String[] words1 = {"wrt","wrf","er","ett","rftt"};
        String[] words2 = {"z","x","z"};
        String[] words3 = {"abc", "ab"};

        alienDict t = new alienDict();

        System.out.println(t.alienOrder(words1));
        System.out.println(t.alienOrder(words2));
        System.out.println(t.alienOrder(words3));
    }
}
