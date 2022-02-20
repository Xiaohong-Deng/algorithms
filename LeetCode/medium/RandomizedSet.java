package LeetCode.medium;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RandomizedSet {
    private Map<Integer, Integer> keyToIdx;
    private List<Integer> l;

    public RandomizedSet() {
        this.keyToIdx = new HashMap<>();
        this.l = new ArrayList<>();
    }
    
    public boolean insert(int val) {
        if (!keyToIdx.containsKey(val)) {
            keyToIdx.put(val, l.size());
            l.add(val);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean remove(int val) {
        if (keyToIdx.containsKey(val)) {
            int idx = keyToIdx.get(val);
            l.set(idx, l.get(l.size() - 1));
            l.remove(l.size() - 1);
            // special case idx is the last element, it is removed, no update for its new idx
            if (idx < l.size())
                keyToIdx.put(l.get(idx), idx);
            keyToIdx.remove(val);
            return true;
        }
        return false;
    }
    
    public int getRandom() {
        return l.get(ThreadLocalRandom.current().nextInt(l.size()));
    }

    public static void main(String[] args) {
        RandomizedSet rs = new RandomizedSet();
        rs.insert(0);
        rs.insert(1);
        rs.remove(0);
        rs.insert(2);
        rs.remove(1);

        rs.getRandom();
    }
}
