package LeetCode.medium;

import java.util.concurrent.ThreadLocalRandom;

public class findDup {
    // there is exactly one dup
    // for a pivot < dup num, swap results in rank < nums[rank], e.g., 0 < rank[0] (1)
    // for a pivot >= dup num, swa results in rank = nums[rank].
    // but dup can be anywhere on the left and pivot might not be the dup num
    // but we continue until the final element we get one element left we get the dup
    public int findDuplicate(int[] nums) {
        int start, end;
        start = 0;
        end = nums.length - 1;
        int rank = rselect(nums, start, end);

        while (rank == nums.length - 1 || nums[rank] != nums[rank + 1]) {
            // when start = end - 1, two elements, rank would be either start or end, so eventually any index in [0, len - 1] can be reached
            // once dup is reached it would put its dup to the left and go left, and the following search always go right until [small, dup]
            // in case small is pivot next space 1 search can not work so we need special handling here
            // if we use while (rank == i) we do not need the handling because rank can touch all the indices if needs be
            if (rank < nums[rank]) {
                // go right
                start = rank + 1;
                // being defensive but while loop condition ensures no start > end scenario
                if (start > end) {
                    break;
                } else {
                    rank = rselect(nums, start, end);
                }
            } else {
                // go left
                end = rank - 1;
                if (start > end) {
                    break;
                } else {
                    rank = rselect(nums, start, end);
                }
            }
        }
        return nums[rank];
    }

    private int rselect(int[] nums, int start, int end) {
        if (start == end) {
            return start;
        }
        int pivot = ThreadLocalRandom.current().nextInt(start, end + 1);

        int temp = nums[start];
        nums[start] = nums[pivot];
        nums[pivot] = temp;

        int i, j;
        i = j = start + 1;
        while (j <= end) {
            if (nums[j] <= nums[start]) {
                temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
                i++;
            }
            j++;
        }

        temp = nums[i - 1];
        nums[i - 1] = nums[start];
        nums[start] = temp;
        return i - 1;
    }

    public static void main(String[] args) {
        int[] nums = {4,3,1,4,2};
        findDup t = new findDup();
        System.out.println(t.findDuplicate(nums));
    }
}
