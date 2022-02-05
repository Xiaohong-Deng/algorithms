package LeetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class combSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> tempSoln = new ArrayList<>();

        int maxDepth = candidates.length;

        for (int i = 0; i < maxDepth; i++) {
            tempSoln.add(candidates[i]);
            dfs(ans, candidates, tempSoln, i, target - candidates[i], maxDepth);
            tempSoln.remove(tempSoln.size() - 1);
        }

        return ans;
    }

    // if target is 10 and we have 1 and 9, when return from 9, we do not return from 1 because there might be 4 and 5
    // there is no early termination. And all numbers are positive so once find one sol we return
    // we can use the number multiple times so it is not a problem where we decide if we take a number or not
    // it is another branch and bound where for each position we take one of the numbers available and go to next position until target is below 0 or 0
    // the number of positions are infinite;
    private void dfs(List<List<Integer>> ans, int[] candidates, List<Integer> tempSoln, int start, int target, int maxDepth) {
        if (target == 0) {
            List<Integer> sol = new ArrayList<>();
            for (Integer i : tempSoln) {
                sol.add(i);
            }
            ans.add(sol);
            return;
        }

        if (target < 0) {
            return;
        }

        for (int i = start; i < maxDepth; i++) {
            tempSoln.add(candidates[i]);
            dfs(ans, candidates, tempSoln, i, target - candidates[i], maxDepth);
            tempSoln.remove(tempSoln.size() - 1);
        }
    }

    public static void main(String[] args) {
        int[] candidates = {2,3,6,7};
        int target = 7;

        combSum test = new combSum();
        List<List<Integer>> ans = test.combinationSum(candidates, target);
        for (List<Integer> list : ans) {
            System.out.print('[');
            for (int i : list) {
                System.out.print(i + ", ");
            }
            System.out.print(']');
            System.out.println();
        }
    }
}
