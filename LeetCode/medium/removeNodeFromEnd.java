package LeetCode.medium;

import LeetCode.easy.reverseLinkedList.ListNode;

public class removeNodeFromEnd {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) {
            return null;
        }

        ListNode dummy = new ListNode(0, head);
        ListNode first, second;
        first = second = dummy;

        int i = 0;

        while (first != null && i <= n) {
            first = first.next;
            i++;
        }

        // in problem description 1 <= n <= size
        if (i <= n) {
            return null;
        }

        while (first != null) {
            first = first.next;
            second = second.next;
        }

        if (second == dummy) {
            return null;
        }

        ListNode newNext = second.next.next;
        second.next = newNext;

        return head;
    }
}
