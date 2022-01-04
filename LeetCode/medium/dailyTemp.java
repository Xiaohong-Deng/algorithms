package LeetCode.medium;
import java.util.Deque;
import java.util.ArrayDeque;

public class dailyTemp {
    public int[] waitDays(int[] temp) {
        Deque<Integer> stack = new ArrayDeque<>();
        int[] days = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            while (stack.size() != 0 && temp[stack.peek()] < temp[i]) {
                int topIdx = stack.pop();
                days[topIdx] = i - topIdx;
            }
            stack.push(i);
        }

        return days;
    }

    public static void main(String[] args) {
        int[] temp = {73, 74, 75, 71, 69, 72, 76, 73};
        dailyTemp test = new dailyTemp();
        int[] ans = test.waitDays(temp);
        for (int i : ans) {
            System.out.println(i);
        }
    }
}
