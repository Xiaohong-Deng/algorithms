package LeetCode.contests.Mar12;

// if we have k < nums.length, we can take k - 1 nums out and add the largest one back so first k - 1 elements are candidate
// kth element can never be candidate, either we take it out or we add one on top of it.
// k + 1 th element is candidate we can make it top by taking out k elements

// What are the corner cases? 
// If k == num.length we have no k + 1 th element, so just max of k - 1 elements
// If k == 0, return nums[0], if k == 1, return nums[1]. In both cases we have empty removed
// A standalone case is k % 2 == 1 and nums.length == 1, in that case empty pile is not avoidable.
public class p3 {
    public int maximumTop(int[] nums, int k) {
        if (nums.length == 1 && k % 2 == 1) {
            return -1;
        }
        
        if (k == 0) {
            return nums[0];
        }

        int max_removed = -1;

        int upperBound, ptr;
        ptr = 0;

        if (k - 1 < nums.length) {
            upperBound = k - 1; // if k == 0, k - 1 = -1, empty removed, so is k == 1
        } else {
            upperBound = nums.length;
        }

        for (int i = 0; i < upperBound; i++) {
            if (nums[i] > max_removed) {
                max_removed = nums[i];
            }
            ptr++;
        }

        // now we have taken k - 1 elements away, if (k + 1)th element is largest, we should take kth, if not we add largest in the pq
        if (ptr + 1 < nums.length && max_removed >= 0) {
            return Math.max(nums[ptr + 1], max_removed);
        } else if (max_removed < 0) {
            // ptr points at the current top
            // if empty removed but no moves left, we have no elements to remove, the current top is the final top, return nums[ptr]
            // if empty removed but 1 move left, we must remove the last one, the one after the current top is the true top, return nums[ptr + 1]
            return nums[ptr + 1];
        } else { // ptr >= nums.length - 1, nums has one left or empty, recall the kth cannot be top, so max of removed elements
            return max_removed;
        }
        
    }

    public static void main(String[] args) {
        // int[] nums = {35,43,23,86,23,45,84,2,18,83,79,28,54,81,12,94,14,0,0,29,94,12,13,1,48,85,22,95,24,5,73,10,96,97,72,41,52,1,91,3,20,22,41,98,70,20,52,48,91,84,16,30,27,35,69,33,67,18,4,53,86,78,26,83,13,96,29,15,34,80,16,49};
        p3 t = new p3();
        int[] nums2 = {68,76,53,73,85,87,58,24,48,59,38,80,38,65,90,38,45,22,3,28,11};
        System.out.println(t.maximumTop(nums2, 59));
        int[] nums3 = {2};
        System.out.println(t.maximumTop(nums3, 3));
    }
}
