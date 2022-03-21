package LeetCode.hard;

/**
 * vowelPerm
 */
public class vowelPerm {

    // the question requires us to return result % 10^9+7
    // so we can apply this mod after each time we apply arithmetic operation to any component of final result
    // if (a+b) % MOD we can apply % MOD to a and b every time we apply arithmetic operations to them
    public int countVowelPermutation(int n) {
        long aCnt, eCnt, iCnt, oCnt, uCnt;
        aCnt = eCnt = iCnt = oCnt = uCnt = 1;
        long MOD = 1000000000 + 7;

        for (int i = 1; i < n; i++) {
            long aCntTemp = (eCnt + iCnt +uCnt) % MOD;
            long eCntTemp = (aCnt + iCnt) % MOD;
            long iCntTemp = (eCnt + oCnt) % MOD;
            long oCntTemp = iCnt % MOD;
            long uCntTemp = (oCnt + iCnt) % MOD;
            aCnt = aCntTemp;
            eCnt = eCntTemp;
            iCnt = iCntTemp;
            oCnt = oCntTemp;
            uCnt = uCntTemp;
        }

        long result = (aCnt + eCnt + iCnt + oCnt + uCnt) % MOD;
        return (int) result;
    }
}