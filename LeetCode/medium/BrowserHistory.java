package LeetCode.medium;

public class BrowserHistory {
    private Node now;
    public static class Node {
        private String val;
        private Node prev;
        private Node next;

        public Node(String val, Node prev, Node next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }
    public BrowserHistory(String homepage) {
        now = new Node(homepage, null, null);
    }
    
    public void visit(String url) {
        Node next = new Node(url, now, null);
        now.next = next;
        now = next;
    }
    
    public String back(int steps) {
        while (steps != 0 && now.prev != null) {
            now = now.prev;
            steps--;
        }
        return now.val;
    }
    
    public String forward(int steps) {
        while (steps != 0 && now.next != null) {
            now = now.next;
            steps--;
        }

        return now.val;
    }
}
