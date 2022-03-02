package LeetCode.medium;

public class flattenMultilevelDLL {
    public static class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;

        public Node(int val, Node prev, Node next, Node child) {
            this.val = val;
            this.prev = prev;
            this.next = next;
            this.child = child;
        }
    }
    public Node flatten(Node head) {
        Node cur = head;
        while (cur != null) {
            if (cur.child != null) {
                insertChild(cur, cur.next);
                cur.child = null;
            }
            cur = cur.next;
        }

        return head;
    }

    private void insertChild(Node parent, Node parentNext) {
        Node head = parent.child;
        Node cur = head;
        Node tail = head;

        while (cur != null) {
            tail = cur;
            if (cur.child != null) {
                insertChild(cur, cur.next);
                cur.child = null;
            }
            cur = cur.next;
        }
        parent.next = head;
        head.prev = parent;
        if (parentNext != null) {
            tail.next = parentNext;
            parentNext.prev = tail;
        }
    }
}
