package LeetCode.medium;

public class uniqueBSTPerm {
    private int[][] cache;
    public int numTrees(int n) {
        cache = new int[n + 1][n + 1];
        return numTrees(1, n);
    }

    private int numTrees(int start, int end) {

        int totalSum = 0;
        for (int i = start; i < end + 1; i++) {
            int left, right;
            if (start > i - 1) {
                left = 1;
            } else if (cache[start][i - 1] != 0) {
                left = cache[start][i - 1];
            } else {
                left = numTrees(start, i - 1);
                cache[start][i - 1] = left;
            }

            if (i + 1 > end) {
                right = 1;
            } else if (cache[i + 1][end] != 0) {
                right = cache[i + 1][end];
            } else {
                right = numTrees(i + 1, end);
                cache[i + 1][end] = right;
            }

            totalSum += (left * right);
        }

        return totalSum;
    }

    public static void main(String[] args) {
        uniqueBSTPerm t = new uniqueBSTPerm();
        System.out.println(t.numTrees(3));
    }
}
