package LeetCode.medium;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class permutation {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();

        if (nums.length == 0) {
            return ans;
        }

        List<Integer> temp = new ArrayList<>();
        Set<Integer> addedIdx = new HashSet<>();

        for (int i = 0; i < nums.length; i++) {
            temp.add(nums[i]);
            addedIdx.add(i);
            dfs(ans, temp, addedIdx, nums, nums.length);
            temp.remove(temp.size() - 1);
            addedIdx.remove(i);
        }

        return ans;
    }

    private void dfs(List<List<Integer>> ans, List<Integer> temp, Set<Integer> addedIdx, int[] nums, int len) {
        if (temp.size() >= len) {
            ans.add(new ArrayList<Integer>(temp));
        }

        for (int i = 0; i < len; i++) {
            if (!addedIdx.contains(i)) {
                temp.add(nums[i]);
                addedIdx.add(i);
                dfs(ans, temp, addedIdx, nums, len);
                temp.remove(temp.size() - 1);
                addedIdx.remove(i);
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {1,2,3};
        permutation test = new permutation();
        List<List<Integer>> ans = test.permute(nums);

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
