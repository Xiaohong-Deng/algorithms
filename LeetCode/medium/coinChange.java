package LeetCode.medium;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class coinChange {
    private int ans;

    public static class Pair {
        public int amount;
        public int numCoins;

        public Pair(int a, int n) {
            this.amount = a;
            this.numCoins = n;
        }
    }

    // with bfs we check all possible combination of n coins before moving to n + 1 coin, so we can return the first valid answer
    // if we see an amount appear before it either consists of fewer or same number of coins, implying that we shall not add this amount again
    public int change(int[] coins, int amount) {
        ans = -1;

        Set<Integer> seen = new HashSet<>();
        Queue<Pair> q = new ArrayDeque<>();

        q.add(new Pair(amount, 0));

        seen.add(amount);

        while (!q.isEmpty()) {
            Pair cur = q.poll();

            int curAmount = cur.amount;
            int curNumCoins = cur.numCoins;

            if (curAmount == 0) {
                return curNumCoins;
            }

            for (int i : coins) {
                if (curAmount - i < 0 || seen.contains(curAmount - i)) {
                    continue;
                }

                q.add(new Pair(curAmount - i, curNumCoins + 1));
                seen.add(curAmount - i);
            }
        }


        return ans;
    }


    public static void main(String[] args) {
        int[] coins = {411,412,413,414,415,416,417,418,419,420,421,422};
        int amount = 9864;

        coinChange test = new coinChange();

        System.out.println(test.change(coins, amount));

    }
}
