package LeetCode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class findAnagrams {
    public List<Integer> find(String s, String p) {
        List<Integer> ans = new ArrayList<>();

        int ns = s.length();
        int np = p.length();

        if (np > ns) {
            return ans;
        }

        int[] pCount = new int[26];
        Arrays.fill(pCount, 0);

        for (char i : p.toCharArray()) {
            pCount[i - 'a']++;
        }

        int[] sCount = new int[26];
        Arrays.fill(sCount, 0);

        for (int j = 0; j < np; j++) {
            sCount[s.charAt(j) - 'a']++;

            if (Arrays.equals(sCount, pCount)) {
                ans.add(j - np + 1);
            }
        }

        for (int i = 1; i <= ns - np; i++) {
            sCount[s.charAt(i - 1) - 'a']--;
            sCount[s.charAt(i + np - 1) - 'a']++;

            if (Arrays.equals(sCount, pCount)) {
                ans.add(i);
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        String s = "aaa";
        String p = "aaaaa";
        findAnagrams test = new findAnagrams();

        List<Integer> ans = test.find(s, p);

        for (Integer integer : ans) {
            System.out.println(integer);
        }
    }
}
