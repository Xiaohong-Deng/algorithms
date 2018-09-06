# Graph Coloring

## Problem Definition

## Paradigms
### Greedy

#### Performance
On datasets of different sizes **random_greedy** achieves better results than **naive_greedy** with nodes sorted in descending order almost every time. `num_iter` has a slight and positive impact on the result when it is increased from 100 to 500, from 500 to 1000. But the impact is just the outcome is more steady, not improved. i.e., the worse solution is less likely to occur when `num_iter` is increased.

**random_greedy_with_color** slightly improved the results for some datasets, e.g., 17 colors instead of 18 (`random_greedy()`)  for a 500 vertex dataset with `num_iter = 1000` for both algorithms. 16 colors can be achieved when `num_iter` is increased to 1500 but can not be stablized even when it reaches 7000. Based on
how it is implemented it converges pretty early which means during most of the iterations nothing changes except the order of colors.

**random_greedy_with_color_alternative** always iterates over the solution generated in the previous iteration. Suprisingly it still converges very fast. Despite the fact we are using potentially different solutions every time, the number of colors is almost the same. But this time with `num_iter = 3000` 16 colors for the 500 vertex dataset used for `random_greedy_with_color` can be stablized. Similar effect happens to datasets of other sizes.
