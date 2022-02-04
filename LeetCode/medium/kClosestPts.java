package LeetCode.medium;

import java.util.concurrent.ThreadLocalRandom;

public class kClosestPts {
    public int[][] kClosest(int[][] points, int k) {
        if (k == points.length) {
            return points;
        }
        int[][] ans = new int[k][2];

        double[] dist = new double[points.length];

        for (int i = 0; i < dist.length; i++) {
            dist[i] = Math.pow(points[i][0], 2) + Math.pow(points[i][1], 2);
        }

        rselect(ans, dist, points, k - 1);

        return ans;
    }

    private void rselect(int[][] ans, double[] dist, int[][] points, int maxK) {
        int start, end, pivot, rank;

        start = 0;
        end = points.length - 1;
        pivot = ThreadLocalRandom.current().nextInt(start, end + 1);

        rank = swap(dist, points, start, end, pivot);

        while (rank != maxK) {
            if (rank < maxK) {
                start = rank + 1;
            } else {
                end = rank - 1;
            }

            pivot = ThreadLocalRandom.current().nextInt(start, end + 1);
            rank = swap(dist, points, start, end, pivot);
        }

        for (int i = 0; i <= maxK; i++) {
            ans[i] = points[i];
        }
    }

    private int swap(double[] dist, int[][] points, int start, int end, int pivot) {
        int i, j;
        int[] tempPt;
        double tempDist;

        i = j = start + 1;

        tempDist = dist[pivot];
        dist[pivot] = dist[start];
        dist[start] = tempDist;

        tempPt = points[pivot];
        points[pivot] = points[start];
        points[start] = tempPt;

        while (j <= end) {
            if (dist[j] <= dist[start]) {
                tempDist = dist[j];
                dist[j] = dist[i];
                dist[i] = tempDist;

                tempPt = points[j];
                points[j] = points[i];
                points[i] = tempPt;

                i++;
                j++;
            } else {
                j++;
            }
        }

        tempDist = dist[i - 1];
        dist[i - 1] = dist[start];
        dist[start] = tempDist;

        tempPt = points[i - 1];
        points[i - 1] = points[start];
        points[start] = tempPt;

        return i - 1;
    }

    public static void main(String[] args) {
        int[][] points = {{3,3},{5,-1},{-2,4}};
        int k = 2;
        kClosestPts test = new kClosestPts();

        int[][] ans = test.kClosest(points, k);
        for (int[] is : ans) {
            System.out.println(is[0] + " " + is[1]);
        }
    }
}
