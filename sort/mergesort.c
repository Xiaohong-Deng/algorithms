#include <stdio.h>

void Mergesort(int a[]) {

}

void Msort() {

}

void Merge() {

}

main(int argc, char *argv) {
  int unsort[];
  int count = 0;
  int N = 0;
  int temp;

  if (scanf("%d", N) == 1) {
    continue;
  } else {
    printf("illegal argument!\n");
    return;
  }
  unsort[N];
  while (scanf("%d", temp) == 1) {
    unsort[count++] = temp;
  }
  Mergesort(unsort);
  int i;
  for (i = 0; i < N; i++) {
    printf("%dth number is %d: \n", i, unsort[i]);
  }
}