package LeetCode.hard;

import java.util.HashMap;
import java.util.Map;

public class minWindowSubstring {
    public String minWindow(String s, String t) {
        if (t.length() > s.length()) {
            return "";
        }

        int i, j;
        i = j = 0;

        Map<Character, Integer> tmap = new HashMap<>();
        Map<Character, Integer> smap = new HashMap<>();

        populateTmap(t, tmap);

        char sc;
        while (j - i < t.length()) {
            sc = s.charAt(j);
            if (smap.containsKey(sc)) {
                smap.put(sc, smap.get(sc) + 1);
            } else {
                smap.put(sc, 1);
            }
            j++;
        }

        // now we have the min window lets check if it is valid. If not expand it until it is.
        // if it is valid we return immediately
        if (isWindowValid(tmap, smap)) {
            return s.substring(i, j);
        }

        int mi, mj;
        String minWindow = "";
        mi = 0;
        mj = s.length() + 1;
        while (j < s.length()) {
            j = expandUntilValid(s, j, tmap, smap);

            // now j might be s.length() and window is invalid
            if (j >= s.length() && !isWindowValid(tmap, smap)) {
                return minWindow;
            }
    
            // now we have a valid window lets shrink it until it is invalid
            i = shrinkUntilInvalid(s, t, i, j, tmap, smap);
    
            if (j - i + 1 < mj - mi) {
                mi = i - 1;
                mj = j;
                minWindow = s.substring(mi, mj);
            }

            // now i - 1 to j - 1 inclusive is the valid range, i + 1 to j - 1 is invalid range
        }

        return minWindow;
    }

    private void populateTmap(String t, Map<Character, Integer> tmap) {
        for (char c : t.toCharArray()) {
            if (tmap.containsKey(c)) {
                tmap.put(c, tmap.get(c) + 1);
            } else {
                tmap.put(c, 1);
            }
        }
    }

    // return the exclusive j
    private int expandUntilValid(String s, int j, Map<Character, Integer> tmap, Map<Character, Integer> smap) {
        char sc;
        while (j < s.length()) {
            sc = s.charAt(j);
            if (tmap.containsKey(sc)) {
                if (smap.containsKey(sc)) {
                    smap.put(sc, smap.get(sc) + 1);
                } else {
                    smap.put(sc, 1);
                }

                // if sc in tmap it means we have important change in our window we need to check if window is valid
                if (isWindowValid(tmap, smap)) {
                    j++;
                    return j;
                }
            }
            j++;
        }
        return j;
    }

    // return the exclusive i, the real start is i - 1
    private int shrinkUntilInvalid(String s, String t, int i, int j, Map<Character, Integer> tmap, Map<Character, Integer> smap) {
        char sc;
        while (j - i > t.length()) {
            sc = s.charAt(i);
            if (tmap.containsKey(sc)) {
                int tcount = tmap.get(sc);
                int scount = smap.get(sc);
                if (tcount == scount) {
                    // we prepare for the next round to find a new valid window, i - 1 to j - 1 inclusive is valid
                    if (scount > 1) {
                        smap.put(sc, scount - 1);
                    } else {
                        smap.remove(sc);
                    }
                    i++;
                    return i;
                } else {
                    smap.put(sc, smap.get(sc) - 1);
                }
            }
            i++;
        }

        // if we return here it means j - i == t.length(), the window i to j - 1 inclusive is valid
        // to make it consistent with the other return we need i + 1
        return i + 1;
    }

    private boolean isWindowValid(Map<Character, Integer> tmap, Map<Character, Integer> smap) {
        for (char c : tmap.keySet()) {
            if (!smap.containsKey(c) || tmap.get(c) > smap.get(c)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String s = "ADOBECODEBANC"; // 13 chars
        String t = "ABC";
        minWindowSubstring test = new minWindowSubstring();
        System.out.println(test.minWindow(s, t));

        s = "a";
        t = "a";
        System.out.println(test.minWindow(s, t));

        s = "ab";
        t = "b";
        System.out.println(test.minWindow(s, t));

    }
}
