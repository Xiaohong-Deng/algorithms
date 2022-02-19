package LeetCode.hard;

public class numOfOne {
    private int count;
    public int countDigitOne(int n) {
        if (n == 0) {
            return 0;
        }

        count = 0;

        getCountMath(n);

        return count;
    }

    private void getCount(int n) {
        if (n == 0) {
            return;
        }

        int temp = n;

        while (temp > 0) {
            if (temp % 10 == 1) {
                count++;
            }
            temp = temp / 10;
        }

        getCount(n - 1);
    }

    private void getCountMath(int n) {
        for (long i = 1; i <= n; i *= 10) {
            long divider = i * 10;
            count += (n / divider) * i + Math.min(Math.max(n % divider - i + 1, 0), i);
        }
    }

    public static void main(String[] args) {
        int n = 13;
        numOfOne t = new numOfOne();

        System.out.println(t.countDigitOne(n));
    }
}
