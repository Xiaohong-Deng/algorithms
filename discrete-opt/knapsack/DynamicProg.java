/*
data type for solving 0-1 knapsack problem. Input file must be of
the following form:
number if items [whitespace] total size of knapsack
value of item1 [whitespace] weight of item1
value of item2 [whitespace] weight of item2
...
Output format should be of the following:
value of the optimal solution
index of 1st item selected [whitespace] index of 2nd item selected ...
*/

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.Scanner;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;
/*
The problem solving model used is Dynamic Programming. Memoization is hard coded to
enhance the performance
*/
public class DynamicProg {
  private Map<List<Integer>, Integer> cache = new HashMap<>();
  private int[] weights;
  private int[] values;
  private int itemSize;
  private int packSize;
  private boolean isSolved;
//  private int cacheHit;

  public DynamicProg(int itemSize, int packSize, int[] weights, int[] values) {
    this.itemSize = itemSize;
    this.packSize = packSize;
    this.weights = weights;
    this.values = values;
  }

  public int solver() {
    isSolved = true;
    return solver(itemSize, packSize);
  }

  public void getCache() {
    for (Map.Entry<List<Integer>, Integer> entry: cache.entrySet()) {
      StdOut.println(entry.getKey() + "->" + entry.getValue());
    }
  }

//  public int getCacheHit() {
//    return cacheHit;
//  }
  //@param number of items, total size of knapsack
  //@return value of the opt solution
  private int solver(int itemSize, int packSize) {
    //List object can be used to calculate hashCode, the result is used as Key in hash table
    final List<Integer> itemAndPack = Arrays.asList(itemSize, packSize);
    if (itemSize == 0 || packSize == 0) return 0;
    if (cache.containsKey(itemAndPack)) {
//      cacheHit++;
      return cache.get(itemAndPack);
    }
    int rv = 0;
    if (weights[itemSize - 1] > packSize) rv = solver(itemSize - 1, packSize);
    else {
      int sol1 = solver(itemSize - 1, packSize);
      int sol2 = solver(itemSize - 1, packSize - weights[itemSize - 1]) + values[itemSize - 1];
      rv = sol1 > sol2 ? sol1 : sol2;
    }
    cache.put(itemAndPack, rv);
    return rv;
  }

  //@return list of items which are selected as opt solution
  public Iterable<Integer> itemList() {
    if (!isSolved) return null;
    Queue<Integer> itemList = new Queue<Integer>();
    int currentItemSize = itemSize;
    int currentPackSize = packSize;
    for (int i = itemSize; i >= 0; i--) {
      if (currentPackSize == 0) {
        break;
      }
      int oneExclude = cache.get(Arrays.asList(currentItemSize - 1, currentPackSize));
      int current = cache.get(Arrays.asList(currentItemSize, currentPackSize));
      if (oneExclude != current) {
        StdOut.println("current val in pack: " + current);
        StdOut.println("current val in pack with current item excluded: " + oneExclude);
        StdOut.println("current packsize: " + currentPackSize);
        StdOut.println("item included: " + currentItemSize);
        StdOut.println("item weight: " + weights[currentItemSize - 1]);
        itemList.enqueue(i);
        currentPackSize -= weights[currentItemSize - 1];
      }// else itemList.enqueue(0);
      currentItemSize--;
    }
    return itemList;
  }

  public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub
    File in = new File(args[0]);
    Scanner scanner = new Scanner(in);
    int itemSize = scanner.nextInt();
    int packSize = scanner.nextInt();
    int[] values = new int[itemSize];
    int[] weights = new int[itemSize];
    for (int i = 0; i < itemSize; i++) {
      values[i] = scanner.nextInt();
      weights[i] = scanner.nextInt();
    }
    scanner.close();

    DynamicProg problem = new DynamicProg(itemSize, packSize, weights, values);
    int maxVal = problem.solver();
    StdOut.println("The optimal value is: " + maxVal);

//    problem.getCache();

    for (Integer i: problem.itemList()) {
      StdOut.println("pack included:  " + i);
    }
  }
}
