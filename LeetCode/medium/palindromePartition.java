package LeetCode.medium;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class palindromePartition {
    public List<List<String>> partition(String s) {
        
        if (s.length() == 1) {
            List<List<String>> ans = new ArrayList<>();
            ans.add(new ArrayList<>(Arrays.asList(s)));
            return ans;
        }
        
        // we pass the reference of the List<List<String>> and List<String> to recursive calls.
        // the trick to reuse List<String> is popping out the last element every time return from the last call, in DFS this works
        List<String> sharedList = new ArrayList<String>();
        List<List<String>> ans = new ArrayList<>();

        int start = 0;
        int end = 0;

        // startIdx is the first char index, cutIdx is the index of the char that has the cut before it
        if (isPalindrome(s, start, end)) {
            sharedList.add(s.substring(start, end + 1));
            partitionRecursive(s, start + 1, end + 2, sharedList, ans);
            sharedList.remove(sharedList.size() - 1);
        }

        partitionRecursive(s, start, end + 2, sharedList, ans);

        return ans;
    }

    private void partitionRecursive(String s, int startIdx, int cutIdx, List<String> stringList, List<List<String>> acc) {
        if (cutIdx == s.length()) {
            if (isPalindrome(s, startIdx, cutIdx - 1)) {
                List<String> member = new ArrayList<>();
                for (String string : stringList) {
                    member.add(string);
                }
                member.add(s.substring(startIdx, cutIdx));
                acc.add(member);
            }
        } else {
            // make a cut
            if (isPalindrome(s, startIdx, cutIdx - 1)) {
                stringList.add(s.substring(startIdx, cutIdx));
                partitionRecursive(s, cutIdx, cutIdx + 1, stringList, acc);
                stringList.remove(stringList.size() - 1);
            }
            // do not make a cut
            partitionRecursive(s, startIdx, cutIdx + 1, stringList, acc);
        }
    }

    private boolean isPalindrome(String s, int start, int end) {
        while (start < end) {
            if (s.charAt(start++) != s.charAt(end--)) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {
        String s = "aab";

        palindromePartition test = new palindromePartition();
        List<List<String>> res = test.partition(s);

        for (List<String> list : res) {
            for (String str : list) {
                System.out.print(str + " ");
            }
            System.out.println();
        }
    }
}
