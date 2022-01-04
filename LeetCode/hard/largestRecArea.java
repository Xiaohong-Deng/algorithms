package LeetCode.hard;
import java.util.Stack;

public class largestRecArea {
    public int largestRectangleArea(int[] heights) {
        int max_area = 0;
        Stack<Integer> stack = new Stack<>();
        int top, height, right, left;

        for (int i = 0; i < heights.length; i++) {
            while (!stack.empty() && heights[stack.peek()] > heights[i]) {
                top = stack.pop();
                height = heights[top];
                right = i;
                if (!stack.empty()) {
                    left = stack.peek() + 1;
                } else {
                    left = 0;
                }
                int area = height * (right - left);
                if (area > max_area) {
                    max_area = area;
                }
            }
            stack.push(i);
        }

        while (!stack.empty()) {
            top = stack.pop();
            height = heights[top];
            right = heights.length;
            if (!stack.empty()) {
                left = stack.peek() + 1;
            } else {
                left = 0;
            }
            int area = height * (right - left);
            if (area > max_area) {
                max_area = area;
            }
        }

        return max_area;
    }

    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6, 2, 3};
        largestRecArea test = new largestRecArea();
        System.out.println(test.largestRectangleArea(heights));
    }
}
