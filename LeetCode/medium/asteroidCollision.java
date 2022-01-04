package LeetCode.medium;
import java.util.Deque;
import java.util.ArrayDeque;

public class asteroidCollision {
    public int[] getAsteroidCollision(int[] asteroids) {
        Deque<Integer> stack = new ArrayDeque<>();


        // > 0 moves towards right, < 0 move towards left
        // if stack empty || top goes left || top * cur > 0
        for (int i = 0; i < asteroids.length; i++) {
            if (stack.size() == 0 || stack.peek() < 0 || asteroids[i] > 0) {
                stack.push(asteroids[i]);
                continue;
            }

            boolean explode = false;
            // when top goes right and cur goes left collision
            while (stack.size() != 0 && stack.peek() > 0) {
                int top = stack.peek();
                if (Math.abs(top) < Math.abs(asteroids[i])) {
                    stack.pop();
                } else if (Math.abs(top) > Math.abs(asteroids[i])) {
                    explode = true;
                    break;
                } else {
                    explode = true;
                    stack.pop();
                    break;
                }
            }

            if (!explode && (stack.size() == 0 || stack.peek() < 0)) {
                stack.push(asteroids[i]);
            }
        }

        int[] ans = new int[stack.size()];

        for (int i = ans.length - 1; i >= 0; i--) {
            ans[i] = stack.pop();
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] ast1 = {5, 10, -5};
        int[] ast2 = {-2, -1, 1, 2};
        int[] ast3 = {10, 2, -5};
        asteroidCollision test = new asteroidCollision();
        int[] res1 = test.getAsteroidCollision(ast1);
        int[] res2 = test.getAsteroidCollision(ast2);
        int[] res3 = test.getAsteroidCollision(ast3);

        for (int i : res1) {
            System.out.print(i + " ");
        }

        System.out.println();

        for (int i : res2) {
            System.out.print(i + " ");
        }
        
        System.out.println();

        for (int i : res3) {
            System.out.print(i + " ");
        }

        System.out.println();

    }
}
