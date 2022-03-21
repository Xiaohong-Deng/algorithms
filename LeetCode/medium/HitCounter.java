package LeetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class HitCounter {
    private List<Integer> l;
    public HitCounter() {
        l = new ArrayList<>();
    }
    
    public void hit(int timestamp) {
        l.add(timestamp);
    }
    
    public int getHits(int timestamp) {
        if (l.size() == 0) {
            return 0;
        }
        int startTime = timestamp - 300 + 1;
        int startIdx = bsearch(startTime, 0, l.size() - 1);
        int endIdx = bsearch(timestamp, 0, l.size() - 1);
        int time = l.get(endIdx);

        // if time != target time bsearch can move pointer to right position
        // otherwise we want to be inclusive so move start to left and end to right
        if (time == timestamp)
            endIdx = l.lastIndexOf(time);

        int num;

        if (time <= timestamp) {
            num = endIdx + 1;
        } else {
            num = endIdx;
        }

        time = l.get(startIdx);
        if (time == startTime)
            startIdx = l.indexOf(time);

        if (time >= startTime) {
            num -= startIdx;
        } else {
            num -= startIdx + 1;
        }

        return num;
        
    }

    // need to find duplicate elements if timestamp == dups
    private int bsearch(int timestamp, int start, int end) {
        if (start >= end) {
            return start;
        }

        int mid = (start + end) / 2;
        int time = l.get(mid);

        if (time == timestamp) {
            return mid;
        } else if (time > timestamp) {
            end = mid - 1;
        } else {
            start = mid + 1;
        }

        return bsearch(timestamp, start, end);
    }

    public static void main(String[] args) {
        HitCounter ht = new HitCounter();
        ht.hit(1);
        ht.hit(1);
        ht.hit(1);
        ht.hit(300);
        int getOne = ht.getHits(300);
        int getTwo = ht.getHits(301);
        System.out.println(getOne + " " + getTwo);
    }
}
