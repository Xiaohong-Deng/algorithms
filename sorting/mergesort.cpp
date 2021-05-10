#include "mergesort.h"
#include "utils.h"

void msort(int *arr, int count) {
  if (count > 1) {
    int left_count = count / 2;
    int right_count = count - left_count;
    msort(arr, left_count);
    msort(arr + left_count, right_count);
    merge(arr, arr + left_count, arr, left_count, right_count, count);
  }
}

void merge(int *a, int *b, int *arr, int left_count, int right_count, int count) {
  int aux[count];
  int idx_a, idx_b, i;
  idx_a = idx_b = 0;
  for (i = 0; i < count; i++) {
    if (idx_a < left_count && idx_b < right_count) {
      if (a[idx_a] < b[idx_b]) {
        aux[i] = a[idx_a++];
      } else {
        aux[i] = b[idx_b++];
      }
    } else if (idx_a < left_count) {
      aux[i] = a[idx_a++];
    } else if (idx_b < right_count) {
      aux[i] = b[idx_b++];
    }
  }
  memcpy(arr, aux, sizeof(aux));
}

int main(int argc, char const *argv[]) {
  if (argc > 1) {
    string fn = argv[1];
    int count;
    int *int_arr;

    tie(count, int_arr) = parse_data(fn);

    msort(int_arr, count);
    validate(int_arr, count);
    delete[] int_arr;
  } else {
    std::cout << "Please provide the file containing unsorted list" << '\n';
  }
  return 0;
}
