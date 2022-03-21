package LeetCode.medium;

import java.util.Arrays;
import java.util.List;

public class wordBreak {
    public static class Trie {
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

        public boolean search(char[] cs, int start, int end) {
            TrieNode cur = root;
            for (int i = start; i < end; i++) {
                char c = cs[i];
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
    }

    public boolean wBreak(String s, List<String> wordDict) {
        Trie trie = new Trie();
        for (String str : wordDict) {
            trie.insert(str);
        }

        boolean[] cache = new boolean[s.length() + 1];
        cache[0] = true;

        char[] sChars = s.toCharArray();

        // we assume [0, j] has a valid breakdown
        // we check if [j, i] is a valid word
        // if both yes we found it
        // we find first such one by [0, 0], [0, i] which is the first word in the valid breakdown
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (cache[j] && trie.search(sChars, j, i)) {
                    cache[i] = true;
                    break;
                }
            }
        }

        return cache[sChars.length];
    }

    public static void main(String[] args) {
        String s = "catsandog";
        String[] wordDict = {"cats","dog","sand","and","cat"};

        String s2 = "aaaaaa";
        String[] wordDict2 = {"aaaa", "aa"};

        wordBreak t = new wordBreak();
        System.out.println(t.wBreak(s2, Arrays.asList(wordDict2)));
    }
}
