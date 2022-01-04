package LeetCode.medium;
import java.util.HashMap;

public class songSixty {
    public int getNumSongs(int[] time) {
        HashMap<Integer, Integer> prevMod = new HashMap<>();
        int modRes, ans, comp;
        ans = 0;
        for (int i = 0; i < time.length; i++) {
            modRes = time[i] % 60;
            comp = (60 - modRes) % 60;
            if (prevMod.containsKey(comp)) {
                ans += prevMod.get(comp);
            }

            if (prevMod.containsKey(modRes)) {
                prevMod.put(modRes, prevMod.get(modRes) + 1);
            } else {
                prevMod.put(modRes, 1);
            }
        }
        return ans;
    }

    public int getNumSongsFreq(int[] time) {
        int[] freq = new int[60];
        int modRes, ans;
        ans = 0;
        for (var t : time) {
            modRes = t % 60;
            if (modRes == 0) {
                ans += freq[modRes];
            } else {
                ans += freq[60 - modRes];
            }
            freq[modRes]++;
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] time = {60, 60, 60};
        songSixty test = new songSixty();
        System.out.println(test.getNumSongsFreq(time));
    }
}
