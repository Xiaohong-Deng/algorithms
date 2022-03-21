package LeetCode.contests.Mar5;

import java.util.ArrayList;
import java.util.List;

class cellRange {
    public List<String> cellsInRange(String s) {
        char startCol = s.charAt(0);
        char endCol = s.charAt(3);
        char startRow = s.charAt(1);
        char endRow = s.charAt(4);
        
        List<String> ans = new ArrayList<>();

        for (int i = 0; startCol + i <= endCol; i++) {
            for (int j = 0; startRow + j <= endRow; j++) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) (startCol + i));
                sb.append((char) (startRow + j));
                ans.add(sb.toString());
            }
        }

        return ans;
    }
}