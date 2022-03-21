package LeetCode.contests.Mar12;

import java.util.HashSet;
import java.util.Set;


public class p2 {
    public int digArtifacts(int n, int[][] artifacts, int[][] dig) {
        int count = 0;

        Set<Integer> digged = new HashSet<>();
        for (int[] i : dig) {
            int r, c;
            r = i[0];
            c = i[1];
            digged.add(r * n + c);
        }

        for (int i = 0; i < artifacts.length; i++) {
            boolean isDigged = true;
            int leftRow, leftCol, rightRow, rightCol, idx;
            leftRow = artifacts[i][0];
            leftCol = artifacts[i][1];
            rightRow = artifacts[i][2];
            rightCol = artifacts[i][3];

            for (int j = leftRow; j < rightRow + 1; j++) {
                for (int j2 = leftCol; j2 < rightCol + 1; j2++) {
                    idx = j * n + j2;
                    if (!digged.contains(idx)) {
                        isDigged = false;
                        break;
                    }
                }
            }

            if (isDigged) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[][] artifacts = {{0,0,0,0},{0,1,1,1}};
        int n = 2;
        int[][] dig = {{0,0}, {0,1}, {1,1}};
        p2 t = new p2();
        System.out.println(t.digArtifacts(n, artifacts, dig));
    }
}
