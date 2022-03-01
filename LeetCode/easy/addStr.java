package LeetCode.easy;

public class addStr {
    public String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int carry = 0;
        for (int i = num1.length() - 1, j = num2.length() - 1; i >= 0 || j >= 0; i--, j--) {
            int res = carry;
            res += i < 0 ? 0 : num1.charAt(i) - '0';
            res += j < 0 ? 0 : num2.charAt(j) - '0';
            sb.insert(0, res % 10);
            carry = res / 10;
        }

        if (carry == 1)
            sb.insert(0, 1);

        return sb.toString();
    }
}
