package LeetCode.medium;

public class containerMostWater {
    public int maxArea(int[] height) {
        int i, j, shorter, maxA, curA;
        i = 0;
        j = height.length - 1;
        shorter = Math.min(height[i], height[j]);

        maxA = (j - i) * shorter;

        while (i < j) {
            if (height[i] < height[j]) {
                i++;
            } else {
                j--;
            }

            shorter = Math.min(height[i], height[j]);

            curA = (j - i) * shorter;
            if (curA > maxA) {
                maxA = curA;
            }
        }

        return maxA;

    }

    public static void main(String[] args) {
        int[] height = {1,8,6,2,5,4,8,3,7};
        containerMostWater test = new containerMostWater();

        System.out.println(test.maxArea(height));
    }
}
