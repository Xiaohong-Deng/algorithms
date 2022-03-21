package LeetCode.contests.Mar12;

import java.util.ArrayList;
import java.util.List;

class p1 {
    public List<Integer> findKDistantIndices(int[] nums, int key, int k) {
        List<Integer> ans = new ArrayList<>();
        int oldEnd, start, end;

        oldEnd = -1;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == key) {
                if (i - k < 0) {
                    start = 0;
                } else {
                    start = i - k;
                }

                if (i + k >= nums.length) {
                    end = nums.length - 1;
                } else {
                    end = i + k;
                }

                if (oldEnd > 0) {
                    if (start <= oldEnd) {
                        start = oldEnd + 1;
                    }
                }

                for (int j = start; j < end + 1; j++) {
                    ans.add(j);
                }

                oldEnd = end;
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] nums = {734,228,636,204,552,732,686,461,973,874,90,537,939,986,855,387,344,939,552,389,116,93,545,805,572,306,157,899,276,479,337,219,936,416,457,612,795,221,51,363,667,112,686,21,416,264,942,2,127,47,151,277,603,842,586,630,508,147,866,434,973,216,656,413,504,360,990,228,22,368,660,945,99,685,28,725,673,545,918,733,158,254,207,742,705,432,771,578,549,228,766,998,782,757,561,444,426,625,706,946};
        int key = 939;
        int k = 34;

        p1 t = new p1();
        System.out.println(t.findKDistantIndices(nums, key, k));
    }
}