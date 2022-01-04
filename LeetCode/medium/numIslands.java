package LeetCode.medium;

public class numIslands {    
    public int getNumIslands(char[][] grid) {
        int nrows, ncols, icount;
        nrows = grid.length;
        ncols = grid[0].length;
        icount = 0;


        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                if (grid[i][j] == '1') {
                    icount++;
                    dfs(i, j, grid);
                }
            }
        }

        return icount;
    }
    
    private void dfs(int row, int col, char[][] grid) {
        int nrows = grid.length;
        int ncols = grid[0].length;

        if (row < 0 || row >= nrows || col < 0 || col >= ncols || grid[row][col] == '0')
            return;

        grid[row][col] = '0';
        dfs(row - 1, col, grid);
        dfs(row + 1, col, grid);
        dfs(row, col - 1, grid);
        dfs(row, col + 1, grid);
    }

    public static void main(String[] args) {
        char[][] grid = {{'1', '1', '0'}, {'1', '1', '0'}, {'0', '0', '1'}};
        numIslands test = new numIslands();
        int inum = test.getNumIslands(grid);
        System.out.println(inum);
    }
}
