package LeetCode.easy;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class deleteNodeInLinkedList {
  public static class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
  }
  public void deleteNode(ListNode node) {
    ListNode next = node.next;
    node.val = next.val;
    node.next = next.next;
  }
}