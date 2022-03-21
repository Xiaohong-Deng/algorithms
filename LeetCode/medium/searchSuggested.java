package LeetCode.medium;

import java.util.ArrayList;
import java.util.List;

public class searchSuggested {
    public static class Trie {
        private static int size = 26;
        private TrieNode root;
        public static class TrieNode {
            private boolean value = false;
            private TrieNode[] next = new TrieNode[size];
        }

        public Trie() {
            root = new TrieNode();
        }

        public void insert(String s) {
            char c;
            int len = s.length();
            TrieNode cur = root;
            for (int i = 0; i < len; i++) {
                c = Character.toLowerCase(s.charAt(i));
                if (cur.next[c - 'a'] == null) {
                    cur.next[c - 'a'] = new TrieNode();
                }
                cur = cur.next[c - 'a'];
            }
            cur.value = true;
        }

        public List<String> startWith(String prefix, int k) {
            List<String> ans = new ArrayList<>();

            TrieNode cur = root;
            int len = prefix.length();
            char c;

            for (int i = 0; i < len; i++) {
                c = Character.toLowerCase(prefix.charAt(i));
                if (cur.next[c - 'a'] == null) {
                    return ans;
                }
                cur = cur.next[c - 'a'];
            }

            if (cur.value) {
                ans.add(prefix);
            }

            StringBuilder sb = new StringBuilder(prefix);

            for (int i = 0; i < size; i++) {
                if (cur.next[i] != null) {
                    sb.append((char) (i + 'a'));
                    dfs(cur.next[i], ans, sb, k);
                    sb.deleteCharAt(sb.length() - 1);
                }
            }

            return ans;
        }

        private void dfs(TrieNode tn, List<String> ans, StringBuilder sb, int k) {
            if (ans.size() >= k) {
                return;
            }

            if (tn.value) {
                ans.add(sb.toString());
            }

            for (int i = 0; i < size; i++) {
                if (tn.next[i] != null) {
                    sb.append((char) (i + 'a'));
                    dfs(tn.next[i], ans, sb, k);
                    sb.deleteCharAt(sb.length() - 1);
                }
            }

            return;
        }
    }

    public List<List<String>> suggestedProducts(String[] products, String searchWord) {
        List<List<String>> ans = new ArrayList<>();
        int len = searchWord.length();
        if (len < 2) {
            return ans;
        }
        Trie t = new Trie();

        initTrie(t, products);

        for (int i = 1; i < len + 1; i++) {
            ans.add(t.startWith(searchWord.substring(0, i), 3));
        }

        return ans;
    }

    private void initTrie(Trie t, String[] products) {
        for (String s : products) {
            t.insert(s);
        }
    }

    public static void main(String[] args) {
        String[] products = {"mobile","mouse","moneypot","monitor","mousepad"};
        String searchWord = "mouse";

        searchSuggested ss = new searchSuggested();

        List<List<String>> lls = ss.suggestedProducts(products, searchWord);

        for (List<String> ls : lls) {
            System.out.println(ls.toString());
        }
    }
}
