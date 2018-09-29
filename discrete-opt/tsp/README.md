# Traveling Salesman Problem

## Paradigms
### Greedy

### Local Search
#### Guided Local Search

#### Fast Local Search
activate sub-neighborhoods
> Moves will affect part of the solution directly or indirectly while leaving other parts unaffected. If a sub-neighborhood contains affected parts then it needs to be activated since an opportunity could be arise there for an improving move as a result of the original move performed.

I think the sub-neighborhoods contain the affected parts are the neighborhoods where the affected parts change or further changes involve the affected parts such that we get new configurations. This collection of new configurations is the sub-neighborhoods needed to be activated.

This is somehow a greedy heuristic. We get an improvement by going into the sub-neighborhoods contain the affected parts. So we assume we will have a better chance to get even better improvements by focusing on the parts we just changed (change them again).

#### Guided Fast Local Search

Another heuristic of activating sub-neighborhoods is the sub-neighborhoods the features being penalized is involved should be activated in the hope of removing the features.

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

#### Large Datasets

When dealing with a 33810-node dataset, my 16 GB memory exploded with 2d numpy array. First `np.genfromtext` takes over 7 times more memory to load a file. So is `np.loadtxt`. `np.savetxt` doesn't correctly save the complete data to file. It truncates the data.

Some other ways work and some don't. I found that HDF5 works. The 2d array is 9.1 GB. So is the `.h5` file. If you specify `dtype=float32` you can halve the size.

#### Other Tries to Memory & Speed Trade-Off

Surprisingly condensed matrix doesn't bring faster run! So I reduced `num_iter` and run on 1 core to get more precise results.

run on 1 core

| Algorithms | Dataset Size | Running Time | Number of Iterations | Cached Distance Table |
|------------|--------------|--------------|----------------------|-----------------------|
| Random Greedy | 1889 | 8.22s | 5 | Cache on the fly |
| Random Greedy | 1889 | 14s to 15s | 5 | Condensed Matrix |
| Random Greedy | 1889 | 17s to 19s | 5 | No |
| Random Greedy | 1889 | 7.09s(building csv) | 5 | Matrix |
| Random Greedy | 1889 | 6.55s(reading csv) | 5 | Matrix |

Hash table, although computing hash value may be no cheaper than computing index. It take more space.

2d array where inner arrays have different sizes. Only two or three arithmetic operations fewer.

multithreading instead of multiprocessing so they share the `dist_table`


### Local Search
#### Distance Table
I designed an experiment in C++ to test which is faster, `sqrt`, index computing or 2d array accessing. If I didn't design it wrong, this is the benchmarks I got.

With compiler optimization `-O2` on, loop over the operations for 2 million times each.

| Cache Method | Time |
|--------------|------|
| No | 0.01s |
| Condensed Matrix | 0.002s |
| 2D Array | 0.006s |

With compiler optimization off, loop over for the same number of times.

| Cache Method | Time |
|--------------|------|
| No | 0.04s |
| Condensed Matrix | 0.24s |
| 2D Array | 0.24s |

Maybe condensed array is not bad in C++. Plus it's about 2.3 GB for 33810-node dataset.

Because HDF5 files are binary with metadata, I believe `H5Cpp.h` directly loads or `memcopy` HDF5 files from disk to memory. It's the fastest way and requires contiguous space in memory.

2-Opt is our neighbor move here. A 2-Opt results in a reverse sub-sequence of the tour or the other 2 segments of the tour (reverse them and change there positions), depending on which is shorter. The sequence is directed, by reversing you change the direction of the sub-sequence which incurs a O(n) operation. This is true for linked list or double linked list.

To achieve O(1) neighbor move (2-OPT), we keep 2 identical tour except that they have opposite directions. When swap we cut off the segment in the middle for both sequences. We swap the segment to the other sequence respectively. We connect the end points for 2 new pairs respectively. e.g., i to j and k to l turning to i to k and j to l.

---
