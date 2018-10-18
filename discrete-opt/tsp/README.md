# Traveling Salesman Problem

![alt-text][51_vis]

## Problem Definition

![alt-text][problem_def]

## Paradigms
### Greedy

### Local Search

| objective function | features | cost | sub-neighborhood |
|--------------------|----------|------|------------------|
| length of tour | edges | edge length | 2-Opts that involves a certain node |

#### Guided Local Search

Use a penalization term in objective function to guide the local search. If search is stuck in a local minima, update the penalty for all features. Start the search for local minima again with the augmented objective, hoping that the penalty will guide the search to escape the local minima.

There is a formula called **utility** which evaluates the penalty of which feature should be updated. Update only one penalty each time, which is the feature that has the maximum utility.

#### Fast Local Search
activate sub-neighborhoods
> Moves will affect part of the solution directly or indirectly while leaving other parts unaffected. If a sub-neighborhood contains affected parts then it needs to be activated since an opportunity could be arise there for an improving move as a result of the original move performed.

I think the sub-neighborhoods contain the affected parts are the neighborhoods where the affected parts change or further changes involve the affected parts such that we get new configurations. This collection of new configurations is the sub-neighborhoods needed to be activated.

This is somehow a greedy heuristic. We get an improvement by going into the sub-neighborhoods contain the affected parts. So we assume we will have a better chance to get even better improvements by focusing on the parts we just changed (change them again).

#### Guided Fast Local Search

Another heuristic of activating sub-neighborhoods is the sub-neighborhoods the features being penalized is involved should be activated in the hope of removing the features.

#### Tips

- keep track of the differences of the augmented objective value for moves, not the augmented objective itself
- optionally, monitor actual objective value for moves not being accepted because moves are accepted based on augmented objective value. You have a small chance losing the new best value by doing that.
- If no improvements found in a sub-neighborhood, close it. Otherwise keep it open.
- No Parallelism. As metioned in the paper, GFLS is not sensitive to starting point solution. So restart doesn't do any good and randomness has no where else to add.

#### How to Compile and Run Cpp file
Unless you know how to deal with **HDF5** in C++, run the compile command as follows after making sure HDF5 is installed.

```
g++ -I/usr/include/hdf5/serial -Wdate-time -D_FORTIFY_SOURCE=2 -std=c++17 -g -O2 -fdebug-prefix-map=/build/hdf5-X9JKIg/hdf5-1.10.0-patch1+docs=. -fstack-protector-strong -Wformat -Werror=format-security local_search.cpp -L/usr/lib/x86_64-linux-gnu/hdf5/serial /usr/lib/x86_64-linux-gnu/hdf5/serial/libhdf5_hl_cpp.a /usr/lib/x86_64-linux-gnu/hdf5/serial/libhdf5_cpp.a /usr/lib/x86_64-linux-gnu/hdf5/serial/libhdf5_hl.a /usr/lib/x86_64-linux-gnu/hdf5/serial/libhdf5.a -Wl,-Bsymbolic-functions -Wl,-z,relro -lpthread -lsz -lz -ldl -lm -Wl,-rpath -Wl,/usr/lib/x86_64-linux-gnu/hdf5/serial -o ls.out
```

Make sure generate `dist_table` through `table_gen.py`

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

Some other ways work and some don't. I found that HDF5 worked. The 2d array is 9.1 GB. So is the `.h5` file. If you specify `dtype=float32` you can halve the size. Computing with `float` is faster than `double` in C++.

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

#### Python Cache Method Benchmark

Number of iterations is 1 million

| Cache Method | Time |
|--------------|------|
| No | 1.65s |
| Condensed Matrix | 2.65s |
| 2D Array | 0.45s |

Truncating `float64` or `float` to `float32` or `float16` worsens `sqrt`. Maybe it has something to do with how `math.sqrt` works. It converts them back?

### Local Search

#### Distance Table
I designed an experiment in C++ to test which is faster, `sqrt`, index computing or 2d array accessing. If I didn't design it wrong, this is the benchmarks I got. Note 2D array store each row in different places which incurs extra overhead while accessing.

With compiler optimization `-O2` on, number of iterations is 2 million each.

