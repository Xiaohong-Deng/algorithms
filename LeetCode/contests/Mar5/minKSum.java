package LeetCode.contests.Mar5;

import java.util.Arrays;


public class minKSum {
    // to sum up 1 to 100, we use formula (100 + 1) * (100 / 2)
    // to sum n (exclusive) to n + k (exclusive), ((n + k) / 2) * (n + k + 1) - (n / 2) * (n + 1) - n + k, use double to avoid rounding when n or n + k is odd
    public long minimalKSum(int[] nums, int k) {
        Arrays.sort(nums);
        long sumK = 0;
        int low, high;
        low = 0;
        high = nums[0];

        if (k < high - low) {
            high = low + k + 1;
        }

        sumK += GaussianSum(low, high);
        k -= high - low - 1;

        if (k > 0) {
            for (int i = 1; i < nums.length; i++) {
                low = high;
                high = nums[i];

                if (high == low)
                    continue;

                if (k < high - low) {
                    high = low + k + 1;
                }

                sumK += GaussianSum(low, high);
                k -= high - low - 1;

                if (k <= 0) {
                    break;
                }
            }
        }

        if (k > 0) {
            low = high;
            high = low + k + 1;

            sumK += GaussianSum(low, high);
        }

        return sumK;
    }

    private long GaussianSum(int low, int high) {
        if (high - low <= 1) {
            return 0;
        }

        int first = low + 1;
        int last = high - 1;
        int num = high - low - 1;
        return (long) ((num / 2.0) * (first + last));
    }

    public static void main(String[] args) {
        // int[] nums = {96,44,99,25,61,84,88,18,19,33,60,86,52,19,32,47,35,50,94,17,29,98,22,21,72,100,40,84};
        int[] nums = {1000000000};
        int k = 1000000000;
        minKSum t = new minKSum();
        long ans = t.minimalKSum(nums, k);
        System.out.println(ans);
    }
}
