package LeetCode.medium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class letterCombPhone {
    public List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        int numDigits = digits.length();

        if (numDigits== 0) {
            return ans;
        }

        Map<Character, char[]> intToChars = new HashMap<>();
        char[] digitArray = digits.toCharArray();
        buildIntToChars(intToChars, digitArray);

        int depth = 0;

        StringBuilder buf = new StringBuilder();

        char[] firstChars = intToChars.get(digitArray[depth]);

        for (int i = 0; i < firstChars.length; i++) {
            buf.append(firstChars[i]);
            dfs(numDigits, depth + 1, digitArray, ans, buf, intToChars);
            buf.deleteCharAt(depth);
        }

        return ans;
    }

    private void buildIntToChars(Map<Character, char[]> intToChars, char[] digitArray) {
        for (int i = 0; i < digitArray.length; i++) {
            char[] chars;

            int j = digitArray[i] - '2';

            if (j == 5 || j == 7) {
                chars = new char[4];
            } else {
                chars = new char[3];
            }

            if (j < 6) {
                chars[0] = (char) ('a' + j * 3);
                chars[1] = (char) (chars[0] + 1);
                chars[2] = (char) (chars[0] + 2);
            } else {
                chars[0] = (char) ('a' + j * 3 + 1);
                chars[1] = (char) (chars[0] + 1);
                chars[2] = (char) (chars[0] + 2);
            }
            
            if (j == 5 || j == 7) {
                chars[3] = (char) (chars[0] + 3);
            }

            intToChars.put(digitArray[i], chars);
        }
    }

    private void dfs(int numDigits, int depth, char[] digitArray, List<String> ans, StringBuilder buf, Map<Character, char[]> intToChars) {
        if (depth >= numDigits) {
            ans.add(buf.toString());
            return;
        }

        char[] chars = intToChars.get(digitArray[depth]);

        for (int i = 0; i < chars.length; i++) {
            buf.append(chars[i]);
            dfs(numDigits, depth + 1, digitArray, ans, buf, intToChars);
            buf.deleteCharAt(depth);
        }
    }

    public static void main(String[] args) {
        String digits = "7";

        letterCombPhone test = new letterCombPhone();

        List<String> ans = test.letterCombinations(digits);

        for (String string : ans) {
            System.out.println(string);
        }
    }
}
