package LeetCode.medium;

import java.util.HashSet;
import java.util.Set;

public class longestSubstringWithoutRepeatedChar {
    public int lengthOfLongest(String s) {
        if (s.length() == 0 || s.length() == 1) {
            return s.length();
        }

        int longestLen, curLen;
        longestLen = curLen = 1;

        Set<Character> charSet = new HashSet<>();

        int i, j;
        char cj, ci;
        i = 0;
        j = 1;
        charSet.add(s.charAt(i));
        
        while (j < s.length()) {
            cj = s.charAt(j);
            if (!charSet.contains(cj)) {
                charSet.add(cj);
                curLen++;
            } else {
                if (curLen > longestLen) {
                    longestLen = curLen;
                }

                ci = s.charAt(i);
                while (ci != cj) {
                    charSet.remove(ci);
                    i++;
                    ci = s.charAt(i);
                }

                // now ci == cj, remove it by just i++
                i++;
                curLen = j - i + 1;

                // after this non-increasing process we dont check if curLen is better
            }
            j++;
        }

        if (curLen > longestLen)
            longestLen = curLen;

        return longestLen;
    }

    public static void main(String[] args) {
        String s1 = "bbbbb";
        String s2 = "abcabcbb";
        String s3 = "pwwkew";
        String s4 = "au";

        longestSubstringWithoutRepeatedChar test = new longestSubstringWithoutRepeatedChar();
        System.out.println(test.lengthOfLongest(s1));
        System.out.println(test.lengthOfLongest(s2));
        System.out.println(test.lengthOfLongest(s3));
        System.out.println(test.lengthOfLongest(s4));
    }
}
