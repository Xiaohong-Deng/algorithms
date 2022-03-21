package LeetCode.medium;

public class balanceMinDel {
    // we take a cut in middle and if delete b in first half and a in second half it is valid
    // but in general it removes too many
    // but there is one cut that make this removal minimum
    // the point is the final result is a...ab...b, there is a cut between middle a | b, there is always a cut
    public int countMinDelTwoPass(String s) {
        int l = s.length();

        if (l <= 1) {
            return 0;
        }

        int[] prefix, suffix;
        prefix = new int[l];
        suffix = new int[l];

        prefix[0] = s.charAt(0) == 'b' ? 1 : 0;
        for (int i = 1; i < l; i++) {
            prefix[i] = prefix[i - 1] + (s.charAt(i) == 'b' ? 1 : 0);
        }

        suffix[l - 1] = s.charAt(l - 1) == 'a' ? 1 : 0;

        for (int i = l - 2; i >= 0; i--) {
            suffix[i] = suffix[i + 1] + (s.charAt(i) == 'a' ? 1 : 0);
        }

        int result = Math.min(prefix[l - 1], suffix[0]);

        for (int i = 0; i < l - 1; i++) {
            result = Math.min(result, prefix[i] + suffix[i + 1]);
        }

        if (result > prefix[l - 1]) {
            result = prefix[l - 1];
        }

        return result;
    }

    public static void main(String[] args) {
        String s = "abbbababbbbbbbabababbabbbbaaabaabbaaaabbabbbbaabaabaaabbbbaabbabababaababbbabbbababbbbbbabbabaaabbabbaabbabbaabaabababbbbababbabbbbbbbaaaaabababbbbabbbbbbabbaabbbbbaabbbaabbabaabbabaabbbbabbaaaaaababababbaaaabbbabaabababaabaabaaabbbbaabbaabbbaaaaababbababaabbabaaaaaababbbaabaabababbbbbabababbababaaaaaaababbabbabaaabbbbaaabaaaaaabaabaaabaabbabaaababababbbaaaaaaabbabaabbbaababaaabaabaabbababaababbbbbbbbbbaababbaabaabbbbaabbabbababaaabaabbbaabbbbbbabaababbbabbaabbabbbbaabbbbababbbbbaaabaabaabbbbbbbbbabaabbababaaaabbaabbababaaababbababbaababbbbbbbabbabbbbbbabbbabbbbbbabbabaabbaaaaabaaaaabbabaaaaaabbababbbbbaabaabbbaabbaaaababbbbaaababbbbabbbbbbbaaabaabbababbbabaababbabababbaaabbaaabbbabaabaabbbbaaabbaabbbabaabbaaabbbbbbababaababaabaabbabaabaabbaaabbbaabbbaabbaaababbbbbaabbbbabaabaabbabaaababaaaababbbababbaaaaaabaaabbabaaaabbbabababbababbaabbaabbaaabbbabbbbabababaaaaabbabaaabababaabaaabbbaababaababbbaaaabbabbaabaabbbaabbbbababaabbbaabaaaababbabbaaaaababaaababbabbababbbababbaaababbaabbaaabaabbbaaababbbbbbaabbaababbbbbbaabbaabbabaabbbbbbbababbbbbabbaababbbbbabbbaaababbaabbababbaabbaaaaaaabaaababaaabaabaaabaaaaababbbaaaabbbbbbbaaabbbaaabaaaabaaaaabbabaabbabaaabbaaaababababbabababaaaabababbbbababaabbbababbaabbaabaabaaaabaabbaababaabbaaababaababbababaabaaaaabbbbbaabbabbbbaaabbbbbbabbbbabaaaaabbbabaabbbbaabbabaabbbababaaabbbabbaaaabaaababbbabbaabaababbaaaabbaaaabbaababababbaaaabbbbbbababbbbaaabbbaabbabbbaaabbabbbbbbbbabbbaabbbababbabaabbbababbbababbabaaabababaabbbaabbabbbaabababbabbabaabbabaaababbbbbbaaaaaaaabbbbaaaaabbababbbbabbaabbaabaaababbaababababaabaaaaaaabaaabaababbbbbaabbbaaaabbabbbabbbabbbabbabaabbbaaaaabbabbabaabbabaaababaaabbaaaaababbbaababababbaaabaabbabbabbababaabaabaabaabbbabaabbaababbabbbbbbaaaabbbbbaabbaabaaaabbabbabbbababaabbaaabbaaabbaabbbbbabbabbbbaaaaaabbbaabaaababbbaabbbaabbababbbbabbbbbaaaabaaabaabbbbabbaabbaabbabbbbbbbbbbabaaabaaaaabbbabaaaabbaaabbbbbbaabaabababbaababbabaabbaabbaaaabbbbbbaaabaaaababaabaababbbbaababbbaaaaabbaaaaabbbbaaabaabbbabaaabbbaabbaaaaabbbbbabaababaabaababbaabbaabbabaaabaabbabbabaaabbaaaabbaaababbaabbbabbabbbbaaabaaabbbbbabaabaabbbababbaabaaabbbababbababaabbabbbbbaaaaababaabbaabaaaabbbaaabbaaaabbbbbaabbaababbbabbabbbaaaaaaabaaaabbbabaaaabababbbababaaaaaabbabaabbaaaabababaabbbaabaabbababbbabababaabbabbaabbbababbbabaabbbaaabbaaaaababbabbabaabbbbaaaabbaabbbbabbbaabbaababbaabbabbabbabbabababbbaaabbbabaabbababababaaabaaaabbaabbbbbbbabbbbabbababbbbbaabbbbbaaabbbbababaabbbaaabaaaaaabbbabababbbabaaaaaabbbbaaaaaababbaaabaaabbbbaabaaabaaababbabbbaabbabaaaabaababbabbbaabbbbbbabbaababaabbbbbabaaabbbaabbaabaabaababbabbbabbaabbabbbbabbabaaaaababaababaaaaabababbbbbabababbabbbbababaaaaabbaabbbaabbbabbabbbbbaaabaaababbbabbabbbbbbababaabbbbbbaabaaaaaabaaababbbbbabbabababaaababaaababaaabaaaabbabbbbaaabbabbbbbaaaababbabbaaaabbbabaababbaababbabba";
        balanceMinDel t = new balanceMinDel();
        System.out.println(t.countMinDelTwoPass(s));
    }
}
