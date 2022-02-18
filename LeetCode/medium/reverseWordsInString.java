package LeetCode.medium;

public class reverseWordsInString {
    public String getReversed(String s) {
        StringBuilder ans = new StringBuilder();
        int len = s.length();
        int start, end;

        start = end = len - 1;
        for (int i = len - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == ' ') {
                if (start != end) {
                    ans.append(s.substring(start + 1, end + 1) + " ");
                    end = start;
                }
                end--;
            } 

            start--;
        }

        if (start != end) {
            ans.append(s.substring(start + 1, end + 1));
        } else {
            ans.deleteCharAt(ans.length() - 1);
        }

        return ans.toString();
    }

    public static void main(String[] args) {
        String s1 = "the sky is blue";
        String s2 = "  hello world  ";
        String s3 = "a good   example";

        reverseWordsInString t = new reverseWordsInString();

        System.out.println(t.getReversed(s1));
        System.out.println(t.getReversed(s2));
        System.out.println(t.getReversed(s3));
    }
}
