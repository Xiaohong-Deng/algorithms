package LeetCode.hard;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class employeeFreeTime {
    public static class Interval {
        public int start;
        public int end;
    
        public Interval() {}
    
        public Interval(int _start, int _end) {
            start = _start;
            end = _end;
        }
    };

    // idea is sort all working time intervals of all people
    // merge one by one until we find a gap between current and next
    // which means no merge between current and next
    public List<Interval> freeTime(List<List<Interval>> schedule) {
        PriorityQueue<Interval> sortedIntervals = new PriorityQueue<>((a, b) -> a.start - b.start);
        List<Interval> ans = new ArrayList<>();

        for (List<Interval> is : schedule) {
            sortedIntervals.addAll(is);
        }

        Interval cur = sortedIntervals.poll();
        while (!sortedIntervals.isEmpty()) {
            Interval next = sortedIntervals.poll();

            if (next.start <= cur.end) {
                cur.end = Math.max(cur.end, next.end);
            } else {
                // found a gap;
                ans.add(new Interval(cur.end, next.start));
                cur = next;
            }
        }

        return ans;
    }
}
