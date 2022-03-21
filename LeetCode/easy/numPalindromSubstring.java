package LeetCode.easy;

import java.util.Arrays;

public class numPalindromSubstring {
    private int[][] cache;
    // we have n^2 possible substring in String s
    public int countSubstrings(String s) {
        int l = s.length();
        cache = new int[l][l];

        for (int[] is : cache) {
            Arrays.fill(is, -1);
        }

        for (int i = 0; i < l; i++) {
            cache[i][i] = 1;
        }

        for (int i = 0; i < l; i++) {
            for (int j = i + 1; j < l; j++) {
                char c1, c2;
                c1 = s.charAt(i);
                c2 = s.charAt(j);
                if (c1 == c2) {
                    if (j == i + 1 && cache[i][j] == -1) {
                        cache[i][j] = 1;
                    } else if (cache[i][j] == -1) {
                        cache[i][j] = solve(s, i + 1, j - 1);
                    }
                } else {
                    cache[i][j] = 0;
                }
            }
        }

        int total = 0;

        for (int[] is : cache) {
            for (int i : is) {
                if (i > 0) {
                    total += i;
                }
            }
        }

        return total;
    }

    private int solve(String s, int start, int end) {
        if (start == end) {
            return 1;
        }

        char c1, c2;
        c1 = s.charAt(start);
        c2 = s.charAt(end);

        if (c1 != c2) {
            cache[start][end] = 0;
            return 0;
        }

        if (start == end - 1 && cache[start][end] == -1) {
            cache[start][end] = 1;
        } else if (cache[start][end] == -1) {
            cache[start][end] = solve(s, start + 1, end - 1);
        }
        return cache[start][end];
    }

    public static void main(String[] args) {
        String s1 = "aaa";
        String s2 = "aaaaa";
        numPalindromSubstring t = new numPalindromSubstring();
        System.out.println(t.countSubstrings(s1));
        System.out.println(t.countSubstrings(s2));
    }
}
