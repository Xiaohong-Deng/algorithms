package LeetCode.hard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class nQueens {
    private int size;
    private List<List<String>> ans;
    public List<List<String>> solveNQueens(int n) {
        size = n;
        ans = new ArrayList<>();
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '.';
            }
        }
        dfs(0, new HashSet<>(), new HashSet<>(), new HashSet<>(), board);
        return this.ans;
    }

    //Like sudoku n queen can have a row blocked by previous placement, in which case we also need to return;
    private void dfs(int row, HashSet<Integer> cols, HashSet<Integer> diag, HashSet<Integer> antiDiag, char[][] board) {
        if (row == this.size) {
            addSol(board);
            return;
        }

        for (int col = 0; col < size; col++) {
            if (cols.contains(col) || diag.contains(row - col) || antiDiag.contains(row + col)) {
                continue;
            }
            cols.add(col);
            diag.add(row - col);
            antiDiag.add(row + col);
            board[row][col] = 'Q';
            dfs(row + 1, cols, diag, antiDiag, board);
            cols.remove(col);
            diag.remove(row - col);
            antiDiag.remove(row + col);
            board[row][col] = '.';
        }
    }

    private void addSol(char[][] board) {
        List<String> sol = new ArrayList<>();
        for (char[] cs : board) {
            sol.add(new String(cs));
        }
        this.ans.add(sol);
    }

    public static void main(String[] args) {
        nQueens t = new nQueens();
        List<List<String>> sols = t.solveNQueens(4);

        for (List<String> list : sols) {
            System.out.println(list.toString());
        }
    }
}
