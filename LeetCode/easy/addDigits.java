package LeetCode.easy;


public class addDigits {
  public int getAddDigits(int num) {
    while (num / 10 > 0) {
      int sum = 0;
      while (num != 0) {
        sum += num % 10;
        num = num / 10;
      }
      num = sum;
    }
    return num;
  }
}