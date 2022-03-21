package LeetCode.easy;

import java.util.ArrayList;
import java.util.List;

public class OrderedStream {
    private int ptr;
    private String[] vals;
    public OrderedStream(int n) {
        vals = new String[n];
        ptr = 0;
    }
    
    // it needs to return longest chunk from ptr until there is a hole or the end, do not reset ptr
    // in this case if we concatenate all returned lists we get the whole array.
    public List<String> insert(int idKey, String value) {
        List<String> ans = new ArrayList<>();

        vals[idKey - 1] = value;

        while (ptr < vals.length && vals[ptr] != null) {
            ans.add(vals[ptr]);
            ptr++;
        }

        return ans;
    }
}
