# Graph Coloring
![alt text][50_vis]
## Problem Definition

![alt text][problem_def]

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

Greedy implementations were run on a i5-3470 CPU and Windows 10.

Local Search implementations were run on Ubuntu 18.04.

### Greedy

1 cores.

On `data/gc_500_1`

| Algorithm | Running Time | Number of Colors | Number of Iterations | Node Sorted in Degree |
|-----------|--------------|------------------|----------------------|-----------------------|
| naive greedy | milliseconds | 20 | 1 | No |
| naive greedy | milliseconds | 18 | 1 | Descending |
| random greedy | seconds | steady 18 | 2000 | No |
| random greedy with color | seconds | steady 17 | 1000 | Descending within each color group |
| random greedy with color alternative | seconds | 16 with over 60% chance | 3000 | Descending within each color group |

On `data/gc_1000_5`

| Algorithm | Running Time | Number of Colors | Number of Iterations | Node Sorted in Degree |
|-----------|--------------|------------------|----------------------|-----------------------|
| random greedy with color alternative | 60s | 110 to 112 | 3000 | Descending within each color group |

`random_greedy_with_color_alternative` differs from `random_greedy_with_color` in that it iterates over the solution generated in the previous iteration, whereas `random_greedy_with_color` iterates over the global best each time.

Later running on 4 cores with the same `num_iter` produced more stable results.

### Local Search

4 cores and compiled with `-O2`.

On `data/gc_500_1`

| Algorithm | Running Time | Number of Colors | Number of Iterations | Starting Solution | Neighbor Searching | Neighbor Choosing | Restart Heuristic |
|-----------|--------------|------------------|----------------------|-------------------|--------------------|-------------------|---------|
| Local Search | miliseconds | 20 | 1 | non-iterated random greedy | random pick node | first legal neighbor | no move for 500 neighbors |
| same as above | 0.5s | 18 | 400 | same as above | same as above | same as above | same as above |

On `data/gc_1000_5`

| Algorithm | Running Time | Number of Colors | Number of Iterations | Starting Solution | Neighbor Searching | Neighbor Choosing | Restart Heuristic |
|-----------|--------------|------------------|----------------------|-------------------|--------------------|-------------------|---------|
| Local Search | 3s | 111 | 50 | non-iterated random greedy | random pick node | first legal neighbor | no move for 500 neighbors |
| same as above | 47s | 110 | 800 | same as above | same as above | same as above | same as above |
| Simulated Annealing | 7s | 101 or 100 | 1 | saa | random pick node and color | take worse neighbor with prob. | temperature drop below 1.0 |
| Simulated Annealing | 1750s | 101 or 100 | 200 | saa | random pick node and color | take worse neighbor with prob. | temperature drop below 1.0 |


#### Local Search

Vanilla local search is greedy.

Neighbor searching strategy is as follows:

1. Randomly pick a node

2. Iterate over all colors in the initial solution, search for the first legal one.

3. If no color is legal for the node move to the next random node

4. If no move for n random node, abort searching and restart.

#### Simulated Annealing

Pure simulated annealing may not be the most performant or optimal approach. People got better and faster solutions with tabu search or kemp chain. Mix all of them is also good.

Reheating: No

Cooling: Drop temperature fast in early stage. Drop slower when lower. Need careful tuning.

On my CPU I can do approx. 60 million neighbor searches within 7 seconds. There is a trade-off between restart times and neighbor searching times in each restart. Either you do more restarts and fewer neighbor searches or the other way around by tuning the parameters. There is also a similar trade-off between number of searches at a higher temperature and number of searches at a lower temperature.

For the set of hyper-parameters I tuned, 200 iterations doesn't bring convergence to 100 colors. In each iteration, it makes about 57000+ neighbor moves. Maybe I need to make some kind of trade-off.

One crucial thing is neighbor searching strategy. I tried several but present two here. One is bad at performance the other is good.

1. Randomly pick a node and randomly pick a color for this node. If accept the neighbor then move on to the next node. If the neighbor is not accepted, randomly pick another color for this node again for n times. n is a hyper-parameter.

2. Randomly pick a node and randomly pick a color. Move on to the next node no matter the pair of node and color is accepted. This is a more random strategy than the previous one.

Update the temperature only after a move.

Good hyper-parameters are dataset specific. Tuning is time consuming.

---
[50_vis]: ./50_vis.jpg
[problem_def]: ./problem_def.jpg
