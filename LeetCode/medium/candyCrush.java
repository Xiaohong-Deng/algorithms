package LeetCode.medium;

import java.util.Arrays;

public class candyCrush {
    private int rows;
    private int cols;
    public int[][] crush(int[][] board) {
        rows = board.length;
        cols = board[0].length;
        int k = 3;

        boolean isMarked = true;

        while (isMarked) {
            isMarked = markRows(board, rows, cols, k);
            isMarked = markCols(board, rows, cols, k) || isMarked;

            if (isMarked) {
                drop(board, rows, cols);
            }
        }

        return board;
    }

    private boolean markRows(int[][] board, int rows, int cols, int k) {
        boolean isMarked = false;
        int start;
        for (int i = 0; i < rows; i++) {
            start = 0;
            for (int j = 1; j < cols; j++) {
                // for each row we check if previous cell is 0 if yes then combo is invalid, set start to current cell
                int pred = Math.abs(board[i][j - 1]);
                if (pred == 0) {
                    start = j;
                    continue;
                }
                // we also compare abs value because values might be mark in cols as negative values
                int cur = Math.abs(board[i][j]);
                // if current element == previous element, we increment j to expand the window [start, j]
                // if current != previous we see if we can mark the combo elements
                // we mark the combo elements to the negative value
                if (pred != cur) {
                    if (j - start >= k) {
                        for (int l = start; l < j; l++) {
                            board[i][l] = -Math.abs(board[i][l]);
                        }
                        isMarked = true;
                    }
                    start = j;
                }
            }

            // corner case where combo ends at the last element
            if (cols - start >= k) {
                for (int j = start; j < cols; j++) {
                    board[i][j] = -Math.abs(board[i][j]);
                }
                isMarked = true;
            }
        }
        return isMarked;
    }

    private boolean markCols(int[][] board, int rows, int cols, int k) {
        boolean isMarked = false;
        int start;
        for (int i = 0; i < cols; i++) {
            start = 0;
            for (int j = 1; j < rows; j++) {
                int pred = Math.abs(board[j - 1][i]);
                if (pred == 0) {
                    start = j;
                    continue;
                }
                int cur = Math.abs(board[j][i]);
                if (pred != cur) {
                    if (j - start >= k) {
                        for (int l = start; l < j; l++) {
                            board[l][i] = -Math.abs(board[l][i]);
                        }
                        isMarked = true;
                    }
                    start = j;
                }
            }

            if (rows - start >= k) {
                for (int j = start; j < rows; j++) {
                    board[j][i] = -Math.abs(board[j][i]);
                }
                isMarked = true;
            }
        }
        return isMarked;
    }

    // we use the move zero to the end trick to delete crushed elements and drop non-negative elements to the bottom
    // then write the unfilled cells with zeros
    private void drop(int[][] board, int rows, int cols) {
        // loop over cols
        for (int i = 0; i < cols; i++) {
            // for eahc col we write from bottom to top
            int writePtr = rows - 1;
            for (int j = rows - 1; j >= 0; j--) {
                if (board[j][i] >= 0) {
                    board[writePtr--][i] = board[j][i];
                }
            }
            while (writePtr >= 0) {
                board[writePtr--][i] = 0;
            }
        }
    }

    public static void main(String[] args) {
        int[][] board = {{110,5,112,113,114},{210,211,5,213,214},{310,311,3,313,314},{410,411,412,5,414},{5,1,512,3,3},{610,4,1,613,614},{710,1,2,713,714},{810,1,2,1,1},{1,1,2,2,2},{4,1,4,4,1014}};
        candyCrush t = new candyCrush();
        int[][] newBoard = t.crush(board);
        for (int[] is : newBoard) {
            System.out.println(Arrays.toString(is));
        }
    }
}
