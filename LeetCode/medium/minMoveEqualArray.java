package LeetCode.medium;

import java.util.concurrent.ThreadLocalRandom;

public class minMoveEqualArray {
    public int minMoves2(int[] nums) {
        int median = findMedian(nums);
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            count += Math.abs(nums[i] - median);
        }

        return count;
    }

    private int findMedian(int[] nums) {
        int medianIdx = nums.length / 2;

        if (nums.length % 2 == 0) 
            return (rselect(medianIdx, nums) + rselect(medianIdx - 1, nums)) / 2;
        else
            return rselect(medianIdx, nums);
    }

    private int rselect(int target, int[] nums) {
        int start, end, pivot, rank;
        start = 0;
        end = nums.length - 1;
        pivot = ThreadLocalRandom.current().nextInt(start, end + 1);
        rank = swap(pivot, start, end, nums);

        while (rank != target) {
            if (rank < target) {
                start = rank + 1;
            } else {
                end = rank - 1;
            }

            pivot = ThreadLocalRandom.current().nextInt(start, end + 1);
            rank = swap(pivot, start, end, nums);
        }

        return nums[rank];
    }

    private int swap(int pivot, int start, int end, int[] nums) {
        int i, j, temp;

        temp = nums[pivot];
        nums[pivot] = nums[start];
        nums[start] = temp;

        i = j = start + 1;

        while (j <= end) {
            if (nums[j] < nums[start]) {
                temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
                i++;
            }
            j++;
        }

        temp = nums[start];
        nums[start] = nums[i - 1];
        nums[i - 1] = temp;

        return i - 1;
    }

    public static void main(String[] args) {
        int[] nums = {1,0,0,8,6};
        minMoveEqualArray t= new minMoveEqualArray();

        System.out.println(t.minMoves2(nums));
    }
}
