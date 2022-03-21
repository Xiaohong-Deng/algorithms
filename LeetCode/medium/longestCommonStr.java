package LeetCode.medium;

import java.util.Arrays;

public class longestCommonStr {
    private int[][] cache;
    private String t1;
    private String t2;
    public int longestCommonSubsequence(String text1, String text2) {
        int len1, len2;
        len1 = text1.length();
        len2 = text2.length();
        cache = new int[len1 + 1][len2+ 1];

        for (int i = 1; i < len1 + 1; i++) {
            for (int j = 1; j < len2 + 1; j++) {
                cache[i][j] = -1;
            }
        }
        
        t1 = text1;
        t2 = text2;

        return solve2(len1, len2);
    }

    private int solve(int len1, int len2) {
        if (cache[len1][len2] != -1) {
            return cache[len1][len2];
        }

        int op1 = solve(len1, len2 - 1);

        int lastOcc = t1.lastIndexOf(t2.charAt(len2 - 1), len1 - 1);
        int op2 = 0;

        if (lastOcc != -1) {
            op2 = 1 + solve(lastOcc, len2 - 1);
        }

        cache[len1][len2] = Math.max(op1, op2);

        return cache[len1][len2];
    }

    private int solve2(int len1, int len2) {
        if (cache[len1][len2] != -1) {
            return cache[len1][len2];
        }

        char c1, c2;
        c1 = t1.charAt(len1 - 1);
        c2 = t2.charAt(len2 - 1);
        int sol;
        if (c1 == c2) {
            sol = 1 + solve2(len1 - 1, len2 - 1);
        } else {
            sol = Math.max(solve2(len1, len2 - 1), solve2(len1 - 1, len2));
        }

        cache[len1][len2] = sol;
        return sol;
    }

    public static void main(String[] args) {
        String t1 = "abcba";
        String t2 = "abcbcba";
        longestCommonStr t = new longestCommonStr();
        System.out.println(t.longestCommonSubsequence(t1, t2));
    }
}
