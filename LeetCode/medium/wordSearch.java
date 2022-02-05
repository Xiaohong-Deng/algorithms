package LeetCode.medium;

import java.util.Arrays;

public class wordSearch {
    private boolean[][] explored;
    
    public boolean exist(char[][] board, String word) {
        char[] charArray = word.toCharArray();

        int numRows = board.length;
        int numCols = board[0].length;
        int wordLen = word.length();

        explored = new boolean[numRows][numCols];
        for (boolean[] e : explored) {
            Arrays.fill(e, false);
        }

        int depth = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (dfs(board, charArray, depth, i, j, numRows, numCols, wordLen)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfs(char[][] board, char[] charArray, int depth, int row, int col, int numRows, int numCols, int wordLen) {
        if (depth >= wordLen) {
            return true;
        }

        if (row < 0 || row >= numRows || col < 0 || col >= numCols || explored[row][col] || board[row][col] != charArray[depth]) {
            return false;
        }

        explored[row][col] = true;
        if (dfs(board, charArray, depth + 1, row - 1, col, numRows, numCols, wordLen)) {
            return true;
        }

        if (dfs(board, charArray, depth + 1, row + 1, col, numRows, numCols, wordLen)) {
            return true;
        }

        if (dfs(board, charArray, depth + 1, row, col - 1, numRows, numCols, wordLen)) {
            return true;
        }

        if (dfs(board, charArray, depth + 1, row, col + 1, numRows, numCols, wordLen)) {
            return true;
        }

        explored[row][col] = false;

        return false;
    }

    public static void main(String[] args) {
        char[][] board = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };

        String word = "ABCB";

        wordSearch test = new wordSearch();

        System.out.println(test.exist(board, word));

    }
}
