package LeetCode.medium;

import java.util.Arrays;

public class houseRobber {
    private int[] cache;
    public int rob(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return nums[0];
        }

        if (len == 2) {
            return Math.max(nums[0], nums[1]);
        }
        cache = new int[len];
        Arrays.fill(cache, -1);
        return Math.max(nums[len - 1] + maxAmount(nums, 0, len - 3), maxAmount(nums, 0, len - 2));
    }

    private int maxAmount(int[] nums, int start, int end) {
        if (start == end) {
            return nums[start];
        }

        if (start == end - 1) {
            if (cache[end] == -1)
                cache[end] = Math.max(nums[start], nums[end]);
            return cache[end];
        }

        if (cache[end] == -1) {
            cache[end] = Math.max(nums[end] + maxAmount(nums, start, end - 2), maxAmount(nums, start, end - 1));
        }

        return cache[end];
    }

    public static void main(String[] args) {
        int[] nums = {1,2,3,1};
        houseRobber t = new houseRobber();
        System.out.println(t.rob(nums));
    }
}
