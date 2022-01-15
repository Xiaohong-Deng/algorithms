package LeetCode.easy;

public class townJudge {
    public int findTownJudge(int n, int[][] trust) {
        int[] countTrusted = new int[n + 1];
        byte[] countTrust = new byte[n + 1];

        // Arrays.fill(countTrusted, 0);
        // Arrays.fill(countTrust, 0);

        for (var relationship : trust) {
            countTrusted[relationship[1]]++;
            countTrust[relationship[0]] = (byte) 0x01;
        }

        for (int i = 1; i < n + 1; i++) {
            if (countTrusted[i] == n - 1 && countTrust[i] == 0x01)
                return i;
        }

        return -1;
    }
}
