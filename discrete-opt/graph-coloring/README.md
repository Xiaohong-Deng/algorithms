# Graph Coloring
![alt text][50_vis]
## Problem Definition

## Paradigms
### Greedy

### Local Search
Neighbor definition: change the color of a node.

How to pick the node to change color? pick a color class with fewest nodes. Then pick a node with fewest degree?

How to pick the new color? color with most nodes first? or just iterate from color 0 to num_colors. Not that many colors after all.

Or we just randomly pick a node?

Randomness: gcc has no `random_device` deterministic behaviors on Windows. Boost has

## Performance
### Greedy

Performance on `data/gc_500_1`

| Algorithm | Running Time | Number of Colors | Number of Iterations | Node Sorted in Degree |
|-----------|--------------|------------------|----------------------|-----------------------|
| naive greedy | milliseconds | 20 | 1 | No |
| naive greedy | milliseconds | 18 | 1 | Descending |
| random greedy | seconds | steady 18 | 2000 | No |
| random greedy with color | seconds | steady 17 | 1000 | Descending on color groups |
| random greedy with color alternative | seconds | 16 with over 60% chance | 3000 | Descending on color groups |

On datasets of different sizes **random_greedy** achieves better results than **naive_greedy** with nodes sorted in descending order almost every time. `num_iter` has a slight and positive impact on the result when it is increased from 100 to 500, from 500 to 1000. But the impact is just the outcome is more steady, not improved. i.e., the worse solution is less likely to occur when `num_iter` is increased.

**random_greedy_with_color** slightly improved the results for some datasets, e.g., 17 colors instead of 18 (`random_greedy()`)  for `gc_500_1` with `num_iter = 1000` for both algorithms. 16 colors can be achieved when `num_iter` is increased to 1500 but can not be stabilized even when it reaches 7000. Based on
how it is implemented it converges pretty early which means during most of the iterations nothing changes except the order of colors.

**random_greedy_with_color_alternative** always iterates over the solution generated in the previous iteration. Surprisingly it still converges very fast. Despite the fact we are using potentially different solutions every time, the number of colors is almost the same. But this time with `num_iter = 3000` 16 colors for the `gc_500_1` can be stabilized. Similar effect happens to datasets of other sizes.

### Local Search

Performance on `data/gc_500_1`

| Algorithm | Running Time | Number of Colors | Number of Iterations | Starting Solution | Neighbor Searching | Neighbor Choosing | Restart Heuristic |
|-----------|--------------|------------------|----------------------|-------------------|--------------------|-------------------|---------|
| Local Search | seconds | 17 | 1 | non-iterated random greedy | random pick | first legal neighbor | no move for n neighbors |
| same as above | seconds | 16 | 100 | same as above | same as above | same as above | same as above |

---
[50_vis]: ./50_vis.jpg
