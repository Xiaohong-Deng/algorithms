package LeetCode.medium;

public class countSortedVowelStr {
    public int count(int n) {
        int numVowels = 5;
        int[][] nums = new int[n + 1][numVowels + 1];

        for (int i = 1; i < numVowels + 1; i++) {
            nums[1][i] = i;
        }

        for (int i = 2; i < n + 1; i++) {
            nums[i][1] = 1;
            for (int j = 2; j < numVowels + 1; j++) {
                nums[i][j] = nums[i - 1][j] + nums[i][j - 1];
            }
        }

        return nums[n][5];
    }

    public static void main(String[] args) {
        countSortedVowelStr t = new countSortedVowelStr();
        System.out.println(t.count(2));
    }
}
