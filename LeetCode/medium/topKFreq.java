package LeetCode.medium;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;

public class topKFreq {
    // this method will cause O(nlogn) is nums are distinct and k = n
    public int[] topK(int[] nums, int k) {
        int[] ans = new int[k];
        Map<Integer, Integer> count = new HashMap<>();

        countOccurance(nums, count);

        if (count.size() == nums.length) {
            return nums;
        }

        // we can do rselect for every k, that would be O(kn)
        // kth most frequent is the (n - k + 1)th least frequent, minus one to get index
        int[] keys = new int[count.size()];
        int[] counts = new int[count.size()];

        int idx = 0;
        for (Map.Entry<Integer, Integer> pair : count.entrySet()) {
            keys[idx] = pair.getKey();
            counts[idx] = pair.getValue();
            idx++;
        }

        if (keys.length == k) {
            return keys;
        }

        selectMinK(ans, keys, counts, keys.length - k);

        return ans;
    }

    private void selectMinK(int[] ans, int[] keys, int[] counts, int minK) {
        int start, end, pivot, rank;

        start = 0;
        end = keys.length - 1;;
        pivot = ThreadLocalRandom.current().nextInt(0, end + 1);

        rank = swap(keys, counts, start, end, pivot);            

        while (rank != minK) {
            if (rank < minK) {
                start = rank + 1;
            } else {
                end = rank - 1;
            }

            pivot = ThreadLocalRandom.current().nextInt(start, end + 1);
            rank = swap(keys, counts, start, end, pivot);
        }

        for (int i = rank; i < keys.length; i++) {
            ans[keys.length - i - 1] = keys[i];
        }
    }

    private void countOccurance(int[] nums, Map<Integer, Integer> count) {
        for (int i = 0; i < nums.length; i++) {
            if (count.containsKey(nums[i])) {
                count.put(nums[i], count.get(nums[i]) + 1);
            } else {
                count.put(nums[i], 1);
            }
        }
    }

    private int swap(int[] keys, int[] counts, int start, int end, int pivot) {
        int i, j, temp;

        i = j = start + 1;

        temp = counts[pivot];
        counts[pivot] = counts[start];
        counts[start] = temp;

        temp = keys[pivot];
        keys[pivot] = keys[start];
        keys[start] = temp;

        while (j <= end) {
            // equal counts must be on the right of pivot
            // but it does not matter, think if you have too many euqal counts you have to do extra search on the right of your pivot
            // if you have too many equal counts you put them on the left you have to do extra search on the left
            if (counts[j] < counts[start]) {
                temp = counts[i];
                counts[i] = counts[j];
                counts[j] = temp;

                temp = keys[i];
                keys[i] = keys[j];
                keys[j] = temp;

                i++;
                j++;
            } else {
                j++;
            }
        }

        temp = counts[i - 1];
        counts[i - 1] = counts[start];
        counts[start] = temp;

        temp = keys[i - 1];
        keys[i - 1] = keys[start];
        keys[start] = temp;

        return i - 1;
    }

    public static void main(String[] args) {
        int[] nums = {3,2,3,1,2,4,5,5,6,7,7,8,2,3,1,1,1,10,11,5,6,2,4,7,8,5,6};
        int k = 10;
        topKFreq test = new topKFreq();
        int[] ans = test.topK(nums, k);
        for (int i : ans) {
            System.out.println(i);
        }
    }
}
