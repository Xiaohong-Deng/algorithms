# 0-1 Knapsack

## Problem Definition
![alt text][problem_def]

## Paradigms

### Dynamic Programming

**Step 1**: Formulate **recurrence** based on a structure of an optimal solution.

Let S = max-value solution to the problem. S is a subset of all the items {0, 1, ..., n}

Case 1: item n is not in S. S must be the optimal solution to the subproblem: with the first n-1 items and capacity W

Case 2: item n is in S. S-{n} must be the optimal solution to the subproblem: with the first n-1 items and capacity W-Wn

We have 2 subproblems here. We also have built connections between optimal solution S and optimal solutions to the 2 subproblems.

For first i items and capacity W, we have solution V

V(i, W) = max{V(i-1, W), Vi + V(i-1, W-Wi)} and if Wi > W, V(i, W) = V(i-1, W)

**Step 2**: Identify subproblems

- All possible prefixes of items {0, 1, ..., n}
- All possible integral residual capacities x in {0, 1, ..., W}

For prefix {0, 1, ..., n-2} we need residual capacities {W-Wn, W-W(n-1), W-Wn-W(n-1), W} to form 4 subproblems. We don't need all possible residual capacities for each prefix (W+1 subproblems for each prefix).

As a top-down recursive approach, we can call `solve(itemSize=n, packSize=W)`. It'll check a look-up table for solutions to the two subproblems. If it's a miss, recursively call `solve()` with subset of items and capacities and store the return values to the look-up table.

If we go for a bottom-up approach, we need to compute a sum of combinations. For example, when prefix is {0}, we need all possible `a1*W1 + a2*W2 + ... + an*Wn` where ai is in {0, 1}. The cost of computing the combinations may be significant. Maybe we are better off with computing a solution for each x in {0, 1, ..., W} with a specific prefix.

### Branch and Bound

Branch and bound is also a paradigm looking for subproblems. The set of all subproblems can be viewed as a binary tree (so is the DP approach).

**Branch**: create two subproblems by assuming if we put the current i-th item to the pack or not. We are not looking for best solutions here. We are considering all possible combinations of n items if no pruning ever happens.

**Bound**: find optimistic estimations of the subproblems. The estimations include

1. lower bound which is the total item value in pack. This is the partial solution to the subproblem.
2. upper bound which is the largest value possible for current settings in the subproblem

We also maintain a global lower bound that is one of the folloing values
1. the best local lower bound in all the nodes explored.
2. the optimal in-pack value we have confirm thourgh other means like applying an inferior but faster algorithm. The result produced may beat the best local lower bound so far.

The goal is pruning the nodes so running time is reduced. If node is not pruned and we reach the leaf, we have a complete solution to the problem. That is a specific subset of n items and the sum of their values.

The key to this approach is trying to increase the global lower bound and decrease the local upper bound.

- If the global lower bound is greater or equal to the local upper bound, prune the node.
- If the item being considered is larger than the space remained, prune the node.
- As long as you have space and items remained, your local upper bound must be larger than your local lower bound. That is the reason why we don't check equality between local lower bound and local upper bound. Instead we check if we reach the leaf (items out) or `space <= 0`.

Some optimization procedures can be applied to this algorithm but a basic version can be pictured as follows

Green: local lower bound; Red: space remained; Purple: local upper bound

In the image the local upper bound is all the values of items that haven't been considered plus the values of items in pack.

![alt text][bb_vis]

#### Implementation

I chose to implement the algorithm with a binary tree where each node in the tree represents a subproblem. Local lower and upper bounds are stored in the nodes with some other variables. The tree structure and the look-up table used in DP are interchangable in the two algorithms. The nodes can be viewed as entries (keys) in the table and the variables attached are the values.

The stack in the main loop enables the program to process the nodes in a depth-first order. We can also use recursion to achieve that.

##### Relaxation

Coming soon.

##### Other Tricks

Coming soon.

---
[problem_def]: ./problem_def.jpg
[bb_vis]: ./bb_vis.jpg
