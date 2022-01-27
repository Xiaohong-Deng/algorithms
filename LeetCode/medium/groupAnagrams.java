package LeetCode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class groupAnagrams {
    public List<List<String>> group(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            String k = countSort(s);
            if (map.containsKey(k)) {
                map.get(k).add(s);
            } else {
                List<String> l = new ArrayList<>();
                l.add(s);
                map.put(k, l);
            }
        }
        List<List<String>> ans = new ArrayList<>();
        for (String k : map.keySet()) {
            ans.add(map.get(k));
        }

        return ans;
    }

    private String countSort(String s) {
        int[] count = new int[26];
        Arrays.fill(count, 0);
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        StringBuilder sorted = new StringBuilder();
        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count[i]; j++) {
                sorted.append('a' + i);
            }
        }

        return sorted.toString();
    }
}
