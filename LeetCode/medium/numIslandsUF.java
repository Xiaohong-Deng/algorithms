package LeetCode.medium;

import java.util.ArrayList;
import java.util.Arrays;

public class numIslandsUF {
    private static class UF {
        private int[] id;
        private int[] rank;
        // count num of 1's
        private int count;

        public UF(char[][] grid) {
            int nrows = grid.length;
            int ncols = grid[0].length;
            count = nrows * ncols;
            id = new int[count];
            rank = new int[count];
            Arrays.fill(rank, 0);
            for (int i = 0; i < nrows; i++) {
                for (int j = 0; j < ncols; j++) {
                    if (grid[i][j] == '1') {
                        id[i * ncols + j] = i * ncols + j;
                    } else {
                        count--;
                    }
                }
            }
        }

        public int find(int i) {
            ArrayList<Integer> path = new ArrayList<Integer>();
            while (id[i] != i) {
                path.add(i);
                i = id[i];
            }

            for (var n : path) {
                id[n] = i;
            }

            return id[i];
        }

        public void union(int i, int j) {
            int r1 = find(i);
            int r2 = find(j);
            if (r1 != r2) {
                if (rank[r1] > rank[r2]) {
                    id[r2] = r1;
                } else if (rank[r1] < rank[r2]) {
                    id[r1] = r2;
                } else {
                    id[r1] = r2;
                    rank[r2]++;
                }
                count--;
            }
        }

        public int getCount() {
            return count;
        }
    }

    public int getNumIslands(char[][] grid) {
        UF forest = new UF(grid);
        int nrows, ncols;
        nrows = grid.length;
        ncols = grid[0].length;


        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                if (grid[i][j] == '1') {
                    int cur = i * nrows + j;
                    if (i > 0 && grid[i - 1][j] == '1') {
                        forest.union(cur, (i - 1) * ncols + j);
                    }
                    if (i < nrows - 1 && grid[i + 1][j] == '1') {
                        forest.union(cur, (i + 1) * ncols + j);
                    }
                    if (j > 0 && grid[i][j - 1] == '1') {
                        forest.union(cur, i * ncols + j - 1);
                    }
                    if (j < ncols - 1 && grid[i][j + 1] == '1') {
                        forest.union(cur, i * ncols + j + 1);
                    }

                }
            }
        }

        return forest.getCount();
    }

    public static void main(String[] args) {
        char[][] grid = {{'1', '1', '0'}, {'1', '1', '0'}, {'0', '0', '1'}};
        numIslandsUF test = new numIslandsUF();
        System.out.println(test.getNumIslands(grid));
    }
}
