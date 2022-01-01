package LeetCode.medium;
import java.util.HashMap;

class RecurDecimal {
    public String fractionToDecimal(int numerator, int denominator) {
        StringBuffer res = new StringBuffer();
        HashMap<Long, Integer> num_to_index = new HashMap<>();
        long quotient;
        
        long num = numerator;
        long denom = denominator;
        
        if ((num< 0 && denom > 0) || (num > 0 && denom < 0)) {
            res.append('-');
            if (num < 0) {
                num = Math.abs(num);
            }
            if (denom < 0) {
                denom = Math.abs(denom);
            }
        }
        
        res.append(num / denom);
        if (num % denom != 0) {
            res.append('.');
        }
        num = (num % denom) * 10;
        while (num % denom != 0) {
            if (num_to_index.containsKey(num)) {
                int index = num_to_index.get(num);
                
                res.insert(index, '(');
                res.append(')');
                return res.toString();
            } else {
                quotient = num / denom;
                num_to_index.put(num, res.length());
                res.append(quotient);
                num = (num % denom) * 10;
            }
        }
        
        quotient = num / denom;
        if (quotient != 0) {
            res.append(quotient);
        }
        return res.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("hello");
    }
}