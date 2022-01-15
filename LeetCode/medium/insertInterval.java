package LeetCode.medium;

import java.util.List;
import java.util.ArrayList;

public class insertInterval {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        if (intervals.length == 0) {
            int[][] ans = {newInterval};
            return ans;
        }

        List<int[]> ansList = new ArrayList<>();
        int[] merged = intervals[0];
        boolean isMerged, inserted;
        isMerged = inserted = false;

        if (isOverlapped(merged, newInterval)) {
            merge(merged, newInterval);
            isMerged = true;
            if (intervals.length == 1) {
                ansList.add(merged);
            }
        } else if (merged[0] < newInterval[0]) {
            ansList.add(merged);
            if (intervals.length == 1) {
                ansList.add(newInterval);
            }
        } else {
            ansList.add(newInterval);
            ansList.add(merged);
            inserted = true;
        }
        // if newInterval is disjoint we compare it with every interval until some interval is after newInterval, at this point we know merge will never occur
        // if newInterval is overlapped with some interval we compare everu interval until some interval is overlapped with it
        // after it is merged we compare every remaining interval to see if it can be merged until the first disjoint interval appears.
        // at this point we insert the merged interval and every remaining interval.
        for (int i = 1; i < intervals.length; i++) {
            if (inserted) {
                ansList.add(intervals[i]);
            } else if (isMerged) {
                if (isOverlapped(merged, intervals[i])) {
                    merge(merged, intervals[i]);
                    if (i == intervals.length - 1) {
                        ansList.add(merged);
                    }
                } else {
                    ansList.add(merged);
                    ansList.add(intervals[i]);
                    inserted = true;
                }
            } else {
                merged = intervals[i];
                if (isOverlapped(merged, newInterval)) {
                    merge(merged, newInterval);
                    isMerged = true;
                    if (i == intervals.length - 1) {
                        ansList.add(merged);
                    }
                } else if (merged[0] < newInterval[0]) {
                    ansList.add(merged);
                    if (i == intervals.length - 1) {
                        ansList.add(newInterval);
                    }
                } else {
                    ansList.add(newInterval);
                    ansList.add(merged);
                    inserted = true;
                }
            }
        }

        int[][] ans = new int[ansList.size()][2];

        for (int i = 0; i < ansList.size(); i++) {
            ans[i] = ansList.get(i);
        }


        return ans;
    }

    private boolean isOverlapped(int[] interval1, int[] interval2) {
        int[] first, last;
        if (interval1[0] >= interval2[0]) {
            first = interval2;
            last = interval1;
        } else {
            first = interval1;
            last = interval2;
        }

        if (first[1] >= last[0]) {
            return true;
        } else {
            return false;
        }
    }

    private void merge(int[] merged, int[] interval) {
        merged[0] = Math.min(merged[0], interval[0]);
        merged[1] = Math.max(merged[1], interval[1]);
    }

    public static void main(String[] args) {
        int[][] intervals = {{1,2}, {3,5}, {6,7}, {8,10}, {12, 16}};
        int[] newInterval = {4, 8};

        insertInterval test = new insertInterval();
        int[][] ans = test.insert(intervals, newInterval);
        for (int[] is : ans) {
            System.out.println(is[0] + ", " + is[1]);
        }

        int[][] intervals2 = {{1, 5}};
        int[] newInteravl2 = {0, 0};

        int[][] ans2 = test.insert(intervals2, newInteravl2);
        for (int[] is : ans2) {
            System.out.println(is[0] + ", " + is[1]);
        }

    }
}
