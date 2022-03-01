package LeetCode.hard;

public class reversePairs {
    public int reverse(int[] nums) {
        int count = 0;
        int mid = (nums.length + 0) / 2;
        count += sort(nums, 0, mid);
        count += sort(nums, mid + 1, nums.length - 1);
        count += merge(nums, 0, mid, nums.length - 1);
        return count;
    }

    private int sort(int[] nums, int start, int end) {
        if (start >= end) {
            return 0;
        }
        int count = 0;
        int mid = (end + start) / 2;
        count += sort(nums, start, mid);
        count += sort(nums, mid + 1, end);
        count += merge(nums, start, mid, end);

        return count;
    }

    private int merge(int[] nums, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        // assume [start, mid], [mid + 1, end] are sorted
        int i, j, k, count;
        i = start;
        j = mid + 1;
        count = k = 0;
        while (i <= mid && j <= end) {
            if ((long) nums[i] > (long) 2 * nums[j]) {
                count += mid - i + 1;
                j++;
            } else {
                i++;
            }
        }

        i = start;
        j = mid + 1;

        while (i <= mid && j <= end) {
            if (nums[i] > nums[j]) {
                temp[k++] = nums[j++];
            } else {
                temp[k++] = nums[i++];
            }
        }

        while (i <= mid) {
            temp[k++] = nums[i++];
        }

        while (j <= end) {
            temp[k++] = nums[j++];
        }

        System.arraycopy(temp, 0, nums, start, temp.length);

        return count;
    }
}
