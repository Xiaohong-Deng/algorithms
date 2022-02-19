package LeetCode.hard;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class streamDisjointInterval {
    private TreeMap<Integer, Interval> tree;

    public static class Interval {
        public int start;
        public int end;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public streamDisjointInterval() {
        tree = new TreeMap<>();
    }
    
    public void addNum(int val) {
        if (tree.containsKey(val)) {
            return;
        }

        // get closest key lower and higher than val
        Integer l = tree.lowerKey(val);
        Integer h = tree.higherKey(val);

        // invariant is <start, [start, interval]> to ensure no dups, but key can be something in the middle of the range

        // if val is can connect two intervals
        if (l != null && h != null && tree.get(l).end + 1 == val && tree.get(h).start == val + 1) {
            tree.get(l).end = tree.get(h).end;
            tree.remove(h); // no duplicated intervals
        } else if (l != null && tree.get(l).end + 1 >= val) {
            // if val is right after l interval or in it we update interval with max(end, val)
            tree.get(l).end = Math.max(tree.get(l).end, val);
        } else if (h != null && tree.get(h).start - 1 <= val) {
            // if val is right before h interval or in it we update interval with min(start, val)
            tree.get(h).start = Math.min(tree.get(h).start, val);
        } else {
            tree.put(val, new Interval(val, val));
        }
    }
    
    public int[][] getIntervals() {
        List<Interval> ans = new ArrayList<>(tree.values());
        int[][] arr = new int[ans.size()][2];
        for (int i = 0; i < ans.size(); i++) {
            Interval range = ans.get(i);
            arr[i] = new int[2];
            arr[i][0] = range.start;
            arr[i][1] = range.end;
        }

        return arr;
    }
}
