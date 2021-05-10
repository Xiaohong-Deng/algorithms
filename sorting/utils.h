#ifndef UTILS_H_
#define UTILS_H_
#include <array>
#include <cstring>
#include <fstream>
#include <iostream>
#include <iterator>
#include <math.h>
#include <string>
#include <sstream>
#include <tuple>
#include <vector>
#include <random>

using namespace std;

tuple<int, int *> parse_data(string file_name);
void validate(int *arr, int count);

// the point is to generate a reusable random int generator
// maybe can replace it with a closure
class Randint
{
private:
  default_random_engine rng;
public:
  int operator()(int start, int end);
  Randint(unsigned int seed=random_device{}());
  ~Randint ();
};

#endif