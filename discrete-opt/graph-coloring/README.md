# Graph Coloring

## Problem Definition

## Paradigms
### Greedy

#### Performance
On datasets of different sizes **random_greedy** achieves better result than **naive_greedy** with nodes sorted in descending order almost every time. `num_shuffle` has a slight and positive impact on the result when it is increased from 100 to 500, from 500 to 1000. But the impact is just the outcome is more steady, not improved. i.e., the worse solution is less likely to occur when `num_shuffle` is increased.
