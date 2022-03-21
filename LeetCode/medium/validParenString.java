package LeetCode.medium;

public class validParenString {
    public boolean checkValidString(String s) {
        int lo = 0, hi = 0;
        // "(" is 1, "(*" can be 0,1,2, [0, 2], numbers are possible count
        for (char c: s.toCharArray()) {
            // if it is * it can reduce lo by 1 and increase hi by 1
            // if it is ( it can increase lo and hi by 1
            // if it is ) it can reduce low and high by 1
            lo += c == '(' ? 1 : -1;
            hi += c != ')' ? 1 : -1;
            if (hi < 0) break;
            lo = Math.max(lo, 0);  // cut off lo because we need to check it to return
        }
        return lo == 0;
    }

    public static void main(String[] args) {
        
    }
}
