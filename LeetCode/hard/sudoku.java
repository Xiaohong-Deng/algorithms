package LeetCode.hard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class sudoku {
    private int numRows;
    private int numCols;
    private int numGrids;

    public void solve(char[][] board) {
        numRows = board.length;
        numCols = board[0].length;
        numGrids = (numRows / 3) * (numCols / 3);
        // set up constraints in rows, cols and grids
        List<Set<Character>> rows = new ArrayList<>();
        List<Set<Character>> cols = new ArrayList<>();
        List<Set<Character>> grids = new ArrayList<>();

        initConstraints(board, rows, cols, grids);

        int row, col;
        row = col = 0;

        fillBoardDFS(row, col, board, rows, cols, grids);
    }

    // when recursive call dfs it has two base cases, one is fail to continue, the other is solution found.
    // the caller needs to know if the callee is successful because at each cell we have multiple choices for it
    // which means we have multiple recursive calls. We must know the result of each recursive call to decide if we need
    // to make the next recursive call
    private boolean fillBoardDFS(int row, int col, char[][] board, List<Set<Character>> rows, List<Set<Character>> cols, List<Set<Character>> grids) {
        if (row == numRows - 1 && col >= numCols) {
            return true;
        }

        if (col >= numCols) {
            row++;
            col = 0;
        }

        if (board[row][col] != '.') {
            return fillBoardDFS(row, col + 1, board, rows, cols, grids);
        }

        int gridIdx = (row / 3) * 3 + col / 3;
        

        Set<Character> rowi, coli, gridi;
        rowi = rows.get(row);
        coli = cols.get(col);
        gridi = grids.get(gridIdx);
        // loop over all numbers
        for (int i = 0; i < 9; i++) {
            char c = (char) ('1' + i);

            if (!isValid(c, rowi, coli, gridi)) {
                continue;
            }

            board[row][col] = c;
            rowi.add(c);
            coli.add(c);
            gridi.add(c);

            // note if we return true, sibling dfs calls wont be executed
            // caller dfs will get a true so its siblings wont be called
            if (fillBoardDFS(row, col + 1, board, rows, cols, grids)) {
                return true;
            }

            // if the above fillBoardDFS returns false, we need to restore this cell
            // note we need to restore because sibling dfs calls might need to use this cell and the last c has no successor to replace in this cell
            board[row][col] = '.';
            rowi.remove(c);
            coli.remove(c);
            gridi.remove(c);
        }

        

        // if we tried every char in this cell and it is either invalid or subsequent recursive calls return false, then this call returns false
        return false;
    }

    public void printBoard(char[][] board) {
        for (char[] cs : board) {
            for (char c : cs) {
                System.out.print(String.valueOf(c) + ", ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isValid(char c, Set<Character> row, Set<Character> col, Set<Character> grid) {
        if (row.contains(c) || col.contains(c) || grid.contains(c)) {
            return false;
        }

        return true;
    }

    private void initConstraints(char[][] board, List<Set<Character>> rows, List<Set<Character>> cols, List<Set<Character>> grids) {
        for (int i = 0; i < numRows; i++) {
            rows.add(new HashSet<Character>());
        }

        for (int i = 0; i < numCols; i++) {
            cols.add(new HashSet<Character>());
        }

        for (int i = 0; i < numGrids; i++) {
            grids.add(new HashSet<Character>());
        }
        
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                char c = board[row][col];
                if (c == '.') {
                    continue;
                }
                int gridIdx = (row / 3) * 3 + col / 3;

                rows.get(row).add(c);
                cols.get(col).add(c);
                grids.get(gridIdx).add(c);
            }
        }
    }

    public static void main(String[] args) {
        char[][] board = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        sudoku test = new sudoku();
        test.solve(board);
        test.printBoard(board);
    }
}
