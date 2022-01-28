package LeetCode.medium;

public class longestPalindromicSubstring {
    // high 1 indicates if checked, low 1 indicates if palindrome
    private byte[] isPalindromeCache;
    private int len;

    public String findLongestDP(String s) {
        this.len = s.length();
        this.isPalindromeCache = new byte[len * len];
        for (int i = 0; i < isPalindromeCache.length; i++) {
            isPalindromeCache[i] = 0;
        }

        String longestPalindrome = "";
        for (int i = 0; i < len; i++) {
            for (int j = i; j < len; j++) {
                if (isPalindrome(s, i, j) && j - i + 1 > longestPalindrome.length()) {
                    longestPalindrome = s.substring(i, j + 1);
                }
            }
        }

        return longestPalindrome;
    }

    public String findLongestCenter(String s) {
        String longestPalindrome = "";
        int numCenters = s.length() * 2 - 1;

        String longest;
        for (int i = 0; i < numCenters; i++) {
            if (i % 2 == 0) {
                longest = longestCenterExtend(s, i / 2, i / 2);
            } else {
                longest = longestCenterExtend(s, (i - 1) / 2, (i + 1) / 2);
            }

            if (longest.length() > longestPalindrome.length()) {
                longestPalindrome = longest;
            }
        }

        return longestPalindrome;
    }

    private boolean isPalindrome(String s, int i, int j) {
        if (i == j) {
            return true;
        }

        if (i + 1 == j) {
            return s.charAt(i) == s.charAt(j);
        }

        if (isPalindromeCache[i * len + j] == 0x11) {
            return true;
        } else if (isPalindromeCache[i * len + j] == 0x10) {
            return false;
        }

        if (s.charAt(i) == s.charAt(j)) {
            boolean res = isPalindrome(s, i + 1, j - 1);

            if (res)
                isPalindromeCache[i * len + j] = 0x11;
            else
                isPalindromeCache[i * len + j] = 0x10;
            return res;
        } else {
            return false;
        }
    }

    private String longestCenterExtend(String s, int i, int j) {
        if (s.charAt(i) != s.charAt(j)) {
            return "";
        }

        int head = i--;
        int tail = j++;

        while (i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)) {
            head = i--;
            tail = j++;
        }

        return s.substring(head, tail + 1);
    }

    public static void main(String[] args) {
        String s1 = "babad";
        String s2 = "cbbd";

        longestPalindromicSubstring test = new longestPalindromicSubstring();
        System.out.println(test.findLongestDP(s1));
        System.out.println(test.findLongestDP(s2));

        System.out.println(test.findLongestCenter(s1));
        System.out.println(test.findLongestCenter(s2));
    }
}
