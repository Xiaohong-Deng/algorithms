package LeetCode.hard;

import LeetCode.easy.mergeTwoSorted.ListNode;

public class mergeKSorted {
    private int emptyCount;

    public ListNode[] buildListOfNodes(int[][] listsOfVals) {
        ListNode[] res = new ListNode[listsOfVals.length];
        
        if (listsOfVals.length == 0) {
            return res;
        }

        for (int i = 0; i < listsOfVals.length; i++) {
            res[i] = buildListNode(listsOfVals[i]);
        }

        return res;
    }

    private ListNode buildListNode(int[] vals) {
        if (vals.length == 0)
            return null;

        ListNode head = new ListNode(vals[0]);
        ListNode cur = head;
        for (int i = 1; i < vals.length; i++) {
            ListNode node = new ListNode(vals[i]);
            cur.next = node;
            cur = node;
        }

        return head;
    }

    public ListNode merge(ListNode[] lists) {
        ListNode[] curs = new ListNode[lists.length];
        for (int i = 0; i < curs.length; i++) {
            curs[i] = lists[i];
        }

        initEmptyCount(curs);

        if (curs.length == 0 || allListsEmpty(curs.length)) {
            return null;
        }

        // when the emptyCout is reduced to 1 we must be able to notice it immediately

        // minNodeIdx might return -1 which means all null nodes but we check it before so we dont check here otherwise we have no head to return
        int headIdx = minNodeIdx(curs);
        ListNode head = curs[headIdx];
        int curIdx = headIdx;
        ListNode cur = head;

        if (curs.length - emptyCount == 1) {
            return head;
        }

        curs[curIdx] = curs[curIdx].next;

        if (curs[curIdx] == null) {
            emptyCount++;
        }

        while (!allListsEmpty(curs.length)) {
            // when we are here we have at least one non-empty list, we dont check against -1
            curIdx = minNodeIdx(curs);
            cur.next = curs[curIdx];

            if (curs.length - emptyCount == 1) {
                return head;
            }

            curs[curIdx] = curs[curIdx].next;

            if (curs[curIdx] == null) {
                emptyCount++;
            }

            cur = cur.next;
        }

        return head;
    }

    private void initEmptyCount(ListNode[] curs) {
        emptyCount = 0;
        for (ListNode ln : curs) {
            if (ln == null) {
                emptyCount++;
            }
        }
    }

    private int minNodeIdx(ListNode[] curs) {
        int curIdx = findFirstNonEmpty(curs);

        if (curIdx < 0) {
            return curIdx;
        }

        for (int i = curIdx + 1; i < curs.length; i++) {
            if (curs[i] != null && curs[i].val < curs[curIdx].val) {
                curIdx = i;
            }
        }

        return curIdx;
    }

    private boolean allListsEmpty(int numLists) {
        return emptyCount >= numLists;
    }

    private int findFirstNonEmpty(ListNode[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[][] listsOfVals = {{1, 4, 5}, {1, 3, 4}, {2, 6}};
        mergeKSorted test = new mergeKSorted();
        ListNode[] listOfNodes = test.buildListOfNodes(listsOfVals);
        ListNode merged = test.merge(listOfNodes);
        while (merged != null) {
            System.out.println(merged.val);
            merged = merged.next;
        }
    }
}
