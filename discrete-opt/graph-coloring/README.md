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

Randomness: gcc/MinGW has `random_device` deterministic behaviors on Windows. Use Boost or change platform.

Now C++ implementation running time on `node_to_neighbors` and `naive_greedy` is twice slower than python. Need to change data structures

If move data structures used in python to c++ directly, performance drops. Try array as much as possible. Try `unordered_map` which is hash table whereas `map` is BST.

Use -O2

squeeze performance as much as possible from the objective function computation.

## Performance
### Greedy

On `data/gc_500_1`

| Algorithm | Running Time | Number of Colors | Number of Iterations | Node Sorted in Degree |
|-----------|--------------|------------------|----------------------|-----------------------|
| naive greedy | milliseconds | 20 | 1 | No |
| naive greedy | milliseconds | 18 | 1 | Descending |
| random greedy | seconds | steady 18 | 2000 | No |
| random greedy with color | seconds | steady 17 | 1000 | Descending within each color group |
| random greedy with color alternative | seconds | 16 with over 60% chance | 3000 | Descending within each color group |

`random_greedy_with_color_alternative` differs from `random_greedy_with_color` in that it iterates over the solution generated in the previous iteration, whereas `random_greedy_with_color` iterates over the global best each time.

### Local Search

On `data/gc_500_1`. 1 cores and compiled with default setting.

| Algorithm | Running Time | Number of Colors | Number of Iterations | Starting Solution | Neighbor Searching | Neighbor Choosing | Restart Heuristic |
|-----------|--------------|------------------|----------------------|-------------------|--------------------|-------------------|---------|
| Local Search | seconds | 17 | 1 | non-iterated random greedy | random pick | first legal neighbor | no move for 500 neighbors |
| same as above | seconds | 16 | 100 | same as above | same as above | same as above | same as above |

On `data/gc_1000_5`. 4 cores and compiled with `-O2`.

| Algorithm | Running Time | Number of Colors | Number of Iterations | Starting Solution | Neighbor Searching | Neighbor Choosing | Restart Heuristic |
|-----------|--------------|------------------|----------------------|-------------------|--------------------|-------------------|---------|
| Local Search | 3s | 111 | 50 | non-iterated random greedy | random pick | first legal neighbor | no move for 500 neighbors |
| same as above | 47s | 110 | 800 | same as above | same as above | same as above | same as above |

Vanilla local search is greedy.

---
[50_vis]: ./50_vis.jpg
