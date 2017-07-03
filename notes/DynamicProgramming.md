# Dynamic Programming

1. what are the sub-problems?
2. the optimal solution to the sub-problem
3. optimal substructure: a problem is said to have optimal substructure if the optimal solution can be constructed from the optimal solutions to the sub-problems. I comprehended it as the optimal solutions of the sub-problem are nested in the optimal solution to the problem. Optimal substructure might be equivalent to optimal solutions to sub-problems to my opinion.

### You can find the following three problems on [LeetCode](https://leetcode.com)

## In a matrix filled with 0 and 1, find the maximum square sub-matrix with all 1's

### Q: what is the last part of the optimal solution?

The lower right cell of the sub-matrix.

### Q: what are the relevant sub-problems?

This question is harder. The sub-problems must be similar to the original one. They must be the maximum square sub-matrices based off of their own last parts. Those last parts must be the second last parts in the original optimal solution. Those are the 3 neighbors of the last part. 

The sub-problem is what is the optimal solution given one of the neighbors is the last part. We know that those solutions can not be better than the global optimal because it'll contradict the condition. Say the optimal is a k by k matrix the optimals to the 3 sub-problems are at most k by k but at least one of them has to be (k-1) by (k-1). Going from lower right to upper left, the searching space is shrinking, thus this is a smaller sub-problem.

But given a cell containing 1, we have no idea if it is the lower right cell of the optimal. We can only assuming. In that case we must consider all possibilities. The problem becomes given the cell is the lower right cell of some sub-matrix, what is the maximum size that matrix could be? In this scenario it is possible that the opt solutions to the 3 sub-problems have one or more better sub-matrices. Nonetheless, the way to construct the opt from the sub opt is the same.

The optimal can be constructed by

    optimal_size = min(opt_size(left), opt_size(upper), opt_size(upper-left)) + 1

We start from the lower right of the matrix. If it's a 0, then we peel it off, ask for the sub-problem -- what is the maximum square matrix in the remaining cells. If it's a 1, we want to know what is the maximum square matrix using it as the lower right cell.

It's like the situation in the 0-1 knapsack problem. Starting from the last candidate item **n**, if it's not in the optimal solution, we ask for the sub-problem: Given the knapsack size **k**, items 1, 2, ..., n-1, what is the optimal solution S(n-1, k)? Or if it's in the optimal solution, we ask for the sub-problem: for the knapsack **k - size(n)**, items 1, 2, ..., n-1, what is the optimal solution S(n-1, k-size(n))?

**The most tricky part of dynamic programming problem is construct the optimal solution from the opt solutions of the sub-problems. Sometimes the opt solutions to the sub-problems can be induced by peeling off the last part of the opt solution of the original problem, thus revealing the opt solutions of the sub-problems together with the sub-problems. As a bonus we can construct the optimal solution by simply adding the last part back to the optimal solution of the sub-problem like in the 0-1 knapsack problem. Sometimes it's not that obvious. The general approach to deal with this is remained to be found for me.**

### Q: what is the time complexity of this DP algorithm?
We start from the 1st row and the 1st column, the the remaining of the 2nd row and the 2nd column. Going from upper left to lower right, the computation for each cell is constant, e.g. 3 array look-ups. Time complexity is **O(m * n)**

## Given n non-negative integers representing the histogram's bar height where the width of each bar is 1, find the area of largest rectangle in the histogram.

![histo](histogram.png)![histo_area](histogram_area.png)

### Q: Does the optimal solution have some sort of last part?

Seems not. Not a single part can be identified as the last part in an ordered way. However, there is an invariant(we can call the last part from other problems as an invariant, too). The optimal solution must contain at least one entire histogram. If all the histograms involved in the optimal solution contribute only partial of them, the area of the optimal rectangle can be raised higher. We only have to look for all the rectangles that includes at least one histogram entirely, pick the largest. Each of those rectangles is also the largest rectangle for each of the histograms that are entirely included.

### The algorithmic way of solving this

1. We can take histograms one by one, from left to right, look both directions of it seeking for the first histogram that is lower than it to pinpoint the left boundary and the right boundary. The time complexity for the worst case scenario is **O(n^2)**
2. There is a stack based method as follows. You can read the explanation before reading the steps.
  
  + Starting from the first histogram, push it to the stack, keep pushing the next histograms as long as the histo gets pushed is higher than the histo on the top of the stack, thus keep an **increasing(for simplicity we use increasing here, later we discuss non-decreasing) order** in height in the stack.
  + Once encountered a histo that is lower than the current top item, we pop the top item `top`, calculating the maximum rectangular area using it as the smallest but the only complete histo in the rectangle.
  + **The right boundary** is determined by the lower histo we just encountered. **The left boundary** is determined by the current histo on the top of the stack, which is the left neighbor of our `top` when they were both in the stack. This is attributed to the fact that the height of the items in the stack is always in a increasing order.
  + After dealing with `top`, we check the next top item in the stack, compare it with the current histo according to the criteria
  + If the stack is empty or the next top is lower than the current histo, push the current histo to the stack and move on the the next iteration.
  + Edge case: either we keep an increasing order and pop an item when a histo with the same height of the current top item is encountered, or we keep a non-decreasing order. Either way we need to deal with an edge case. Let's take the latter as an example. When we encounter a lower histo and pop the `top`, its left neighbor is of the same height as `top`. Say the height is `h`. According to the mechanism we set before, we won't be able to compute the correct area for `top`. But we continue popping items of height `h`, computing the wrong area until the last `h` high histo is reached. With the current encountered histo as the right boundary and the left neighbor which is now lower than `h` high `top` we are dealing with, we calculate the correct maximum area for `top` and it is the correct area for all the miscalculated histo with height of `h`. Because they share the same largest area.
  + The time complexity of it is **O(n)** due to the fact that each histo gets pushed and popped once, calculating area requires constant amount of time for each histo.

### Is the stack based algorithm a dynamic programming method?

Obviously it is a method of exhaustion. And dynamic programming is exhaustive. But there are some things missing.

+ Peeling off the invariant of the optimal solution gives you neither the sub-problems nor the slightest hint of the optimal solutions to the sub-problems. **There is no sub-problems.**
+ As a consequence you don't construct the optimal solutions from the optimal substructures.

## In a matrix filled with 0 and 1, find the maximum sub-matrix with all 1's

### Q: what is the last part of the optimal solution?

The last row of the sub-matrix.

### Q: what is the relevant sub-problem?

The original optimal sulotion is the maximum sub-matrix with all 1's in the given matrix. **Given the last row of the optimal solution is the ith row of the matrix, the original opt is also the maximum matrix you can find using row i as the last row.** If we peel off the last row and ask the following question: **from row 0 to row (i-1), what is the maximum sub-matrix with all 1's using row (i-1) as the last row?** Is this a legitimate sub-problem? If it is, how can we construct the optimal solution from the opts of the sub-problems?

After we finish the computation in the **(i-1)**th row, we must carry over a peice of information to the **i**th row computation. We rely on this peice of info and the newly added row to tell us what are the newly formed sub-matrices with all 1's? Besides that we want to compare these newly formed matrices with the largest sub-matrix so far. The key is to make the peice of info **constant**. By constant it means we need constant number of look-ups to find out the newly formed matrices.

The answer is lying in the histogram problem. Now think about it,

### The algorithmic way of solving this
1. 