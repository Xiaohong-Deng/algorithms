#include "local_search.h"


tuple<size_t, size_t, tuple<int, int>*> parse_data(string file_name) {
  size_t node_count;
  size_t edge_count;
  tuple<int, int>* edges;

  ifstream infile(file_name);
  string line;
  getline(infile, line);
  istringstream iss(line);

  if (!(iss >> node_count >> edge_count)) {
    cout << "empty file!" << endl;
    return make_tuple(node_count, edge_count, edges);
  }

  edges = new tuple<int, int>[edge_count];

  for (size_t i = 0; i < edge_count; i++) {
    getline(infile, line);
    istringstream iss(line);
    int a, b;
    if (!(iss >>a >> b)) {
      break;
    }

    edges[i] = make_tuple(a, b);
  }

  return make_tuple(node_count, edge_count, edges);
}


map<int, vector<int>> node_to_neighbors(size_t edge_count, const tuple<int, int> edges[]) {
  map<int, vector<int>> neighbors;

  for (size_t i = 0; i < edge_count; i++) {
    int v1 = get<0>(edges[i]);
    int v2 = get<1>(edges[i]);

    if (neighbors.find(v1) == neighbors.end()) {
      vector<int> ns;
      ns.push_back(v2);
      neighbors.insert(pair<int, vector<int>>(v1, ns));
    } else {
      neighbors[v1].push_back(v2);
    }

    if (neighbors.find(v2) == neighbors.end()) {
      vector<int> ns;
      ns.push_back(v1);
      neighbors.insert(pair<int, vector<int>>(v2, ns));
    } else {
      neighbors[v2].push_back(v1);
    }
  }

  return neighbors;

}


int* naive_greedy(size_t node_count, size_t edge_count, const tuple<int, int> edges[]) {

}


int* random_greedy(size_t node_count, size_t edge_count, const tuple<int, int> edges[]) {
  int* solution = new int[node_count]; // allocate mem on the heap so the local var remains after function call, delete[] it later

  for (size_t i = 0; i < node_count; i++) {
    solution[i] = -1;
  }

  int nodes[node_count];
  for (size_t i = 0; i < node_count; i++) {
    nodes[i] = i;
  }

  random_shuffle(&nodes[0], &nodes[node_count]);

  return solution;
}


string local_search(size_t node_count, size_t edge_count, const tuple<int, int> edges[], size_t num_iter) {
  // solution = random_greedy(node_count, edge_count, edges)



  // return solution
  return "heollo";
}


int main(int argc, char const *argv[]) {
  if (argc > 1) {
    string file_name = argv[1];
    size_t node_count;
    size_t edge_count;
    tuple<int, int>* edges;

    tie(node_count, edge_count, edges) = parse_data(file_name);
    // cout << "node count: " << node_count << endl << "edge count: " << edge_count
    //      << endl;

    map<int, vector<int>> ntn = node_to_neighbors(edge_count, edges);

    for (size_t i = 0; i < node_count; i++) {
      cout << "neighbors of " << i << endl;
      for (auto j: ntn[i]) {
        cout << j << " ";
      }
      cout << endl;
    }

    string solution = local_search(node_count, edge_count, edges);

    delete[] edges;
  }

  return 0;
}
