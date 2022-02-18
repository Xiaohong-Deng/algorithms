package LeetCode.medium;

import java.util.Arrays;

public class minDelUniqueFreq {
    public int minDeletions(String s) {
        int[] freq = new int[26];
        Arrays.fill(freq, 0);

        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i) - 'a']++;
        }

        Arrays.sort(freq);

        int count = 0;

        for (int i = 0; i < freq.length - 1; i++) {
            if (freq[i] == 0) {
                continue;
            }

            int j = i;

            while (j >= 0 && freq[j] == freq[j + 1] && freq[j] != 0) {
                freq[j]--;
                count++;
                j--;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        String s1 = "aab";
        String s2 = "aaabbbcc";
        String s3 = "ceabaacb";

        minDelUniqueFreq t = new minDelUniqueFreq();

        System.out.println(t.minDeletions(s1));
        System.out.println(t.minDeletions(s2));
        System.out.println(t.minDeletions(s3));
    }
}
