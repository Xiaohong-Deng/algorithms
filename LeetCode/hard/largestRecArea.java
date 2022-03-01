package LeetCode.hard;
import java.util.Stack;

// basic idea is for a bar, look to its left until reach a bar lower than it, to its right do the same
// then the recArea is bar_height * (right_bound - left_bound)
public class largestRecArea {
    public int largestRectangleArea(int[] heights) {
        int max_area = 0;
        Stack<Integer> stack = new Stack<>();
        int top, height, right, left;

        // keep a nondecreasing stack
        // because if non-decreasing the right bound for every bar in the nondecreasing stack is unkown, but left bar is always the one before it
        // once decreasing bar shows up, for some bars the lefr and right bounds are clear, so we process such bars
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

        // now we have a nondecreasing stack
        // if the last bar is not the real last bar in array it must be that the bar higher and follow it are popped by some lower bar, but where is that lower bar should it be in the stack?
        // so the last bar is the real last bar and it is higher than the remaining bars in the stack so those bars has right idx being array length
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
