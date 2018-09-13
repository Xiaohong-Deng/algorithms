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


int* naive_greedy(size_t node_count, const tuple<int, int> edges[],
                  const int nodes[], map<int, vector<int>> neighbors) {
  int* node_colors = new int[node_count];

  for (size_t i = 0; i < node_count; i++) {
    node_colors[i] = -1;
  }

  for (size_t i = 0; i < node_count; i++) {
    vector<int> my_neighbors = neighbors[nodes[i]];
    vector<int> neighbor_colors;
    for (size_t j = 0; j < my_neighbors.size(); j++) {
      neighbor_colors.push_back(node_colors[my_neighbors[j]]);
    }

    unordered_set<int> neighbor_colors_set(neighbor_colors.begin(), neighbor_colors.end());

    // use max_colors
    for (size_t j = 0; j < node_count; j++) {
      if (neighbor_colors_set.find(j) == neighbor_colors_set.end()) {
        node_colors[i] = j;
        break;
      }
    }
  }

  return node_colors;
}


tuple<size_t, int*> random_greedy(size_t node_count, size_t edge_count, const tuple<int, int> edges[], size_t num_iter) {
  int* sol; // allocate mem on the heap so the local var remains after function call, delete[] it later
  size_t num_colors = node_count;
  /**
   * TODO: create colors, nodes, neighbors
   * @param i [description]
   */
  int nodes[node_count];
  map<int, vector<int>> neighbors = node_to_neighbors(edge_count, edges);

  for (size_t i = 0; i < node_count; i++) {
    nodes[i] = i;
  }

  for (size_t i = 0; i < num_iter; i++) {
    srand(time(0));
    random_shuffle(&nodes[0], &nodes[node_count]);
    int* temp_sol = naive_greedy(node_count, edges, nodes, neighbors);
    unordered_set<int> temp_colors(temp_sol, temp_sol + node_count);
    if (temp_colors.size() < num_colors) {
      sol = temp_sol;
      num_colors = temp_colors.size();
    }
  }

  return make_tuple(num_colors, sol);
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

    stringstream solution;
    size_t num_colors;
    int* node_colors;

    tie(node_count, edge_count, edges) = parse_data(file_name);

    // map<int, vector<int>> ntn = node_to_neighbors(edge_count, edges);
    tie(num_colors, node_colors) = random_greedy(node_count, edge_count, edges);

    solution << num_colors << " 0" << endl;

    for (size_t i = 0; i < node_count; i++) {
      solution << node_colors[i] << " ";
    }

    cout << solution.str() << endl;

    delete[] edges;
    delete[] node_colors;
  }

  return 0;
}
