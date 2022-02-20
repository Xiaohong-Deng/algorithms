package LeetCode.medium;

public class strWithNoA3B3 {
    public String getStr(int a, int b) {
        StringBuilder ans = new StringBuilder();

        buildStr(a, b, "a", "b", ans);
        return ans.toString();
    }

    private void buildStr(int large, int small, String largeC, String smallC, StringBuilder sb) {
        if (large < small) {
            buildStr(small, large, smallC, largeC, sb);
            return;
        }

        if (small == 0) {
            sb.append(largeC.repeat(Math.min(2, large)));
            return;
        }

        int numLarge = Math.min(2, large);
        int numSmall = large - numLarge >= small ? 1 : 0;
        sb.append(largeC.repeat(numLarge) + smallC.repeat(numSmall));
        buildStr(large - numLarge, small - numSmall, largeC, smallC, sb);
    }

    public static void main(String[] args) {
        strWithNoA3B3 t = new strWithNoA3B3();
        System.out.println(t.getStr(1, 2));
        System.out.println(t.getStr(4, 2));
    }
}
