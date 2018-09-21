# Traveling Salesman Problem

## Performance

i5-3470 CPU, Ubuntu 18.04, run on all 4 cores

### Greedy

| Algorithms | Dataset Size | Running Time | Distance | Number of Iterations | Cached Distance Table |
|------------|--------------|--------------|----------|----------------------|-----------------------|
| Random Greedy | 1889 | 180s | 363249.59 | 200 | No |
| Random Greedy | 1889 | 179s to 191s | 363249.59 | 200 | Condensed Matrix |
| Random Greedy | 1889 | 92.04 | 360758.29 | 200 | Cache on the fly |
| Random Greedy | 1889 | 81.53s(building csv) | 367062.75 | 200 | Matrix |
| Random Greedy | 1889 | 75.01s(reading csv) | 362672.90 | 200 | Matrix |
| Same as Above | 33810 | 1532s | 77886319.94 | 4 | No |
| Same as Above | 33810 | Mem Exploded | - | 4 | Matrix |
| Same as Above | 33810 | 876.92s | 77401494.30 | 4 | Cache on the fly |

Do you notice that condensed matrix caching for 1889 nodes dataset is no better in running time than no caching. I did more detailed tests on that below in another table. That proves that integer multiplication and addition is not cheaper than float `sqrt` or `**2`, at least not much.

To achieve O(1) insertion when inserting a node to the tour, I choose to operate on nodes of a linked list. Built-in List and Deque don't have the property. Insertion is O(n) for them.

Small integer multiplication in Python is optimized such that shift is no good in that. But small integer division is not so do the right shift.

#### Memory & Speed Trade-Off

The major cost of the algorithm is on `sqrt` and `**2`. For Python the least we can do is building a look-up table to cache the distance.

When considering a single insertion probing, we need to compute distance of uv, ui and vi where i is the node to be inserted. uv was looked up in node (i-1) insertion. vi should be stored when considering vw, vi and iw. So look-up table can save 2/3 of the time when no caching is applied.

I tried self-defined look-up table building function and `scipy.spatial.distance.cdist` and `scipy.spatial.distance.pdist`. `pdist` is 50 times faster than self-defined function. `pdist` is a bit faster than `cdist` but uses a condensed matrix where you need to compute index each time before accessing elements. `squareform` it can resolve the problem but the overhead weighs it down such that `cdist` becomes the best.

One should generate a distance look-up table and store it in disk as a file for future use.

On the other hand for the 33810 node dataset things are interesting. Parallel computing just paralyzed. 3 cores were idle. Look-up table just doesn't work.

The symptom was when running on 4 cores RAM consumed was 11 GB. When running on 1 core RAM consumed was 16 GB. 16 GB is all I got. So I think in the 4 cores case the idle cores were just not able to load the data to RAM!!!

#### Other Tries to Memory & Speed Trade-Off

Surprisingly condensed array doesn't bring faster run! So I reduced `num_iter` and run on 1 core to get more precise results.

run on 1 core

| Algorithms | Dataset Size | Running Time | Number of Iterations | Cached Distance Table |
|------------|--------------|--------------|----------------------|-----------------------|
| Random Greedy | 1889 | 8.22s | 5 | Cache on the fly |
| Random Greedy | 1889 | 14s to 15s | 5 | Condensed Matrix |
| Random Greedy | 1889 | 17s to 19s | 5 | No |
| Random Greedy | 1889 | 7.09s(building csv) | 5 | Matrix |
| Random Greedy | 1889 | 6.55s(reading csv) | 5 | Matrix |

condensed matrix as `pdist`

cache on the fly

multithreading instead of multiprocessing so they share the `dist_table`


### Local Search

---
