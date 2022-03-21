package LeetCode.easy;

import java.util.HashSet;

public class longestNiceSubstring {
    public String findLongestNiceSubstring(String s) {
        HashSet<Character> cs = new HashSet<>();

        for (int i = 0; i < s.length(); i++) {
            cs.add(s.charAt(i));
        }

        // if a c is not nice, it cut the string in half, nice substring cannot contain it.
        // if entire string has only nice chars it is nice, which is the base case

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (cs.contains(Character.toLowerCase(c)) && cs.contains(Character.toUpperCase(c))) {
                continue;
            }

            String left = findLongestNiceSubstring(s.substring(0, i));
            String right = findLongestNiceSubstring(s.substring(i + 1, s.length()));

            return left.length() > right.length() ? left : right;
        }

        return s;
    }
}
