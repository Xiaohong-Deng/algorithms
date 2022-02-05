package LeetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class subset {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();

        List<Integer> temp = new ArrayList<>();

        if (nums.length == 0) {
            return ans;
        }

        ans.add(new ArrayList<>(temp));

        for (int i = 0; i < nums.length; i++) {
            temp.add(nums[i]);
            dfs(ans, temp, nums, i + 1, nums.length);
            temp.remove(temp.size() - 1);
        }

        return ans;
    }

    private void dfs(List<List<Integer>> ans, List<Integer> temp, int[] nums, int start, int len) {
        ans.add(new ArrayList<>(temp));

        if (start >= len) {
            return;
        }

        for (int i = start; i < len; i++) {
            temp.add(nums[i]);
            dfs(ans, temp, nums, i + 1, len);
            temp.remove(temp.size() - 1);
        }
    }

    public static void main(String[] args) {
        int[] nums = {1,2,3};
        subset test = new subset();

        List<List<Integer>> ans = test.subsets(nums);

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
