import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class BranchBound {
  private Item[] items;
  private int[] weights;
  private int[] values;
  int[] weightsOrderByDense;
  int[] valuesOrderByDense;
  int[] countLevelMembers;
  private int itemSize;
  private int packSize;
  private int N;
  
  private class Item implements Comparable<Item> {
    private double vwRatio;
    private int index;
    
    public Item(int weight, int value, int index) {
      vwRatio = ((double) value) / weight;
      this.index = index;
    }
    
    public int compareTo(Item that) {
      if (this.vwRatio > that.vwRatio) return 1;
      if (this.vwRatio < that.vwRatio) return -1;
      return 0;
    }
  }
  
  private class Node {
    private int valInPack;
    private int spaceRemained;
    private double valOpt;
    private int level;
    private int[] taken;
    
    public Node(int val, int space, double valOpt, int level, int[] taken) {
      this.valInPack = val;
      this.spaceRemained = space;
      this.level = level;
      this.valOpt = valOpt;
      this.taken = taken;
    }
  }
  
  public BranchBound(int itemSize, int packSize) {
    this.itemSize = itemSize;
    this.packSize = packSize;
    items = new Item[itemSize];
    weights = new int[itemSize];
    values = new int[itemSize];
    countLevelMembers = new int[itemSize + 1];
  }
  
  public int solver() {
    int valOptRoot = (int) Math.floor(relaxRoot());
    Stack<Node> nodes = new Stack<Node>();
    int[] taken = new int[0];
    nodes.push(new Node(0, packSize, valOptRoot, 0, taken));//index = 0, meaning no items considered yet 
    int valBest = greedy(taken, 0);
    Node currentNode = null;
    while (!nodes.isEmpty()) {
      currentNode = nodes.pop();
      countLevelMembers[currentNode.level]++;
      int idx = currentNode.level;
      int nextLevel = idx + 1;
      if (idx == itemSize || currentNode.valOpt <= valBest) continue;
      Node left = null;
      Node right = null;
      if (currentNode.spaceRemained >= weightsOrderByDense[idx]) {
        int[] takenLeft = new int[currentNode.taken.length + 1];
        for (int i = 0; i < takenLeft.length; i++) {
          if (i != takenLeft.length - 1) takenLeft[i] = currentNode.taken[i];
          else takenLeft[i] = idx;
        }
        left = new Node(valuesOrderByDense[idx] + currentNode.valInPack, (currentNode.spaceRemained - weightsOrderByDense[idx]), currentNode.valOpt, nextLevel, takenLeft);
        if (left.valInPack >= valBest) {
          valBest = left.valInPack;
//          StdOut.println("best update: " + valBest);
        }
      }
      int[] takenRight = currentNode.taken;
      int nextValOpt = (int) relaxation(takenRight, idx);
      int nextGreedy = greedy(takenRight, nextLevel); // greedy and linear relaxation both take greedy paradigm but the latter can take fractions so
      // it's not possible for greedy to be greater than relaxation 
      if (nextGreedy > valBest) {
        valBest = nextGreedy; // better keep greedy taken list in a var in case its the global best
        if (nextGreedy == nextValOpt) {
          if (left != null) nodes.push(left);
          continue;
        }
      }
//      StdOut.println("nextValOpt: " + nextValOpt);
      if (nextValOpt > valBest) {
        right = new Node(currentNode.valInPack, currentNode.spaceRemained, nextValOpt, nextLevel, takenRight);
      }
      if (right != null) nodes.push(right); // push order does matter!
      if (left != null) nodes.push(left);
    }
    return valBest;
  }
  
  private double relaxRoot() {
    Arrays.sort(items, Collections.reverseOrder());
    for (int i = 0; i < items.length; i++) {
      StdOut.println(items[i].vwRatio);
    }
    weightsOrderByDense = new int[itemSize];
    valuesOrderByDense = new int[itemSize];
    for (int i = 0; i < itemSize; i++) {
      int idx = items[i].index;
      weightsOrderByDense[i] = weights[idx];
      valuesOrderByDense[i] = values[idx];
    }
    return relaxation(new int[0], 0);
  }
  
  private double relaxation(int[] taken, int level) {
    double bound = 0;
    int weight = 0;
    int len = items.length;
    for (int i = 0; weight < packSize && i < len; i++) {
      boolean isDiscarded = true;
      if (weightsOrderByDense[i] <= (packSize - weight)) {
        if (i < level) {
          for (int j = 0; j < taken.length; j++) {
            if (taken[j] == i) {
              isDiscarded = false;
              break;
            }
          }
          if (!isDiscarded) {
            bound += valuesOrderByDense[i];
            weight += weightsOrderByDense[i];
          }
        } else {
          bound += valuesOrderByDense[i];
          weight += weightsOrderByDense[i];
        }
      } else {
        if (i < level) {
          for (int j = 0; j < taken.length; j++) {
            if (taken[j] == i) {
              isDiscarded = false;
              break;
            }
          }
          if (!isDiscarded) {
            bound += items[i].vwRatio * (packSize - weight);
            weight = packSize;
          }
        } else {
          bound += items[i].vwRatio * (packSize - weight);
          weight = packSize;
        }
      }
    }
    return bound;
  }
  
  private int greedy(int[] taken, int level) {
    int weight = 0;
    int value = 0;
    for (int i = 0; i < items.length; i++) {
      //      int idx = items[i].index;
      if (weightsOrderByDense[i] <= packSize - weight) {
        if (i < level) {
          boolean isDiscarded = true;
          for (int j = 0; j < taken.length; j++) {
            if (taken[j] == i) {
              isDiscarded = false;
              break;
            }
          }
          if (!isDiscarded) {
            weight += weightsOrderByDense[i];
            value += valuesOrderByDense[i];
          }
        } else {
          weight += weightsOrderByDense[i];
          value += valuesOrderByDense[i];
        }
      }
    }
    return value;
  }
  
  public void insert(int value, int weight, int index) {
    items[N] = new Item(weight, value, index);
    weights[N] = weight;
    values[N] = value;
    N++;
  }

  public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub
    File in = new File(args[0]);
    Scanner scanner = new Scanner(in);
    int itemSize = scanner.nextInt();
    int packSize = scanner.nextInt();
    BranchBound problem = new BranchBound(itemSize, packSize);
    for (int i = 0; i < itemSize; i++) {
      int value = scanner.nextInt();
      int weight = scanner.nextInt();
      problem.insert(value, weight, i);
    }
    scanner.close();
    System.out.println("best: " + problem.solver());
    for (int i = 0; i < problem.countLevelMembers.length; i++) {
      System.out.println("level " + i + " number of members: " + problem.countLevelMembers[i]);
    }
  }

}
