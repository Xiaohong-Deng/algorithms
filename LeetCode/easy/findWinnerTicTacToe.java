package LeetCode.easy;

import java.util.HashMap;
import java.util.Map;

public class findWinnerTicTacToe {
    public String tictactoe(int[][] moves) {
        // check if A wins
        if (checkA(moves)) {
            return "A";
        }

        if (checkB(moves)) {
            return "B";
        }

        if (moves.length == 9) {
            return "Draw";
        }

        return "Pending";
    }

    private boolean checkA(int[][] moves) {
        int len = moves.length;
        int i = 0;
        Map<Integer, Integer> rows, cols;
        int diag, antiDiag;
        diag = antiDiag = 0;
        rows = new HashMap<>();
        cols = new HashMap<>();
        while (i < len) {
            int row = moves[i][0];
            int col = moves[i][1];
            if (rows.containsKey(row)) {
                rows.put(row, rows.get(row) + 1);
            } else {
                rows.put(row, 1);
            }

            if (cols.containsKey(col)) {
                cols.put(col, cols.get(col) + 1);
            } else {
                cols.put(col, 1);
            }

            if (row + col == 2) {
                antiDiag++;
            }

            if (row - col == 0) {
                diag++;
            }

            i += 2;
        }

        for (int key : rows.keySet()) {
            if (rows.get(key) == 3) {
                return true;
            }
        }

        for (int key : cols.keySet()) {
            if (cols.get(key) == 3) {
                return true;
            }
        }

        if (diag == 3) {
            return true;
        }

        if (antiDiag == 3) {
            return true;
        }

        return false;
    }

    private boolean checkB(int[][] moves) {
        int len = moves.length;
        int i = 1;
        Map<Integer, Integer> rows, cols;
        int diag, antiDiag;
        diag = antiDiag = 0;
        rows = new HashMap<>();
        cols = new HashMap<>();
        while (i < len) {
            int row = moves[i][0];
            int col = moves[i][1];
            if (rows.containsKey(row)) {
                rows.put(row, rows.get(row) + 1);
            } else {
                rows.put(row, 1);
            }

            if (cols.containsKey(col)) {
                cols.put(col, cols.get(col) + 1);
            } else {
                cols.put(col, 1);
            }

            if (row + col == 2) {
                antiDiag++;
            }

            if (row - col == 0) {
                diag++;
            }

            i += 2;
        }

        for (int key : rows.keySet()) {
            if (rows.get(key) == 3) {
                return true;
            }
        }

        for (int key : cols.keySet()) {
            if (cols.get(key) == 3) {
                return true;
            }
        }

        if (diag == 3) {
            return true;
        }

        if (antiDiag == 3) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        int[][] moves = {{0,0}, {2,0}, {1,1}, {2,1}, {2,2}};
        int[][] moves2 = {{0, 0}, {1, 1}, {0, 1}, {0, 2}, {1, 0}, {2, 0}};
        int[][] moves3 = {{0,0},{1,1},{2,0},{1,0},{1,2},{2,1},{0,1},{0,2},{2,2}};
        int[][] moves4 = {{0,0},{1,2}, {0, 2}, {1,1}};
        findWinnerTicTacToe t = new findWinnerTicTacToe();
        System.out.println(t.tictactoe(moves4));
    }
}
