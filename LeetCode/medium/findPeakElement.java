package LeetCode.medium;

public class findPeakElement {
    public int findPeak(int[] nums) {
        int start, end;
        start = 0;
        end = nums.length - 1;
        return bsearch(nums, start, end);
    }
    
    private int bsearch(int[] nums, int start, int end) {
        if (start == end) {
            return start;
        }
        
        int mid = (start + end) / 2;
        if (mid == 0) {
            if (nums[mid] > nums[mid + 1]) {
                return mid;
            } else {
                return bsearch(nums, mid + 1, end);
            }
        } else if (mid == nums.length - 1) {
            if (nums[mid] > nums[mid - 1]) {
                return mid;
            } else {
                return bsearch(nums, start, mid - 1);
            }
        } else {
            if (nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) {
                return mid;
            } else if (nums[mid] > nums[mid - 1] && nums[mid] < nums[mid + 1]) {
                return bsearch(nums, mid + 1, end);
            } else {
                return bsearch(nums, start, mid - 1);
            }    
        }
    }
}
