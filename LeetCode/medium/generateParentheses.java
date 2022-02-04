package LeetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class generateParentheses {
    public List<String> generate(int n) {
        List<String> ans = new ArrayList<>();

        if (n == 0) {
            return ans;
        }

        StringBuilder buf = new StringBuilder();

        int depth = 0;
        int unpairedLeftCnt = 0;
        int leftRemain = n;

        buf.append('(');
        unpairedLeftCnt++;
        leftRemain--;
        dfs(ans, buf, depth + 1, unpairedLeftCnt, leftRemain, n * 2);
        buf.deleteCharAt(depth);
        unpairedLeftCnt--;
        leftRemain++;

        return ans;
    }

    private void dfs(List<String> ans, StringBuilder buf, int depth, int unpairedLeftCnt, int leftRemain, int strLen) {
        if (depth >= strLen) {
            ans.add(buf.toString());
            return;
        }

        if (unpairedLeftCnt > 0) {
            buf.append(')');
            unpairedLeftCnt--;
            dfs(ans, buf, depth + 1, unpairedLeftCnt, leftRemain, strLen);
            buf.deleteCharAt(depth);
            unpairedLeftCnt++;
        }

        if (leftRemain > 0) {
            buf.append('(');
            unpairedLeftCnt++;
            leftRemain--;
            dfs(ans, buf, depth + 1, unpairedLeftCnt, leftRemain, strLen);
            buf.deleteCharAt(depth);
            unpairedLeftCnt--;
            leftRemain++;
        }
    }

    public static void main(String[] args) {
        int n = 3;
        List<String> ans;
        generateParentheses test = new generateParentheses();
        ans = test.generate(n);

        for (String string : ans) {
            System.out.println(string);
        }
    }
}
