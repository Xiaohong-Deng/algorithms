package LeetCode.easy;

public class mergeTwoSorted {
    public static class ListNode {
        public int val;
        public ListNode next;
        public ListNode() {}
        public ListNode(int val) { this.val = val; }
        public ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }

        if (l2 == null) {
            return l1;
        }

        ListNode head;
        ListNode cur;

        if (l1.val > l2.val) {
            head = l2;
            l2 = l2.next;
        } else {
            head = l1;
            l1 = l1.next;
        }

        cur = head;

        while (l1 != null && l2 != null) {
            if (l1.val > l2.val) {
                cur.next = l2;
                l2 = l2.next;
            } else {
                cur.next = l1;
                l1 = l1.next;
            }

            cur = cur.next;

        }

        if (l1 == null) {
            cur.next = l2;
        } else {
            cur.next = l1;
        }

        return head;
    }
}
