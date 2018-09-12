#include <array>
#include <algorithm>
#include <fstream>
#include <iostream>
#include <iterator>
#include <math.h>
#include <string>
#include <sstream>
#include <tuple>
#include <vector>

using namespace std;

tuple<int, int, int*, int*> parse_data(string file_name) {
  int node_count;
  int edge_count;
  int* edges_v1;
  int* edges_v2;

  ifstream infile(file_name);
  string line;
  getline(infile, line);
  istringstream iss(line);

  if (!(iss >> node_count >> edge_count)) {
    cout << "empty file!" << endl;
    return make_tuple(node_count, edge_count, edges_v1, edges_v2);
  }

  edges_v1 = new int[edge_count];
  edges_v2 = new int[edge_count];

  for (int i = 0; i < edge_count; i++) {
    getline(infile, line);
    istringstream iss(line);
    int a, b;
    if (!(iss >>a >> b)) {
      break;
    }

    edges_v1[i] = a;
    edges_v2[i] = b;
  }

  return make_tuple(node_count, edge_count, edges_v1, edges_v2);
}

int *random_greedy(int node_count, int edge_count, int* edges) {
  int* solution = new int[node_count]; // allocate mem on the heap so the local var remains after function call, delete[] it later
  for (int i = 0; i < node_count; i++) {
    solution[i] = -1;
  }

  int nodes[node_count];
  for (int i = 0; i < node_count; i++) {
    nodes[i] = i;
  }

  random_shuffle(&nodes[0], &nodes[node_count]);

  return solution;
}

string local_search(int node_count, int edge_count, vector<int> edges, int num_iter) {
  // solution = random_greedy(node_count, edge_count, edges)



  // return solution
  return "heollo";
}

int main(int argc, char const *argv[]) {
  if (argc > 1) {
    string file_name = argv[1];
    int node_count;
    int edge_count;
    int* edges_v1;
    int* edges_v2;

    tie(node_count, edge_count, edges_v1, edges_v2) = parse_data(file_name);
    cout << "node count: " << node_count << endl << "edge count: " << edge_count
         << endl << "last edge: " << edges_v1[edge_count-1] << " " << edges_v2[edge_count-1];
  }
  return 0;
}
