package LeetCode.medium;

import java.util.ArrayDeque;
import java.util.Queue;

public class maxAreaIsland {
    private int maxArea;
    private int nrows;
    private int ncols;
    public int maxAreaOfIsland(int[][] grid) {
        maxArea = 0;
        nrows = grid.length;
        ncols = grid[0].length;
        // BFS, loop over all elements, each time we find a cell that is not explored, it is the first time we see the island it belongs to
        // so we explore an island one time, no more
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                if (grid[i][j] != 0) {
                    int area = bfs(grid, i, j);
                    if (area > maxArea) {
                        maxArea = area;
                    }
                }
            }
        }

        return maxArea;
    }

    private int bfs(int[][] grid, int i, int j) {
        Queue<Integer[]> q = new ArrayDeque<>();
        
        Integer[] root = new Integer[2];
        root[0] = i;
        root[1] = j;
        grid[i][j] = 0;
        q.add(root);

        int area = 0;

        while (!q.isEmpty()) {
            Integer[] cur = q.poll();

            int row, col;
            row = cur[0];
            col = cur[1];
            area++;

            if (row - 1 >= 0 && grid[row - 1][col] != 0) {
                Integer[] node = new Integer[2];
                node[0] = row - 1;
                node[1] = col;
                grid[row - 1][col] = 0;
                q.add(node);
            }

            if (col + 1 < ncols && grid[row][col + 1] != 0) {
                Integer[] node = new Integer[2];
                node[0] = row;
                node[1] = col + 1;
                grid[row][col + 1] = 0;
                q.add(node);
            }

            if (row + 1 < nrows && grid[row + 1][col] != 0) {
                Integer[] node = new Integer[2];
                node[0] = row + 1;
                node[1] = col;
                grid[row + 1][col] = 0;
                q.add(node);
            }

            if (col - 1 >= 0 && grid[row][col - 1] != 0) {
                Integer[] node = new Integer[2];
                node[0] = row;
                node[1] = col - 1;
                grid[row][col - 1] = 0;
                q.add(node);
            }
        }

        return area;
    }

    public static void main(String[] args) {
        int[][] grid = {{0,0,1,0,0,0,0,1,0,0,0,0,0}, {0,0,0,0,0,0,0,1,1,1,0,0,0},{0,1,1,0,1,0,0,0,0,0,0,0,0},{0,1,0,0,1,1,0,0,1,0,1,0,0},{0,1,0,0,1,1,0,0,1,1,1,0,0},{0,0,0,0,0,0,0,0,0,0,1,0,0},{0,0,0,0,0,0,0,1,1,1,0,0,0},{0,0,0,0,0,0,0,1,1,0,0,0,0}};
        maxAreaIsland t = new maxAreaIsland();
        int res = t.maxAreaOfIsland(grid);
        System.out.println(res);
    }
}
