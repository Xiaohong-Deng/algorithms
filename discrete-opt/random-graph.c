#include <stdio.h>
#include <stdlib.h>

#define MAXNODES 100

/* randomgraph.g
   Usage: greedy n m
          greedy n m seed

   generate a random graph of n nodes and m vertice C.C. McGeoch 2011
   Output format:
    n     #number of vertices
    2 4 0    # neighbors of v1, ending in 0
    1 3 0    # neighbors of v2, ending in 0
    2 0      # neighbors of v3, ending in 0
    1 0      # neighbors of v4, ending in 0

    comments after last adjacency line.

*/

/*-----------------------------------------------------------*/
main(argc, argv)
int argc;
char *argv[];

{
  int nodes[MAXNODES+1][MAXNODES+1];
  int n;
  int m;
  int src,dst;
  int i, j;

  if ((argc != 4) && (argc != 3)) {
    printf("usage: randomgraph n m  (or)\n  randomgraph n m seed");
    exit(1);
  }

  n = atoi (argv[1]);
  m = atoi (argv[2]);

  if (n > MAXNODES) {
    printf("to many vertices, max is %d. Recompile and try again \n",n);
    exit(1);
  }
  if (m > n*(n-1)/2 ){
    printf("too many edges for this n. Max is %d \n", n*(n-1)/2 );
    exit(1);
  }

  if (argc != 4)
    srand48((long) time(0) );
  else
    srand48((long) atoi (argv[3]));

  for (i = 1; i < n; i++)
    for (j=1; j <n; j++) nodes[i][j]=0;

  double k = (double) m;    /* sample size to select */
  double r = n * (n-1) / 2; /* range of elements considered is 1..r */
  double s = 0;             /* selected so far */
  double c = 0;             /* considered so far */
  double prob;

  for (src = 1; src <= n; src++) {  /* for each source */
    for (dst = 1; dst< src; dst++) { /* for each (s,d) with d < s */
      prob = (k - s) / (r - c); /* probability of selecting this one */
      double p = drand48();

      if (p  < prob ) { /* random selection with probability prob*/
        nodes[src][dst] = 1;
        nodes[dst][src] = 1;
	//  printf("select %d %d\n", src, dst);
	s++;
      }
      c++;
    }//d
  }//s

  printf("%d\n", n);
  for (src = 1; src<= n; src++) {
    for (dst=1; dst<= n; dst++)
      if (nodes[src][dst] == 1) printf("%d\t", dst);
    printf("0\n"); //end
  }

  printf("\n\ncomments:\n");
  printf("random graph\n");
  printf("nodes %d\n", n);
  printf("edges %d\n", m);
  if (argc==4) printf("rng seed %d \n", atoi(argv[3]));

}//main
