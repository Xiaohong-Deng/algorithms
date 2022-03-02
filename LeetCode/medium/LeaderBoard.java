package LeetCode.medium;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class LeaderBoard {
    // store id to score, for addScore
    // the score kept for player is the accumulative score, it is a sum
        private Map<Integer, Integer> scores;
        // store score to number of players having that score, for topK
        private TreeMap<Integer, Integer> sortedScores;
        public LeaderBoard() {
            scores = new HashMap<>();
            sortedScores = new TreeMap<>(Collections.reverseOrder());
        }
        
        public void addScore(int playerId, int score) {
            // we need the old score if playId is already there
            if (!scores.containsKey(playerId)) {
                scores.put(playerId, score);
                // it may be the first to have that score or not
                sortedScores.put(score, sortedScores.getOrDefault(score, 0) + 1);
            } else {
                int oldScore = scores.get(playerId);
                int numOldScore = sortedScores.get(oldScore);
                if (numOldScore == 1) {
                    sortedScores.remove(oldScore);
                } else {
                    sortedScores.put(oldScore, numOldScore - 1);
                }

                int newScore = oldScore + score;
                scores.put(playerId, newScore);
                sortedScores.put(newScore, sortedScores.getOrDefault(newScore, 0) + 1);
            }
        }
        
        public int top(int K) {
            int sum = 0;
            int count = 0;
            for (Map.Entry<Integer, Integer> entry : sortedScores.entrySet()) {
                int score = entry.getKey();
                int num = entry.getValue();
                for (int i = 0; i < num; i++) {
                    sum += score;
                    count++;

                    if (count == K) {
                        return sum;
                    }
                }
            }
            return sum;
        }
        
        // set to 0 aka delete from score
        public void reset(int playerId) {
            int score = scores.get(playerId);
            scores.remove(playerId);
            int num = sortedScores.get(score);
            if (num == 1) {
                sortedScores.remove(score);
            } else {
                sortedScores.put(score, num - 1);
            }
        }    
}
