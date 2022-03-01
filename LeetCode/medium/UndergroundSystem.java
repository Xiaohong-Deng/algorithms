package LeetCode.medium;

import java.util.HashMap;
import java.util.Map;

public class UndergroundSystem {
    private Map<Integer, Pair> checkInMap;
    private Map<String, Pair> travelTime;

    public static class Pair {
        private String station;
        private int time;

        private int totalTime;
        private int sampleSize;

        public Pair(String station, int time) {
            this.station = station;
            this.time = time;
        }

        public Pair(int totalTime, int sampleSize) {
            this.totalTime = totalTime;
            this.sampleSize = sampleSize;
        }
    }

    public UndergroundSystem() {
        checkInMap = new HashMap<>();
        travelTime = new HashMap<>();
    }
    
    public void checkIn(int id, String stationName, int t) {
        if (!checkInMap.containsKey(id)) {
            checkInMap.put(id, new Pair(stationName, t));
        }
    }
    
    public void checkOut(int id, String stationName, int t) {
        if (checkInMap.containsKey(id)) {
            Pair p = checkInMap.get(id);
            int time = t - p.time;
            String sts = p.station + " " + stationName;
            if (travelTime.containsKey(sts)) {
                Pair p2 = travelTime.get(sts);
                travelTime.put(sts, new Pair(p2.totalTime + time, p2.sampleSize + 1));
            } else {
                travelTime.put(sts, new Pair(time, 1));
            }
            checkInMap.remove(id);
        }
    }
    
    public double getAverageTime(String startStation, String endStation) {
        String key = startStation + " " + endStation;
        Pair p = travelTime.get(key);
        return (double) p.totalTime / (double) p.sampleSize;
    }

    public static void main(String[] args) {
        UndergroundSystem t = new UndergroundSystem();
        t.checkIn(45, "a", 3);
        t.checkIn(32, "aa", 8);
        t.checkIn(27, "a", 10);
    }
}
