package LeetCode.easy;

import java.util.ArrayDeque;
import java.util.Deque;

public class validParens {
    public boolean isValid(String s) {
        Deque<Character> dq = new ArrayDeque<>();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == '}' || c == ']' || c == ')') {
                if (dq.isEmpty() || (c == '}' && dq.peekLast() != '{') || (c == ')' && dq.peekLast() != '(') || (c == ']' && dq.peekLast() != '[')) {
                    return false;
                } else {
                    dq.pollLast();
                }
            } else {
                dq.add(c);
            }
        }

        if (!dq.isEmpty()) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        String s = "()[]{}";
        String s2 = "(]";
        String s3 = "({)}";
        String s4 = "({})";
        validParens t = new validParens();
        System.out.println(t.isValid(s));
        System.out.println(t.isValid(s2));
        System.out.println(t.isValid(s3));
        System.out.println(t.isValid(s4));
    }
}
