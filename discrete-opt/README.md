# Discrete Optimization

## Cliff Hanger
- iterative greedy for coloring
- for tsp, use two single linked lists to achieve O(1) state change

## Utils
There is a random graph generator in C which employs `srand48` and `drand48` that are only available in Unix like system.

Hyper-parameter tuning can be a problem for simulated annealing. Grid search or real valued interval search may be useful. May provide a module for that later.

## Literature
### Papers
In `reference-papers` there are

1. A-Branch-and-Bound-Algorithm-for-Knapsack-Problem. A paper from 60's
2. A-Fast-Compact-Approximation-of-the-Exponential-Function. Faster `exp()` C implementation.
3. Exploring-k-Colorable-Landscape-with-Iterated-Greedy. Best greedy algorithm I know for graph coloring.
4. VouTsa-Gls-MetaHeuristic2003. Detailed Guided Local Search and Fast Local Search in theory and practice.

## Online Resources

[fast pow() and exp() in C++ and Java][0]

---
[0]: https://martin.ankerl.com/2007/10/04/optimized-pow-approximation-for-java-and-c-c/
