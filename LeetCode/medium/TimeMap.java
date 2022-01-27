package LeetCode.medium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeMap {
    private Map<String, List<Data>> kvMap;

    public static class Data{
        String val;
        int ts;
    }

    public TimeMap() {
        kvMap = new HashMap<String, List<Data>>();
    }
    
    public void set(String key, String value, int timestamp) {
        if (!kvMap.containsKey(key)) {
            kvMap.put(key, new ArrayList<>());
        }
        Data d = new Data();
        d.val = value;
        d.ts = timestamp;
        kvMap.get(key).add(d);
    }
    
    public String get(String key, int timestamp) {
        if (!kvMap.containsKey(key)) {
            return "";
        }

        List <Data> valList = kvMap.get(key);

        return binarySearch(timestamp, 0, valList.size() - 1, valList);
    }

    private String binarySearch(int ts, int left, int right, List<Data> l) {
        if (left == right) {
            Data d = l.get(left);
            if (d.ts <= ts) {
                return d.val;
            } else if (left > 0) {
                return l.get(left - 1).val;
            } else {
                return "";
            }
        } else {
            // we want left to point at the leftmost element in the search space and right to point at the rightmost element in the search space.
            // when size is even mid points at the last element in the left half. But if left half has only one element mid is left!
            int mid = (left + right) / 2;
            Data d = l.get(mid);
            if (d.ts == ts) {
                return d.val;
            } else if (d.ts < ts) {
                return binarySearch(ts, mid + 1, right, l);
            } else if (mid > left) {
                return binarySearch(ts, left, mid - 1, l);
            } else {
                return binarySearch(ts, left, mid, l);
            }
        }
    }

    public static void main(String[] args) {
        TimeMap test = new TimeMap();
        test.set("love", "high", 10);
        test.set("love", "low", 20);
        String res1 = test.get("love", 5);
        String res2 = test.get("love", 10);
        String res3 = test.get("love", 15);
        String res4 = test.get("love", 20);
        String res5 = test.get("love", 25);

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
        System.out.println(res4);
        System.out.println(res5);
    }
}
