package LeetCode.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

public class meetingRoomTwo {
    public int minMeetingRooms(int[][] intervals) {
        List<int[]> rooms = new ArrayList<>();

        Arrays.sort(intervals, Comparator.comparingInt(i -> i[0]));

        rooms.add(intervals[0]);
        for (int j = 1; j < intervals.length; j++) {
            int roomIdx = -1;
            for (int k = 0; k < rooms.size(); k++) {
                if (!isOverlapped(intervals[j], rooms.get(k))) {
                    roomIdx = k;
                    break;
                }
            }

            if (roomIdx != -1) {
                rooms.remove(roomIdx);
            }
            rooms.add(intervals[j]);
        }

        return rooms.size();
    }

    private boolean isOverlapped(int[] interval1, int[] interval2) {
        int[] first, last;
        if (interval1[0] >= interval2[0]) {
            first = interval2;
            last = interval1;
        } else {
            first = interval1;
            last = interval2;
        }

        // if start time is the same as the other end time, it is not overlapped in this problem
        if (first[1] > last[0]) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        int[][] intervals1 = {{0,30},{5,10},{15,20}};
        int[][] intervals2 = {{7,10}, {2,4}};

        meetingRoomTwo test = new meetingRoomTwo();
        int ans1 = test.minMeetingRooms(intervals1);
        int ans2 = test.minMeetingRooms(intervals2);

        System.out.println(ans1);
        System.out.println(ans2);
    }
}
