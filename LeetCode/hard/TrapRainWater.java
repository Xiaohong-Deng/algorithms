package LeetCode.hard;

public class TrapRainWater {
    public int trap(int[] height) {
        // For each height[i] if we know there is a higher bar on the left (left_max) and higher bar on the right (height[right]) and left high < right high, we can ensure ans += left_high - height[i]
        // if it is not the case we know for height[i] on the right, there is a higher bar on its left (height[left])
        int ans = 0;
        if (height.length == 0) return ans;
        
        int left, right, left_max, right_max;
        left = left_max = right_max = 0;
        right = height.length - 1;
        
        while (left < right) {
            if (height[left] > left_max) {
                left_max = height[left];
            }
            if (height[right] > right_max) {
                right_max = height[right];
            }
            if (left_max < right_max) {
                ans += left_max - height[left];
                left++;
            } else {
                ans += right_max - height[right];
                right--;
            }
        }
        
        return ans;
    }
}
