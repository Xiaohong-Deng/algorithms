package LeetCode.medium;

import java.util.ArrayDeque;
import java.util.Deque;

// at first i = j until k chars is encountered, we push j back by k chars, and i++, j++. At this point j points at the first char in k chars, we start copying the chars after k chars to overwrite the k char segment
// if baaabcd, it becomes bbaabcd, compare first b and second b. Then it becomes bbcdbcd, it returns bbcd

// the key is count adjacent chars and push the counts to the stack
// we can use a StringBuilder to track the remaining chars, once top count is k, we delete the segment using sb.delete(startIdx, endIdx)
public class removeAdjacentDupStr {
    public String removeDuplicates(String s, int k) {
        int len = s.length();
        if (len < k) {
            return s;
        }
        Deque<Integer> counts = new ArrayDeque<>();
        char[] sa = s.toCharArray();
        int j = 0;
        for (int i = 0; i < s.length(); ++i, ++j) {
            sa[j] = sa[i];
            if (j == 0 || sa[j] != sa[j - 1]) {
                counts.add(1);
            } else {
                int incremented = counts.pollLast() + 1;
                if (incremented == k) {
                    j = j - k;
                } else {
                    counts.add(incremented);
                }
            }
        }
        return new String(sa, 0, j);
    }

    public static void main(String[] args) {
        String s = "deeedbbcccbdaa";
        removeAdjacentDupStr t = new removeAdjacentDupStr();
        System.out.println(t.removeDuplicates(s, 3));
    }
}
