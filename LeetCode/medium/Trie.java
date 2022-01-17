package LeetCode.medium;

public class Trie {
    private static int size = 26;
    private TrieNode root;

    private static class TrieNode {
        private boolean value = false;
        private TrieNode[] next = new TrieNode[size];
    }

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (cur.next[c - 'a'] == null) {
                cur.next[c - 'a'] = new TrieNode();
            }
            cur = cur.next[c - 'a'];
        }
        cur.value = true;
    }
    
    public boolean search(String word) {
        TrieNode cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (cur.next[c - 'a'] == null) {
                return false;
            }
            cur = cur.next[c - 'a'];
        }
        return cur.value;
    }
    
    public boolean startsWith(String prefix) {
        TrieNode cur = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            if (cur.next[c - 'a'] == null) {
                return false;
            }
            cur = cur.next[c - 'a'];
        }
        if (cur.value) {
            return true;
        }

        for (int i = 0; i < size; i++) {
            if (cur.next[i] != null)
                return true;
        }

        return false;
    }

    public static void main(String[] args) {
        Trie test = new Trie();
        test.insert("apple");
        assert(test.search("apple"));
        assert(!test.search("app"));
        assert(test.startsWith("app"));
        test.insert("app");
        assert(test.search("app"));
    }
}
