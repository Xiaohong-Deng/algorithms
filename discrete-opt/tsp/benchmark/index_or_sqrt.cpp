#include <array>
#include <algorithm>
#include <cstring>
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

float euclid_dist(float x1, float x2, float y1, float y2) {
  int diff_x = x1 - x2;
  int diff_y = y1 - y2;
  return sqrt(diff_x * diff_x + diff_y * diff_y);
}

unsigned int condensed_index(unsigned int i, unsigned int j, unsigned int n) {
  if (i < j) {
    unsigned int temp = j;
    j = i;
    i = temp;
  }

  return n * j - ((j * (j + 1)) >> 1) + i - 1 - j;
}

int main(int argc, char const *argv[]) {
  random_device ran_dev;
  mt19937 generator(ran_dev());
  uniform_int_distribution<unsigned int> distr(0, 33809);
  uniform_real_distribution<float> distr_real(0.0, 3000.0);

  // segment faults if count is too large. remove one of the tests helps
  size_t count = 2000000;

  float** dist_table = new float*[33810];
  for (size_t i = 0; i < 33810; i++) {
    dist_table[i] = new float[33810];
    for (size_t j = 0; j < 33810; j++) {
      dist_table[i][j] = distr_real(generator);
    }
  }

  unsigned int len_1d = 33810 * 33810;
  float* table_1d = new float[len_1d];
  for (size_t i = 0; i < len_1d; i++) {
    table_1d[i] = distr_real(generator);
  }
  // uniform_int_distribution<size_t> distr_index(0, count - 1);
  // double point_pairs[count][4];
  // for (size_t i = 0; i < count; i++) {
  //   point_pairs[i][0] = distr_real(generator);
  //   point_pairs[i][1] = distr_real(generator);
  //   point_pairs[i][2] = distr_real(generator);
  //   point_pairs[i][3] = distr_real(generator);
  // }
  clock_t begin = clock();
  for (size_t j = 0; j < count; j++) {
    // size_t idx = distr_index(generator);
    float a = distr_real(generator);
    float b = distr_real(generator);
    float c = distr_real(generator);
    float d = distr_real(generator);
    // float x = euclid_dist(a, b, c, d);
  }
  clock_t end = clock();
  double elapsed = double(end - begin) / CLOCKS_PER_SEC;

  cout << "time passed for euclid_dist(): " << elapsed << endl;

  // unsigned int index_pairs[count][2];
  // cout << "size of index_pairs: " << (sizeof(index_pairs) / sizeof(*index_pairs)) << endl;
  // for (size_t i = 0; i < count; i++) {
  //   index_pairs[i][0] = distr(generator);
  //   index_pairs[i][1] = distr(generator);
  // }

  begin = clock();
  for (size_t j = 0; j < count; j++) {
    // size_t idx = distr_index(generator);
    // unsigned int x = condensed_index(index_pairs[idx][0], index_pairs[idx][1], 33810);
    unsigned int a = distr(generator);
    unsigned int b = distr(generator);
    // unsigned int x = condensed_index(a, b, 33810);
    // float y = table_1d[x];
  }

  end = clock();
  elapsed = double(end - begin) / CLOCKS_PER_SEC;

  cout << "time passed for condensed_index(): " << elapsed << endl;

  begin = clock();
  for (size_t i = 0; i < count; i++) {
    unsigned int a = distr(generator);
    unsigned int b = distr(generator);
    // unsigned int x = a * 33810 + b;
    // float y = table_1d[x];
  }
  end = clock();
  elapsed = double(end - begin) / CLOCKS_PER_SEC;

  cout << "time passed for 1d array simulating 2d: " << elapsed << endl;

  begin = clock();
  for (size_t i = 0; i < count; i++) {
    unsigned int a = distr(generator);
    unsigned int b = distr(generator);
    // float x = dist_table[a][b];
  }
  end = clock();
  elapsed = double(end - begin) / CLOCKS_PER_SEC;

  cout << "time passed for 2d array accessing: " << elapsed << endl;

  delete[] dist_table;
  delete[] table_1d;
  return 0;
}
