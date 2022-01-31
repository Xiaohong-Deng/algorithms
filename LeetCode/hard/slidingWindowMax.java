package LeetCode.hard;

import java.util.ArrayDeque;
import java.util.Deque;

public class slidingWindowMax {
    public int[] findMax(int[] nums, int k) {
        int[] ans = new int[nums.length - k + 1];

        Deque<Integer> decreasingDeque = new ArrayDeque<>();

        for (int i = 0; i < nums.length; i++) {
            // during constructing the first window checking head does not cause it to be removed
            // because it is decreasing deque if window start moving the head will be popped for sure
            if (decreasingDeque.size() != 0 && i - decreasingDeque.peek() >= k) {
                decreasingDeque.remove();
            }

            // now adding new number might break the invariant that the deque is decreasing
            // pop tail until the tail is larger than the new number
            while (decreasingDeque.size() != 0 && nums[decreasingDeque.peekLast()] <= nums[i]) {
                decreasingDeque.removeLast();
            }

            decreasingDeque.add(i);

            if (i >= k - 1) {
                ans[i - k + 1] = nums[decreasingDeque.peek()];
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] nums = {1,3,-1,-3,5,3,6,7};
        int k = 3;
        slidingWindowMax test = new slidingWindowMax();

        int[] ans = test.findMax(nums, k);
        for (int i : ans) {
            System.out.println(i);
        }
    }
}
