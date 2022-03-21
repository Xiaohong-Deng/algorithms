package LeetCode.medium;

public class houseRobber2 {
    private int[][] cache;
    public int rob(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return nums[0];
        }

        if (len == 2) {
            return Math.max(nums[0], nums[1]);
        }
        cache = new int[2][len];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < len; j++) {
                cache[i][j] = -1;
            }
        }
        return Math.max(nums[len - 1] + maxAmount(nums, 1, len - 3), maxAmount(nums, 0, len - 2));
    }

    private int maxAmount(int[] nums, int start, int end) {
        if (start > end) {
            return 0;
        }
        // if we have one house
        if (start == end) {
            return nums[start];
        }

        // if we have two adjacent houses
        if (start == end - 1) {
            if (cache[start][end] == -1)
                cache[start][end] = Math.max(nums[start], nums[end]);
            return cache[start][end];
        }

        // general case
        if (cache[start][end] == -1) {
            cache[start][end] = Math.max(nums[end] + maxAmount(nums, start, end - 2), maxAmount(nums, start, end - 1));
        }

        return cache[start][end];
    }

    public static void main(String[] args) {
        int[] nums = {1,2,3,1};
        houseRobber2 t = new houseRobber2();
        System.out.println(t.rob(nums));
    }
}
