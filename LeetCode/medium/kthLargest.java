package LeetCode.medium;

import java.util.concurrent.ThreadLocalRandom;

public class kthLargest {
    public int findKth(int[] nums, int k) {
        // random select, O(n) runtime
        int i = nums.length - k;

        int start, end, pivot, rank;
        start = 0;
        end = nums.length - 1;

        pivot = ThreadLocalRandom.current().nextInt(start, end + 1);

        rank = swap(nums, start, end, pivot);

        while (rank != i) {
            if (rank < i) {
                start = rank + 1;
                pivot = ThreadLocalRandom.current().nextInt(start, end + 1);
                rank = swap(nums, start, end, pivot);
            } else {
                end = rank - 1;
                pivot = ThreadLocalRandom.current().nextInt(start, end + 1);
                rank = swap(nums, start, end, pivot);
            }
        }

        return nums[rank];
    }

    private int swap(int[] nums, int start, int end, int pivot) {
        int i, j, temp;
        temp = nums[start];
        nums[start] = nums[pivot];
        nums[pivot] = temp;
        i = j = start + 1;

        while (j <= end) {
            if (nums[j] <= nums[start]) {
                temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
                i++;
                j++;
            } else {
                j++;
            }
        }

        temp = nums[i - 1];
        nums[i - 1] = nums[start];
        nums[start] = temp;

        return i - 1;
    }

    public static void main(String[] args) {
        int[] nums1 = {3,2,1,5,6,4};
        int[] nums2 = {3,2,3,1,2,4,5,5,6};
        int k1 = 2;
        int k2 = 4;

        kthLargest test = new kthLargest();

        System.out.println(test.findKth(nums1, k1));
        System.out.println(test.findKth(nums2, k2));
    }
}
