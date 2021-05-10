#include "utils.h"
#include "quicksort.h"

void qsort(int *arr, int count, Randint randintgen) {
  if (count > 1) {
    int pivot = randintgen(0, count - 1);
    int temp = arr[0];
    arr[0] = arr[pivot];
    arr[pivot] = temp;
    int i = sweepswap(arr, count);
    qsort(arr, i, randintgen);
    qsort(arr + i + 1, count - i - 1, randintgen);
  }
}

int sweepswap(int *arr, int count) {
  int i, j, temp;
  i = 1;
  for (j = 1; j < count; j++) {
    if (arr[j] <= arr[0]) {
      if (i != j) {
        temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;
      }
      i++;
    }
  }
  temp = arr[i - 1];
  arr[i - 1] = arr[0];
  arr[0] = temp;
  return i - 1;
}

int main(int argc, char const *argv[]) {
  if (argc > 1) {
    string fn = argv[1];
    int *int_arr;
    int count;

    tie(count, int_arr) = parse_data(fn);
    Randint randintgen;
    qsort(int_arr, count, randintgen);
    validate(int_arr, count);
    delete[] int_arr;
  } else {
    std::cout << "Please provide the file containing unsorted list" << '\n';
  }
  return 0;
}