package LeetCode.easy;

public class uniqueSumZero {
    public int[] sumZero(int n) {
        int[] ans = new int[n];

        int start, num;
        if (n % 2 == 1) {
            start = 1;
            ans[0] = 0;
        } else {
            start = 0;
        }

        num = 1;
        for (int i = start; i < n; i += 2) {
            ans[i] = num;
            ans[i + 1] = -num;
            num++;
        }

        return ans;
    }

    public static void main(String[] args) {
        int n = 5;
        uniqueSumZero t = new uniqueSumZero();

        int[] ans = t.sumZero(n);
        for (int i : ans) {
            System.out.println(i);
        }
    }
}
