#include "utils.h"

tuple<int, int *> parse_data(string file_name) {
  ifstream infile(file_name);
  vector<int> int_vec(
    istream_iterator<int>(infile), 
    {}
    );
  int *int_arr = int_vec.data(); // points to internal array of the vector
  int count = int_vec.size();
  int *arr_cpy = new int[count];
  memcpy(arr_cpy, int_arr, count* sizeof(int));
  return tie(count, arr_cpy);
}

void validate(int *arr, int count) {
  int error = 0;
  for (int i = 0; i < count - 1; i++) {
    error |= arr[i] > arr[i + 1];
  }
  if (error) {
    cout << "Not sorted!" << endl;
  }
}

Randint::Randint (unsigned int seed):rng(seed)
{
}

Randint::~Randint()
{
}

int Randint::operator()(int start, int end){
  uniform_int_distribution<int> distr(start, end);
  return distr(rng);
}