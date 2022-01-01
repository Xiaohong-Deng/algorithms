package LeetCode.hard;

/**
 * medianTwoSortedArrays
 */
public class medianTwoSortedArrays {
    public double findMedian(int[] nums1, int[] nums2) {
        double ans;
        int[] arrayS, arrayL;
        if (nums1.length < nums2.length) {
            arrayS = nums1;
            arrayL = nums2;
        } else {
            arrayS = nums2;
            arrayL = nums1;
        }

        int left, right;
        left = 0;
        right = arrayS.length - 1;

        int total = nums1.length + nums2.length;
        int half = total / 2;

        if (arrayS.length == 0 && total == 1) {
            return arrayL[0];
        }

        if (arrayS.length == 0 && total == 0) {
            return Integer.MIN_VALUE;
        }
        
        if (arrayS.length == 0 && total > 1) {
            int j = arrayL.length / 2;
            if (total % 2 == 0) {
                return (arrayL[j - 1] + arrayL[j]) / 2.0;
            } else {
                return arrayL[j];
            }
        }

        int i, j;
        // i is the index of the first element not in the left subarray
        // so is j. Index means how many on my left
        i = (right + left) / 2 + 1;  // note if right + left = 1, i = 1; if right + left = 0, i = 1; i can't be 0
        j = half - i;

        // invariant is the left subarray is of size half
        // variant is the numbers of elements the short and long array contribute to the left subarray
        while (true) {
            // corner cases are determined by i, left and right
            // case 1 search space has one element, it means the algorithm is about to end
            System.out.println(left + " " + right);
            if (right == left) {
                // in this case we only want to know if we want to keep this element or not, so we only have one comparison
                // elements outside of the search space are either in the left subarray or not
                // arrayS[i - 1] in the left subarray
                if (arrayS[i - 1] <= arrayL[j]) {
                    ans = getAns(arrayS, arrayL, i, j);
                } else {
                    // arrayS[i - 1] not in the left subarray
                    ans = getAns(arrayS, arrayL, i - 1, j + 1);
                }
                break;
            }

            // case 2 search space has more than one element, we draw a line between two elements
            if (arrayS[i - 1] <= arrayL[j] && arrayS[i] >= arrayL[j - 1]) {
                ans = getAns(arrayS, arrayL, i, j);
                break;
            } else if (arrayS[i - 1] > arrayL[j]) {
                right = i - 1;
                i = (left + right) / 2 + 1;
                j = half - i;
            } else {
                left = i;
                i = (left + right) / 2 + 1;
                j = half - i;
            }
        }

        return ans;
    }

    private double getAns(int[] arrayS, int[] arrayL, int i, int j) {
        int total = arrayS.length + arrayL.length;
        double ans;
        if (total % 2 == 0) {
            if (i == 0 && j == arrayL.length) {
                ans = (arrayS[i] + arrayL[j - 1]) / 2.0;
            } else if (i == 0 && j < arrayL.length) {
                ans = (arrayL[j - 1] + Math.min(arrayS[i], arrayL[j])) / 2.0;
            } else if (i == arrayS.length && j == 0) {
                ans = (arrayS[i - 1] + arrayL[j]) / 2.0;
            } else if (i == arrayS.length && j > 0) {
                ans = (Math.max(arrayS[i - 1], arrayL[j - 1]) + arrayL[j]) / 2.0;
            } else {
                ans = (Math.max(arrayS[i - 1], arrayL[j - 1]) + Math.min(arrayS[i], arrayL[j])) / 2.0;
            }
        } else {
            if (i == 0 && j == arrayL.length) {
                ans = arrayS[i];
            } else if (i == 0 && j < arrayL.length) {
                ans = Math.min(arrayS[i], arrayL[j]);
            } else if (i == arrayS.length) {
                ans = arrayL[j];
            } else {
                ans = Math.min(arrayS[i], arrayL[j]);
            }
        }

        return ans;
    }
    public static void main(String[] args) {
        int[] a = {3, 4};
        int[] b = {1, 2};
        medianTwoSortedArrays test = new medianTwoSortedArrays();
        double res = test.findMedian(a, b);
        System.out.println(res);
    }
}