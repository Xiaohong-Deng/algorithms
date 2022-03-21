package LeetCode.medium;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class partitionLabels {
    public List<Integer> partitionLabels(String s) {
        int len = s.length();
        int[] last = new int[26];

        List<Integer> ans = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            last[s.charAt(i) - 'a'] = i;
        }

        int start, end;
        start = end = 0;
        for (int i = 0; i < len; i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) {
                ans.add(end - start + 1);
                start = end + 1;
            }
        }

        return ans;
    }
}
