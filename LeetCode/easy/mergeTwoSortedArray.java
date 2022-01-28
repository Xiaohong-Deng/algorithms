package LeetCode.easy;

public class mergeTwoSortedArray {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) {
            return;
        }

        if (m == 0) {
            for (int i = 0; i < nums2.length; i++) {
                nums1[i] = nums2[i];
            }
            return;
        }

        int i, j;
        i = j = 0;

        int[] aux = new int[nums1.length];
        int k = 0;

        while (i < m && j < n) {
            if (nums1[i] < nums2[j]) {
                aux[k++] = nums1[i++];
            } else {
                aux[k++] = nums2[j++];
            }
        }

        if (i >= m) {
            while (j < n) {
                aux[k++] = nums2[j++];
            }
        }

        if (j >= n) {
            while (i < m) {
                aux[k++] = nums1[i++];
            }
        }

        for (int k2 = 0; k2 < aux.length; k2++) {
            nums1[k2] = aux[k2];
        }
    }

    public void mergeInPlace(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) {
            return;
        }

        if (m == 0) {
            for (int i = 0; i < nums2.length; i++) {
                nums1[i] = nums2[i];
            }
            return;
        }

        int i, j, k;
        i = m - 1;
        j = n - 1;
        k = m + n - 1;

        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
            }
        }

        if (i < 0) {
            while (j >= 0) {
                nums1[k--] = nums2[j--];
            }
        }
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 0, 0, 0};
        int[] nums2 = {2, 5, 6};
        int m = 3;
        int n = 3;
        mergeTwoSortedArray test = new mergeTwoSortedArray();
        test.mergeInPlace(nums1, m, nums2, n);
        for (int i : nums1) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
