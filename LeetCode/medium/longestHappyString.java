package LeetCode.medium;

public class longestHappyString {
    public String longestDiverseString(int a, int b, int c) {
        StringBuilder sol = new StringBuilder();
        append(sol, a, b, c, "a", "b", "c");
        return sol.toString();
    }

    private void append(StringBuilder sol, int large, int median, int small, String largeS, String medianS, String smallS) {
        // step 1 make sure order is correct
        if (large < median) {
            append(sol, median, large, small, medianS, largeS, smallS);
            return;
        }

        if (median < small) {
            append(sol, large, small, median, largeS, smallS, medianS);
            return;
        }

        if (median == 0) {
            sol.append(largeS.repeat(Math.min(2, large)));
            return;
        }

        int numL = Math.min(2, large);
        int numM = (large - numL >= median) ? 1 : 0;
        // if after this append medianS becomes largeS we might append 3 medianS which is not allowed
        sol.append(largeS.repeat(numL) + medianS.repeat(numM));
        append(sol, large - numL, median - numM, small, largeS, medianS, smallS);
    }

    public static void main(String[] args) {
        int a = 1, b = 1, c = 7;
        longestHappyString test = new longestHappyString();
        System.out.println(test.longestDiverseString(a, b, c));
    }
}
