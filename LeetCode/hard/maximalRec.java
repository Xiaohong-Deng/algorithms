import java.util.Arrays;

public class maximalRec {
  public int maximalRectangle(char[][] matrix) {
    int numofRows = matrix.length;
    int numofCols = matrix[0].length;
    int[] leftBound = new int[numofCols];
    int[] rightBound = new int[numofCols];
    int[] height = new int[numofCols];
    Arrays.fill(rightBound, numofCols);
    int maxArea = 0;

    // height and leftBound loops can be combined.
    // for clarity I separated them
    for (int i = 0; i < numofRows; i++) {
      int current_left = 0, current_right = numofCols;
      // first we update the left boundary
      for (int j = 0; j < numofCols; j++) {
        if (matrix[i][j] == '1') {
          leftBound[j] = Math.max(leftBound[j], current_left);
        } else {
          leftBound[j] = 0;
          current_left = j + 1;
        }
      }

      // next we update the right boundary, from right to left
      for (int j = numofCols-1; j >= 0; j--) {
        if (matrix[i][j] == '1') {
          rightBound[j] = Math.min(rightBound[j], current_right);
        } else {
          rightBound[j] = numofCols;
          current_right = j;
        }
      }

      // at last we update the height
      for (int j = 0; j < numofCols; j++) {
        if (matrix[i][j] == '1')
          height[j]++;
        else
          height[j] = 0;
      }

      for (int j = 0; j < numofCols; j++)
        maxArea = Math.max(maxArea, (rightBound[j] - leftBound[j]) * height[j]);
    }

    return maxArea;
  }

  public static void main(String[] args) {
    char[][] matrixA = {{'0', '1', '1', '0', '1', '1', '0'}, {'0', '0', '1', '1', '1', '1', '0'},
        {'1', '1', '1', '1', '0', '1', '0'}};
    char[][] matrixB = {{'0', '0', '1', '0', '0'}, {'0', '1', '1', '1', '0'}, {'1', '1', '1', '1', '1'}};
    maximalRec testInstance = new maximalRec();
    System.out.println(testInstance.maximalRectangle(matrixA));
    System.out.println(testInstance.maximalRectangle(matrixB));
  }
}
