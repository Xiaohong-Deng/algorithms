package LeetCode.medium;
import java.util.Deque;
import java.util.ArrayDeque;


public class oneThreeTwo {
    public boolean find132pattern(int[] nums) {
        Deque<Integer> stack = new ArrayDeque<>();
        int[] prefixMin = new int[nums.length];
        prefixMin[0] = nums[0];

        // keep best i candidate for nums[j]
        for (int i = 1; i < prefixMin.length; i++) {
            prefixMin[i] = Math.min(prefixMin[i - 1], nums[i]);
        }

        // stack is decreasing, has candidates of k
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] == prefixMin[i]) {
                continue;
            }

            // stack top is the element after nums[i] in nums
            while (stack.size() != 0 && nums[i] > stack.peekLast()) {
                int top = stack.pollLast();
                if (top > prefixMin[i]) {
                    return true;
                }
            }

            stack.add(nums[i]);
        }

        return false;
    }

    public boolean find132patternsNoStack(int[] nums) {
        int[] prefixMin = new int[nums.length];
        prefixMin[0] = nums[0];

        // keep best i candidate for nums[j]
        for (int i = 1; i < prefixMin.length; i++) {
            prefixMin[i] = Math.min(prefixMin[i - 1], nums[i]);
        }

        for (int i = nums.length - 1, k = nums.length; i >= 0; i--) {
            if (nums[i] > prefixMin[i]) {
                while (k < nums.length && nums[k] <= prefixMin[i]) {
                    k++;
                }
    
                if (k < nums.length && nums[k] < nums[i]) {
                    return true;
                }

                nums[--k] = nums[i];
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 4};
        int[] nums2 = {3, 1, 4, 2};
        int[] nums3 = {-1, 3, 2, 0};
        int[] nums4 = {1, 0, 1, -4, -3};
        int[] nums5 = {-2, 1, 2, -2, 1, 2};
        oneThreeTwo test = new oneThreeTwo();
        System.out.println(test.find132pattern(nums1));
        System.out.println(test.find132pattern(nums2));
        System.out.println(test.find132pattern(nums3));
        System.out.println(test.find132pattern(nums4));
        System.out.println(test.find132pattern(nums5));
        System.out.println(test.find132patternsNoStack(nums1));
        System.out.println(test.find132patternsNoStack(nums2));
        System.out.println(test.find132patternsNoStack(nums3));
        System.out.println(test.find132patternsNoStack(nums4));
        System.out.println(test.find132patternsNoStack(nums5));
    }
}
