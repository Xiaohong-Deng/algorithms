package LeetCode.medium;
import java.util.Arrays;

public class taskSched {
    public int leastInterval(char[] tasks, int n) {
        int[] count = new int[26];
        Arrays.fill(count, 0);
        for (int i = 0; i < tasks.length; i++) {
            count[tasks[i] - 'A']++;
        }
        int f_max = 0;
        for (var c : count) {
            f_max = Math.max(f_max, c);
        }
        int n_max = 0;
        for (var c: count) {
            if (c == f_max)
                n_max++;
        }
        return Math.max((f_max - 1) * (n + 1) + n_max, tasks.length);
    }

    public static void main(String[] args) {
        char[] tasks = {'A','A','A','A','A','A','B','C','D','E','F','G'};
        int n = 2;
        taskSched test = new taskSched();
        System.out.println(test.leastInterval(tasks, n));
    }
}
