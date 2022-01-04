package LeetCode.easy;

public class compBaseTen {
    public int bitwiseComplement(int n) {
        int remain = n;
        int bit = 1;
        
        // handle n = 0, comp = 1
        n = n ^ bit;
        remain >>= 1;
        bit <<= 1;
        
        while (remain != 0) {
            n = n ^ bit;
            remain >>= 1;
            bit <<= 1;
        }
        return n;
    }
}
