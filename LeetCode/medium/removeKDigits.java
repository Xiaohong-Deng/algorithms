package LeetCode.medium;

import java.util.ArrayDeque;
import java.util.Deque;

public class removeKDigits {
    public String getRemoved(String num, int k) {
        Deque<Character> s = new ArrayDeque<>();
        int len = num.length();

        if (k == len) {
            return "0";
        }

        s.add(num.charAt(0));

        for (int i = 1; i < len; i++) {
            char c = num.charAt(i);
            while (k > 0 && !s.isEmpty() && s.peekLast() > c) {
                s.pollLast();
                k--;                
            }
            s.add(c);
        }

        while (k > 0) {
            s.pollLast();
            k--;
        }

        StringBuilder ans = new StringBuilder();
        boolean foundLeadingNonZero = false;
        for (char c : s) {
            if (!foundLeadingNonZero && c == '0') {
                continue;
            }

            foundLeadingNonZero = true;
            ans.append(c);
        }

        if (ans.length() == 0) {
            ans.append('0');
        }

        return ans.toString();
    }

    public static void main(String[] args) {
        String num = "1432219";
        int k = 3;

        removeKDigits test = new removeKDigits();
        System.out.println(test.getRemoved(num, k));
    }
}
