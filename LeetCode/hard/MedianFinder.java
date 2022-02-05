package LeetCode.hard;

import java.util.Collections;
import java.util.PriorityQueue;

// use two heaps one min pq and one max pq
// to keep them balanced when we have one element we keep it in a variable median
// when we have two we insert the small to max pq and the large to min pq
// when we have three we compare to decide who is the middle one to assign to median, keep the small in max pq and the largest in min pq
// when we have the 4th we compare it with median, if smaller it goes to max pq and median goes to min pq
// if larger it goes to min pq, the median goes to max pq
// when we have 5th we compare it with the top of both pqs, the middle one should be assigned to median
// so on and so forth we can maintian the invariant that median is either the median variable or the mean of the two top values on the two heaps
//
// follow up question: if the numbers are integers in the range of [0, 100] how to optimize?
// we can have an array of length 101 as buckets, use incoming integers as index to increment buckets[i]
// when we findMedian we just go over the buckets accumulating until it reaches half of the total size, we also keep track of total number of elements in the buckets.
// the number of buckets is 101 so findMedian costs O(101) better than double heap where cost is O(logn)
public class MedianFinder {
    PriorityQueue<Integer> small;
    PriorityQueue<Integer> large;
    int median;
    int size;

    public MedianFinder() {
        small = new PriorityQueue<>(20, Collections.reverseOrder());
        large = new PriorityQueue<>(20);
        size = 0;
    }
    
    public void addNum(int num) {
        if (size == 0) {
            median = num;
            size++;
            return;
        }

        if (size % 2 == 0) {
            if (num < small.peek()) {
                median = small.poll();
                small.add(num);
            } else if (num > large.peek()) {
                median = large.poll();
                large.add(num);
            } else {
                median = num;
            }
        } else {
            if (num < median) {
                small.add(num);
                large.add(median);
            } else {
                small.add(median);
                large.add(num);
            }
        }
        size++;
    }
    
    public double findMedian() {
        if (size % 2 == 0) {
            return (small.peek() + large.peek()) / 2.0;
        } else {
            return median;
        }
    }

    public static void main(String[] args) {
        MedianFinder test = new MedianFinder();
        test.addNum(1);
        test.addNum(2);
        double median = test.findMedian();

        System.out.println(median);
        test.addNum(3);
        median = test.findMedian();
        System.out.println(median);
    }
}
