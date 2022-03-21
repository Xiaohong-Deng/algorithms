package LeetCode.easy;
import java.util.HashSet;
import java.util.Set;

import LeetCode.easy.reverseLinkedList.ListNode;;

public class cycyleLinkedList {
    public boolean hasCycle(ListNode head) {
        Set<ListNode> seen = new HashSet<ListNode>();
        while (head != null) {
            if (seen.contains(head)) {
                return true;
            }
            seen.add(head);
            head = head.next;
        }

        return false;
    }
}
