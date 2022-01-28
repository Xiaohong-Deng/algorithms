package LeetCode.hard;

import LeetCode.easy.mergeTwoSorted;
import LeetCode.easy.mergeTwoSorted.ListNode;

public class mergeKSortedDC {
    public ListNode merge(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }

        if (lists.length == 1) {
            return lists[0];
        }

        ListNode[] recursiveLists = mergeInPairs(lists);

        while (recursiveLists.length > 1) {
            recursiveLists = mergeInPairs(recursiveLists);
        }

        return recursiveLists[0];
    }

    private ListNode[] mergeInPairs(ListNode[] lists) {
        ListNode[] res = new ListNode[lists.length / 2 + lists.length % 2];
        mergeTwoSorted mergeTwo = new mergeTwoSorted();
        for (int i = 0; i + 1 < lists.length; i += 2) {
            res[i / 2] = mergeTwo.mergeTwoLists(lists[i], lists[i + 1]);
        }

        if (lists.length % 2 == 1) {
            res[res.length - 1] = lists[lists.length - 1];
        }

        return res;
    }

    public static void main(String[] args) {
        int[][] listsOfVals = {{1, 4, 5}, {1, 3, 4}, {2, 6}};
        mergeKSorted builder = new mergeKSorted();
        ListNode[] lists = builder.buildListOfNodes(listsOfVals);
        mergeKSortedDC test = new mergeKSortedDC();
        ListNode merged = test.merge(lists);
        while (merged != null) {
            System.out.println(merged.val);
            merged = merged.next;
        }
    }
}
