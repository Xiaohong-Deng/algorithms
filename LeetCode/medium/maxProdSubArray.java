package LeetCode.medium;

public class maxProdSubArray {
    public int maxProduct(int[] nums) {
        int maxPrefix, minPrefix, globalMax;
        maxPrefix = minPrefix = nums[0];
        globalMax = maxPrefix;

        for (int i = 1; i < nums.length; i++) {
            int pd1 = maxPrefix * nums[i];
            int pd2 = minPrefix * nums[i];
            maxPrefix = Math.max(Math.max(nums[i], pd1), pd2);
            minPrefix = Math.min(Math.min(nums[i], pd1), pd2);
            if (maxPrefix > globalMax) {
                globalMax = maxPrefix;
            }
        }
        
        return globalMax;
    }

    public static void main(String[] args) {
        int[] nums = {2,-5,-2,-4,3};
        maxProdSubArray t = new maxProdSubArray();
        System.out.println(t.maxProduct(nums));
    }
}
