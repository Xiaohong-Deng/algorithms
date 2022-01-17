package LeetCode.medium;
import java.util.ArrayList;
import java.util.List;

public class spiralMatOne {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        int nrows, ncols, ncells;
        boolean needBreak = false;
        int[] bounds = new int[4];
        int[] startIdx = new int[2];

        nrows = matrix.length;
        ncols = matrix[0].length;
        ncells = nrows * ncols;
  
        // up, right, bottom, left bounds. up and bottom bounds are row numbers. left and right bounds are col numbers
        bounds[0] = bounds[3] = 0;
        bounds[1] = ncols - 1;
        bounds[2] = nrows - 1;;

        // row, col
        startIdx[0] = startIdx[1] = 0;

        while (ans.size() < ncells) {
            needBreak = moveRight(matrix, ans, bounds, startIdx);
            if (needBreak)
                break;
            needBreak = moveDown(matrix, ans, bounds, startIdx);
            if (needBreak)
                break;
            needBreak = moveLeft(matrix, ans, bounds, startIdx);
            if (needBreak)
                break;
            needBreak = moveUp(matrix, ans, bounds, startIdx);
            if (needBreak)
                break;
        }

        return ans;
    }

    private boolean moveRight(int[][] mat, List<Integer> spiralList, int[] bounds, int[] startIdx) {
        if (startIdx[1] > bounds[1]) {
            return true;
        }

        int i, j;
        i = startIdx[0];
        j = startIdx[1];

        while (j <= bounds[1]) {
            spiralList.add(mat[i][j]);
            j++;
        }

        bounds[0]++;

        startIdx[0] = i + 1;
        startIdx[1] = j - 1;

        return false;
    }

    private boolean moveDown(int[][] mat, List<Integer> spiralList, int[] bounds, int[] startIdx) {
        if (startIdx[0] > bounds[2]) {
            return true;
        }

        int i, j;
        i = startIdx[0];
        j = startIdx[1];

        while (i <= bounds[2]) {
            spiralList.add(mat[i][j]);
            i++;
        }

        bounds[1]--;

        startIdx[0] = i - 1;
        startIdx[1] = j - 1;

        return false;
    }

    private boolean moveLeft(int[][] mat, List<Integer> spiralList, int[] bounds, int[] startIdx) {
        if (startIdx[1] < bounds[3]) {
            return true;
        }

        int i, j;

        i = startIdx[0];
        j = startIdx[1];

        while (j >= bounds[3]) {
            spiralList.add(mat[i][j]);
            j--;
        }

        bounds[2]--;

        startIdx[0] = i - 1;
        startIdx[1] = j + 1;

        return false;
    }

    private boolean moveUp(int[][] mat, List<Integer> spiralList, int[] bounds, int[] startIdx) {
        System.out.println(bounds[0] + " " + startIdx[0]);
        if (startIdx[0] < bounds[0]) {
            return true;
        }

        int i, j;

        i = startIdx[0];
        j = startIdx[1];

        while (i >= bounds[0]) {
            spiralList.add(mat[i][j]);
            i--;
        }

        bounds[3]++;

        startIdx[0] = i + 1;
        startIdx[1] = j + 1;

        return false;
    }

    public static void main(String[] args) {
        int[][] mat = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
        int[][] mat2 = {{1, 2}, {3, 4}};
        spiralMatOne test = new spiralMatOne();

        List<Integer> ans = test.spiralOrder(mat2);
        for (Integer integer : ans) {
            System.out.println(integer);
        }

        ans = test.spiralOrder(mat);
        for (Integer integer : ans) {
            System.out.println(integer);
        }
    }
    
}
