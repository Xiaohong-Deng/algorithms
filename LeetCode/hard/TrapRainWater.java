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

    public int trapTwoArray(int[] height) {
        int ans = 0;
        if (height.length == 0) return ans;

        int[] prefixMax = new int[height.length];
        int[] suffixMax = new int[height.length];

        prefixMax[0] = height[0];
        for (int i = 1; i < prefixMax.length; i++) {
            if (height[i] > prefixMax[i - 1]) {
                prefixMax[i] = height[i];
            } else {
                prefixMax[i] = prefixMax[i - 1];
            }
        }

        suffixMax[height.length - 1] = height[height.length - 1];
        for (int i = suffixMax.length - 2; i >= 0; i--) {
            if (height[i] > suffixMax[i + 1]) {
                suffixMax[i] = height[i];
            } else {
                suffixMax[i] = suffixMax[i + 1];
            }
        }

        for (int i = 0; i < height.length; i++) {
            ans += Math.min(prefixMax[i], suffixMax[i]) - height[i];
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] height = {0,1,0,2,1,0,1,3,2,1,2,1};
        TrapRainWater test = new TrapRainWater();

        System.out.println(test.trap(height));
        System.out.println(test.trapTwoArray(height));
    }
}
