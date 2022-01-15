package LeetCode.medium;
import java.util.Deque;
import java.util.ArrayDeque;

public class decodeString {    
    public String decode(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        StringBuilder ans = new StringBuilder();

        char cur, top;
        for (int i = 0; i < s.length(); i++) {
            cur = s.charAt(i);
            if (cur == ']') {
                StringBuilder strRev = new StringBuilder();
                StringBuilder timesRev = new StringBuilder();

                while (stack.peek() != '[') {
                    top = stack.pop();
                    strRev.append(top);
                }

                stack.pop();

                while (stack.size() != 0 && Character.isDigit(stack.peek())) {
                    top = stack.pop();
                    timesRev.append(top);
                }

                int t = Integer.parseInt(timesRev.reverse().toString());

                for (int j = 0; j < t; j++) {
                    for (int k = strRev.length() - 1; k >= 0; k--) {
                        stack.push(strRev.charAt(k));
                    }
                }
            } else {
                stack.push(cur);
            }
        }

        while (stack.size() != 0) {
            ans.append(stack.pop());
        }
        return ans.reverse().toString();
    }

    public static void main(String[] args) {
        String s1 = "3[a]2[bc]";
        String s2 = "3[a2[c]]";
        String s3 = "2[abc]3[cd]ef";

        decodeString test = new decodeString();

        System.out.println(test.decode(s1));
        System.out.println(test.decode(s2));
        System.out.println(test.decode(s3));
    }
}
