package LeetCode.medium;

import java.util.List;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class findKClosest {
    // we must return a sorted array without sorting, we do this by finding the left bound and right bound then take subarray
    // k is at least 1
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        // if x does not exist in arr, we need to compare arr[xIdx] and x
        int xIdx = bsearch(x, arr);

        int len = arr.length;

        int left, right;
        if (arr[xIdx] < x) {
            if (xIdx == len - 1 || Math.abs(arr[xIdx] - x) < Math.abs(arr[xIdx + 1] - x)) {
                left = right = xIdx;
            } else {
                left = right = xIdx + 1;
            }
        } else if (arr[xIdx] > x) {
            if (xIdx == 0 || Math.abs(arr[xIdx - 1] - x) > Math.abs(arr[xIdx] - x)) {
                left = right = xIdx;
            } else {
                left = right = xIdx - 1;
            }
        } else {
            left = right = xIdx;
        }

        k--;

        // now left and right are pointing at the same position and that position has the closest element to x
        while (k > 0 && left - 1 >= 0 && right + 1 < len) {
            if (Math.abs(arr[left - 1] - x) > Math.abs(arr[right + 1] - x)) {
                right++;
            } else {
                left--;
            }
            k--;
        }

        if (k > 0 && left - 1 < 0) {
            while (k > 0 && right < len - 1) {
                right++;
                k--;
            }
        }

        if (k > 0 && right + 1 >= len) {
            while (k > 0 && left > 0) {
                left--;
                k--;
            }
        }

        int[] subArr = IntStream.range(left, right + 1).map(i -> arr[i]).toArray();
        return Arrays.stream(subArr).boxed().collect(Collectors.toList());
    }
    
    private int bsearch(int x, int[] arr) {
        int start, mid, end;
        start = 0;
        end = arr.length - 1;
        while (start != end) {
            mid = (end + start) / 2;
            if (arr[mid] == x) {
                return mid;
            } else if (arr[mid] < x) {
                start = mid + 1;
                // if start = 0, end = 1, mid = 0, go right, new start = 1
                if (start > end) {
                    return mid;
                }
            } else {
                end = mid - 1;
                // if start = 0, end = 1, mid = 0, new end = -1
                if (start > end) {
                    return mid;
                }
            }
        }
        
        return start;
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5};
        int k = 4;
        int x = -1;
        findKClosest t = new findKClosest();

        List<Integer> ans = t.findClosestElements(arr, k, x);
        for (Integer i : ans) {
            System.out.println(i);
            assert(Math.abs(i - x) < 3);
        }
    }
}
