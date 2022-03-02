package LeetCode.medium;

import java.util.Arrays;

public class twoCityScheduling {
    public int twoCitySchedCost(int[][] costs) {
        int cost = 0;
        Arrays.sort(costs, (a, b) -> (a[0] - a[1]) - (b[0] - b[1]));
        for (int i = 0; i < costs.length / 2; i++) {
            cost += costs[i][0];
        }

        for (int i = costs.length / 2; i < costs.length; i++) {
            cost += costs[i][1];
        }

        return cost;
    }

    public static void main(String[] args) {
        int[][] costs = {{10,20},{30,200},{400, 50}, {30,20}};
        twoCityScheduling t = new twoCityScheduling();
        System.out.println(t.twoCitySchedCost(costs));
    }
}
