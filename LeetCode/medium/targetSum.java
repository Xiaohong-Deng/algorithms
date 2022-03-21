package LeetCode.medium;

import java.util.Arrays;

public class targetSum {
    private int[][] cache;
    private int total;
    public int findTargetSumWays(int[] nums, int target) {
        total = Arrays.stream(nums).sum();
        // nums[i] can only be positive or zero
        cache = new int[nums.length][2 * total + 1];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < 2 * total + 1; j++) {
                cache[i][j] = Integer.MIN_VALUE;
            }
        }
        return findTarget(nums, 0, 0, target);
    }

    private int findTarget(int[] nums, int start, int sum, int target) {
        if (start == nums.length - 1) {
            if (target == sum + nums[start] && nums[start] == 0) {
                return 2;
            }
            if (target == sum + nums[start] || target == sum - nums[start]) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (cache[start][sum + total] != Integer.MIN_VALUE) {
                return cache[start][sum + total];
            }

            int sol1, sol2;
            sol1 = findTarget(nums, start + 1, sum + nums[start], target);
            sol2 = findTarget(nums, start + 1, sum - nums[start], target);

            cache[start][sum + total] = sol1 + sol2;
            return cache[start][sum + total];
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 0};
        int target = 1;
        targetSum t = new targetSum();
        System.out.println(t.findTargetSumWays(nums, target));
    }
}
