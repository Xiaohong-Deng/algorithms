public class ReversedLinkedList {
	private Node reFirst;

	public static class Node {
		private int val;
		private Node next;

		public Node(int val, Node next) {
			this.val = val;
			this.next = next;
		}

		public int getVal() {
			return this.val;
		}
	}		

	public ReversedLinkedList(Node first) {
		if (first == null) return first;
		Node currentNode = first;
		Node next = currentNode.next;
		Node newNext = null;
		while (next != null) {
			currentNode.next = newNext;
			newNext = currentNode;
			currentNode = next;
			next = currentNode.next;
		}
		currentNode.next = newNext;
		reFirst = currentNode;
	}

	public Node getRevFirst() {
		return reFirst;
	}

  public static void main(String[] args) {
    Node first = new Node(12, new Node(24, new Node(36, null)));
    ReversedLinkedList testcase = new ReversedLinkedList(first);
    Node revFirst = testcase.getRevFirst();
    System.out.println("reversed first value: " + revFirst.getVal());
  }
}