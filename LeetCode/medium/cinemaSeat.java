package LeetCode.medium;

import java.util.HashMap;
import java.util.Map;

public class cinemaSeat {
    public int maxNumberOfFamilies(int n, int[][] reservedSeats) {
        int numFourGroup = 0;
        // if n is very large and reservedSeats is small, many 0 so Map is small than bit array like short[]
        Map<Integer, Integer> isTaken = new HashMap<>();

        int idx, colBit;
        int mask = 1;
        for (int[] bs : reservedSeats) {
            idx = (bs[0] - 1);
            colBit = bs[1] - 1;

            isTaken.put(idx, isTaken.getOrDefault(idx, 0) | mask << colBit);
        }

        for (int k : isTaken.keySet()) {
            numFourGroup += checkRow(k, isTaken.get(k));
        }

        // we skip empty rows
        return numFourGroup + (n - isTaken.size()) * 2;
    }

    private int checkRow(int rowNum, int row) {
        int count = 0;
        if (row == 0) {
            return 2;
        }
        int mask;
        // next check if the left aisle cuts group in half
        // if so we update isTaken
        if ((row >> 1 << 28) == 0) {
            count++;
            mask = 0x000F << 1;
            row = (row | mask);
        }

        // next check right aisle, because left and right are not overlapped thus can maximize the count
        if ((row >> 5 << 28) == 0) {
            count++;
            mask = 0x000F << 5;
            row = (row | mask);
        }

        // finally we check middle
        if ((row >> 3 << 28) == 0) {
            count++;
            // no update because no look back
        }

        return count;
    }

    public static void main(String[] args) {
        int n = 3;
        int[][] reservedSeats = {{1,2},{1,3},{1,8},{2,6},{3,1},{3,10}};

        cinemaSeat t = new cinemaSeat();

        System.out.println(t.maxNumberOfFamilies(n, reservedSeats));
    }
}
