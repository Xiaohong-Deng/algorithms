package LeetCode.medium;

import java.util.Arrays;

public class threeSumClosest {
    public int getSum(int[] nums, int target) {
        int ans = nums[0] + nums[1] + nums[2];
        Arrays.sort(nums);

        int temp, comp, left, right;
        for (int i = 0; i < nums.length; i++) {
            comp = target - nums[i];
            left = i + 1;
            right = nums.length - 1;
            while (left < right) {
                temp = nums[left] + nums[right];
                if (Math.abs(comp - temp) < Math.abs(target - ans)) {
                    ans = nums[i] + temp;
                }

                if (temp < comp) {
                    left++;
                } else if (temp > comp) {
                    right--;
                } else {
                    return target;
                }
            }
        }

        return ans;
    }
}
