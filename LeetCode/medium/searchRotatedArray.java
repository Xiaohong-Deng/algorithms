package LeetCode.medium;

public class searchRotatedArray {
    // if it is a sorted array we just use binary search to find target
    // but it is rotated we have two sorted segments we just need to find where they meet, i.e., the largest and the smallest
    // elements in the array. then find target in that sorted segment
    public int search(int[] nums, int target) {
        int len = nums.length;
        if (len == 1) {
            return target == nums[0] ? 0 : -1;
        }
        if (nums[0] < nums[len - 1]) {
            return bsearch(target, 0, len - 1, nums);
        }
        int smallIdx;

        smallIdx = bsearchSmall(0, len - 1, nums);
        
        if (target >= nums[smallIdx] && target <= nums[len - 1]) {
            return bsearch(target, smallIdx, len - 1, nums);
        } else if (target >= nums[0] && target <= nums[smallIdx - 1]) {
            return bsearch(target, 0, smallIdx - 1, nums);
        } else {
            return -1;
        }
    }

    private int bsearch(int target, int start, int end, int[] nums) {
        if (start > end) {
            return -1;
        }

        int mid = (start + end) / 2;

        if (nums[mid] == target) {
            return mid;
        } else if (nums[mid] > target) {
            end = mid - 1;
            return bsearch(target, start, end, nums);
        } else {
            start = mid + 1;
            return bsearch(target, start, end, nums);
        }
    }

    private int bsearchSmall(int start, int end, int[] nums) {
        if (start == end) {
            return nums[start];
        }

        // if start = 0 and end = 1, mid = 0 so we need to prevent out of bound error
        int prev, succ, mid;
        mid = (start + end) / 2;
        if (mid == 0) {
            prev = nums.length - 1;
        } else {
            prev = mid - 1;
        }
        
        if (mid == nums.length - 1) {
            succ = 0;
        } else {
            succ = mid + 1;
        }
        
        if (nums[mid] > nums[prev] && nums[mid] > nums[succ]) {
            return succ;
        } else if (nums[mid] < nums[prev] && nums[mid] < nums[succ]) {
            return mid;
        } else if (nums[mid] < nums[start]) {
            end = mid - 1;
            return bsearchSmall(start, end, nums);
        } else {
            // corner case nums[mid] = nums[start], in such case we also need to go right
            start = mid + 1;
            return bsearchSmall(start, end, nums);
        }
    }

    public static void main(String[] args) {
        int[] nums = {3, 1};
        searchRotatedArray t = new searchRotatedArray();
        System.out.println(t.search(nums, 0));
    }
}
