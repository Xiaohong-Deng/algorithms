package LeetCode.easy;

public class palindromeNum {
    public boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }

        if (x == 0) {
            return true;
        }

        int reversed = 0;
        int tempX = x;
        while (tempX != 0) {
            reversed = reversed * 10 + tempX % 10;
            tempX /= 10;
        }

        return reversed == x;
    }

    public static void main(String[] args) {
        int x = 121;
        palindromeNum t = new palindromeNum();
        System.out.println(t.isPalindrome(x));
    }
}
