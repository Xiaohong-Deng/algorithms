#ifndef LOCAL_SEARCH_H_
#define LOCAL_SEARCH_H_
#include <array>
#include <algorithm>
#include <cstring>
#include <ctime>
#include <fstream>
#include <H5Cpp.h>
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
using namespace H5;

float* load_dist_table(const size_t node_count, string file_name, string ds_name);
size_t parse_node_count(string file_name);

#endif
