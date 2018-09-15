#ifndef LOCAL_SEARCH_H_
#define LOCAL_SEARCH_H_
#include <array>
#include <algorithm>
#include <ctime>
#include <fstream>
#include <iostream>
#include <iterator>
#include <math.h>
#include <random>
#include <string>
#include <sstream>
#include <tuple>
#include <unordered_map>
#include <unordered_set>
#include <vector>

using namespace std;

tuple<size_t, size_t, tuple<int, int>*> parse_data(string file_name);
// tuple is mutable in cpp
unordered_map<int, vector<int>> node_to_neighbors(size_t node_count, size_t edge_count, const tuple<int, int> edges[]);

int* naive_greedy(size_t node_count, const int nodes[], unordered_map<int, vector<int>> neighbors);
// array always passed as pointer, and size info is lost so passed with size
tuple<size_t, int*> random_greedy(size_t node_count, size_t edge_count, unordered_map<int, vector<int>> neighbors, size_t num_iter=100);
tuple<size_t, int*> local_search(size_t node_count, size_t edge_count, unordered_map<int, vector<int>> neighbors, int threshold,
                                 mt19937 gen, uniform_int_distribution<int> distr);
tuple<size_t, int*> sim_annealing(size_t node_count, size_t edge_count, unordered_map<int, vector<int>> neighbors,
                                  mt19937 generator, uniform_int_distribution<int> distribution, uniform_real_distribution<double> acceptance_distr,
                                  double threshold=1.0, double temperature=10.0, double alpha=0.999, double alpha_rate=1.00045);
// default num_iter should be defined here not .cpp otherwise other .cpp file won't be able to see it
tuple<size_t, int*> iterated_local_search(size_t node_count, size_t edge_count, const tuple<int, int> edges[], int mode=0, size_t num_iter=200, int threshold=300);

#endif
