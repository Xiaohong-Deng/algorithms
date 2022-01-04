package LeetCode.easy;
import java.util.Arrays;

public class validAnagram {
  public boolean isAnagram(String s, String t) {
    if (s == null && t == null) return true;
    if (s == null || t == null) return false;
    if (s.length() != t.length()) return false;
    char[] char1 = s.toCharArray();
    Arrays.sort(char1);
    char[] char2 = t.toCharArray();
    Arrays.sort(char2);
    for (int i = 0; i < char1.length; i++) {
      if (char1[i] != char2[i]) return false;
    }
    return true;
  }
}