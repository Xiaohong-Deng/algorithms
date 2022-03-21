package LeetCode.easy;

public class validPalindrome {
    public boolean isPalindrome(String s) {
        int len = s.length();
        for (int i = 0, j = len - 1; i < j; i++, j--) {
            while (i < j && !Character.isLetterOrDigit(i)) {
                i++;
            }

            while (i < j && !Character.isLetterOrDigit(j)) {
                j--;
            }

            if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String s = "A man, a plan, a canal: Panama";
        validPalindrome t = new validPalindrome();

        System.out.println(t.isPalindrome(s));
    }
}
