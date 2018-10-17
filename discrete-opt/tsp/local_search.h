#ifndef LOCAL_SEARCH_H_
#define LOCAL_SEARCH_H_
#include <array>
#include <algorithm>
#include <assert.h>
#include <cstring>
#include <ctime>
#include <fstream>
#include <H5Cpp.h>
#include <iomanip>
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

using namespace H5;
using namespace std;

double* load_dist_table(const size_t node_count, string file_name, string ds_name);
size_t parse_node_count(string file_name);
tuple<double, size_t*> guided_fast_local_search(const size_t node_count, const double dist_table[], const size_t num_iter=1000);

#endif
