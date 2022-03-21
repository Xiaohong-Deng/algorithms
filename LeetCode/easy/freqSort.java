package LeetCode.easy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class freqSort {
    public int[] frequencySort(int[] nums) {
        Map<Integer, Integer> freqs = new HashMap<>();
        for (int i : nums) {
            if (freqs.containsKey(i)) {
                freqs.put(i, freqs.get(i) + 1);
            } else {
                freqs.put(i, 1);
            }
        }

        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(freqs.entrySet());
        Collections.sort(entries, (a, b) -> a.getValue() == b.getValue() ? b.getKey() - a.getKey() : a.getValue() - b.getValue());

        int ptr = 0;
        for (Map.Entry<Integer,Integer> entry : entries) {
            int key = entry.getKey();
            int value = entry.getValue();
            for (int i = 0; i < value; i++) {
                nums[ptr++] = key;
            }
        }

        return nums;
    }    
}
