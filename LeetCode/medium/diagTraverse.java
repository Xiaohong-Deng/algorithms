package LeetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class diagTraverse {
    public int[] findDiagonalOrder(int[][] mat) {
        List<Integer> ansList = new ArrayList<>();
        boolean needBreak = false;
        int[] startIdx = new int[2];

        startIdx[0] = startIdx[1] = 0;

        while (true) {
            needBreak = moveUpRight(mat, startIdx, ansList);
            if (needBreak)
                break;
            needBreak = moveDownLeft(mat, startIdx, ansList);
            if (needBreak)
                break;
        }

        int[] ans = new int[mat.length * mat[0].length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = ansList.get(i);
        }
        return ans;
    }

    private boolean moveUpRight(int[][] mat, int[] startIdx, List<Integer> diagList) {
        if (invalidIndex(startIdx[0], startIdx[1], mat.length, mat[0].length)) {
            return true;
        }

        int i, j;
        i = startIdx[0];
        j = startIdx[1];

        while (!invalidIndex(i, j, mat.length, mat[0].length)) {
            diagList.add(mat[i][j]);
            i--;
            j++;
        }

        if (!invalidIndex(i + 1, j, mat.length, mat[0].length)) {
            startIdx[0] = i + 1;
            startIdx[1] = j;
        } else {
            startIdx[0] = i + 2;
            startIdx[1] = j - 1;
        }

        return false;
    }

    private boolean moveDownLeft(int[][] mat, int[] startIdx, List<Integer> diagList) {
        if (invalidIndex(startIdx[0], startIdx[1], mat.length, mat[0].length)) {
            return true;
        }

        int i, j;
        i = startIdx[0];
        j = startIdx[1];

        while (!invalidIndex(i, j, mat.length, mat[0].length)) {
            diagList.add(mat[i][j]);
            i++;
            j--;
        }

        if (!invalidIndex(i, j + 1, mat.length, mat[0].length)) {
            startIdx[0] = i;
            startIdx[1] = j + 1;
        } else {
            startIdx[0] = i - 1;
            startIdx[1] = j + 2;
        }

        return false;
    }

    private boolean invalidIndex(int row, int col, int nrows, int ncols) {
        if (row < 0 || row >= nrows || col < 0 || col >= ncols) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        int[][] mat = {{1,2,3},{4,5,6},{7,8,9}};
        diagTraverse test = new diagTraverse();

        int[] ans = test.findDiagonalOrder(mat);
        for (int i : ans) {
            System.out.println(i);
        }

        int[][] mat2 = {{1,2,3,4},{5,6,7,8}};
        ans = test.findDiagonalOrder(mat2);
        for (int is : ans) {
            System.out.println(is);
        }
    }
}
