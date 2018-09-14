#ifndef LOCAL_SEARCH_H_
#define LOCAL_SEARCH_H_
#include <array>
#include <algorithm>
#include <ctime>
#include <fstream>
#include <iostream>
#include <iterator>
#include <map>
#include <math.h>
#include <random>
#include <string>
#include <sstream>
#include <tuple>
#include <unordered_set>
#include <vector>

using namespace std;

tuple<size_t, size_t, tuple<int, int>*> parse_data(string file_name);
// tuple is mutable in cpp
map<int, vector<int>> node_to_neighbors(size_t edge_count, const tuple<int, int> edges[]);

int* naive_greedy(size_t node_count, const tuple<int, int> edges[], const int nodes[], map<int, vector<int>> neighbors);
// array always passed as pointer, and size info is lost so passed with size
tuple<size_t, int*> random_greedy(size_t node_count, size_t edge_count, const tuple<int, int> edges[], map<int, vector<int>> neighbors, size_t num_iter=2000);
// default num_iter should be defined here not .cpp otherwise other .cpp file won't be able to see it
tuple<size_t, int*> local_search(size_t node_count, size_t edge_count, const tuple<int, int> edges[], size_t num_iter=100, int threshold=500);

#endif
