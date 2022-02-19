package LeetCode.medium;

public class angleClock {
    public double getAngle(int hour, int minutes) {
        double tickHour;
        hour = hour % 12;
        tickHour = hour * 5 + (minutes / 60.0) * 5;
        double absDiff = Math.abs(tickHour - (double) minutes);
        if (absDiff > 30.0) {
            absDiff = 60 - absDiff;
        }
        return (absDiff / 60.0) * 360.0;
    }

    public static void main(String[] args) {
        int hour, minutes;
        hour = 12;
        minutes = 30;

        angleClock t = new angleClock();
        System.out.println(t.getAngle(hour, minutes));
    }
}
