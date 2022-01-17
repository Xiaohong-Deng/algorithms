package LeetCode.medium;

public class rotateImage {
    public void rotate(int[][] matrix) {
        int nrows, ncols;
        nrows = matrix.length;
        ncols = matrix[0].length;
        int[][] rotated = new int[ncols][nrows];
        for (int row = 0; row < nrows; row++) {
            for (int i = 0; i < ncols; i++) {
                rotated[i][nrows - row - 1] = matrix[row][i];
            }
        }

        for (int i = 0; i < nrows; i++) {
            matrix[i] = rotated[i];
        }
    }

    public static void main(String[] args) {
        int[][] mat = {{1,2,3}, {4,5,6}, {7,8,9}};
        rotateImage test = new rotateImage();
        test.rotate(mat);
        for (int[] is : mat) {
            System.out.println(is[0] + ", " + is[1] + ", " + is[2]);
        }
    }
}