| Cache Method | Time |
|--------------|------|
| No | 0.01s |
| Condensed Matrix | 0.002s |
| 2D Array | 0.006s |

With compiler optimization off, number of iterations is the same.

| Cache Method | Time |
|--------------|------|
| No | 0.04s |
| Condensed Matrix | 0.24s |
| 2D Array | 0.24s |

Maybe condensed array is not bad in C++. Plus it's about 2.3 GB for 33810-node dataset.

Because HDF5 files are binary with metadata, I believe `H5Cpp.h` directly loads or `memcopy` HDF5 files from disk to memory. It's the fastest way and requires contiguous space in memory.

2-Opt is our neighbor move here. A 2-Opt results in a reverse sub-sequence of the tour or the other 2 segments of the tour (reverse them and change their positions), depending on which is shorter (note by reverse the shorter one we reduce the running time by half on average, but it's not the case for array like containers because reversing and swapping the prefix and suffix may incur shifting the middle segment). The sequence is directed, by reversing you change the direction of the sub-sequence which incurs a O(n) operation. This is true for linked list, array-like containers or double linked list.

Run on 1 core

| Algorithms | Dataset Size | Distance |Running Time | Number of Iterations | Cached Distance Table |
|------------|--------------|----------|--------------|----------------------|-----------------------|
| Guided Fast Local Search | 428.8718 | 51 | 0.01s | 600 | Condensed Matrix |
| Same | 20750.7625 | 100 | 0.026s | 1000 | Same |
| Same | 29453.9269 | 200 | 0.126s | 3000 | Same |
| Same | 319927.0843 | 1889 | 23.8s | 30000 | Same |

To get a decent result on 33810-node dataset it requires hours so I skipped it.

#### Can We Do Better
I don't know if O(1) state change is possible. But if it is, it would be as follows.

To achieve O(1) neighbor move (2-OPT), we keep 2 identical tour except that they have opposite directions. When swap we cut off the segment in the middle for both sequences. We swap the segment to the other sequence respectively. We connect the end points for 2 new pairs respectively. e.g., i to j and k to l turning to i to k and j to l.

Let's say we have a tour

1->2->3->4

4->3->2->1

By doing 2-Opt let's say we remove the edges like this

1 2->3 4

4 3->2 1

Swap the middle segment we have

1->3->2->4

4->2->3->1

Let's ponder on the question of if we need an `unordered_map` or `unordered_set` to track Nodes representing sub-neighborhood. You have to be aware of
1. All containers take in copies of data.
2. Use a reference to a container like a customized Node as a map key or set member is possible if you set up the hash function first.

We need to keep two identical Nodes so we can do the cut-off and swap thing. We need them to be together in the hash table.

It might be temping to use `unordered_map`  where node code name (0, 1, 2, ..., node_count - 1) is the key and a `tuple<Node*, Node*>` is the value. We cannot use reference to Node because reference can only be assigned once.

For each edge in a sub-neighborhood, we loop over the 2 tours simultaneously, one from the start, one form the end. This require the tours to be double linked lists.

## Pitfalls
### Precision

I encountered strange double precision problem. x = y + delta where delta is something like -9.7e-07. But `fabs(x-y)` is 0.0001+. I tried to reproduce it using simple double constants but failed. Maybe it has something to do with delta is computed with floats, not doubles. That is the case when I spotted the problem. Now I use doubles only.

As I repeat GFLS more, `max_util` tends to drop, and as a double you can find more than one `max_util` in one iteration. That is, `x == y`. Does it have anything to do with precision?

### Insert and Delete Elements in a Set While Looping Over It

This is legal in C++. It can be done correctly to reflect the behavior of GFLS. The behavior is easier to implement using array. But that would be a bit expensive to loop over.

```cpp
unordered_set<int> numbers = {1,2,3,4,5,6};
for (auto it = numbers.begin(); it != numbers.end();) {
  if (*it % 2 == 0) {
    it = numbers.erase(it); // return iterator to the element following the erased one
  } else {
    ++it;
  }
}
```

---
[51_vis]: ./51_vis.png
[problem_def]: ./problem_def.png
