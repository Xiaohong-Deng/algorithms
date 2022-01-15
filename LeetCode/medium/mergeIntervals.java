package LeetCode.medium;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class mergeIntervals {
    public int[][] merge(int[][] intervals) {
        List<int[]> ansList = new ArrayList<>();
        Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));
        
        int[] merged = intervals[0];
        for (int j = 1; j < intervals.length; j++) {
            if (merged[1] >= intervals[j][0]) {
                merged[1] = Math.max(merged[1], intervals[j][1]);
            } else {
                ansList.add(merged);
                merged = intervals[j];
            }
        }
        
        ansList.add(merged);

        int[][] ans = new int[ansList.size()][2];
        for (int k = 0; k < ansList.size(); k++) {
            ans[k] = ansList.get(k);
        }

        return ans;
    }

    public static void main(String[] args) {
        int[][] intervals = {{1,3}, {2,6}, {8,10}, {15,18}};
        mergeIntervals test = new mergeIntervals();
        int[][] ans = test.merge(intervals);

        System.out.print('[');
        for (int[] is : ans) {
            System.out.print("[" + is[0] + ", " + is[1] + "], ");
        }
        System.out.println(']');
    }
}
