public class ReverseLinkedList {
	private Node reFirst;

	private class Node {
		private int val;
		private Node next;

		public Node(int val, Node next) {
			this.val = val;
			this.next = next;
		}
	}

	public ReverseLinkedList(Node first) {
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
    Node first = new Node(12, null);
  }
}