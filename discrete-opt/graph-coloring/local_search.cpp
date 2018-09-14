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


tuple<size_t, int*> random_greedy(size_t node_count, size_t edge_count,
                                  const tuple<int, int> edges[],
                                  map<int, vector<int>> neighbors, size_t num_iter) {
  int* sol; // allocate mem on the heap so the local var remains after function call, delete[] it later
  size_t num_colors = node_count;

  int nodes[node_count];
  cout << "num_iter in random greedy: " << num_iter << endl;
  for (size_t i = 0; i < node_count; i++) {
    nodes[i] = i;
  }

  random_device ran_dev;

  for (size_t i = 0; i < num_iter; i++) {
    srand(ran_dev());
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


tuple<size_t, int*> local_search(size_t node_count, size_t edge_count, const tuple<int, int> edges[], size_t num_iter, int threshold) {
  cout << "num_iter in local search: " << num_iter << endl;
  cout << "threshold before restart local search: " << threshold << endl;

  size_t num_colors;
  int* sol;

  size_t ls_num_colors;
  int* ls_sol;

  map<int, vector<int>> neighbors = node_to_neighbors(edge_count, edges);

  // prepare random generator for picking nodes
  const int range_from = 0;
  const int range_to = node_count - 1;
  random_device ran_dev;
  mt19937 generator(ran_dev());
  uniform_int_distribution<int> distr(range_from, range_to); // inclusive

  tie(num_colors, sol) = random_greedy(node_count, edge_count, edges, neighbors, 1);

  for (size_t i = 0; i < num_iter; i++) {
    // if (i % 300 == 1) {
    //   cout << "iter No. " << num_iter << endl;
    // }
    int obj_val = 0; // 2 * sum(Bi * Ci) - sum(Ci^2)

    // we have a feasible sol to start with
    int bad_edges = 0;
    // bookkeeping Ci, initially 0s, extra slots stay that way
    int color_to_size[node_count] = {};
    // bookkeeping Bi, initially 0s, extra slots stay that way
    int color_to_bad_edges[node_count] = {};
    // bookkeeping the number of bad edges each node is involved, initially 0s
    int node_to_bad_edges[node_count] = {};
    // vector<vector<int>> color_to_nodes(num_colors, vector<int>());
    tie(ls_num_colors, ls_sol) = random_greedy(node_count, edge_count, edges, neighbors, 1);

    // initialize color_to_size
    for (size_t j = 0; j < node_count; j++) {
      int color = ls_sol[j];
      color_to_size[color]++;
      // color_to_nodes[color].push_back(i);
    }

    // initialize Ci and obj_val
    for (size_t j = 0; j < ls_num_colors; j++) {
      obj_val -= color_to_size[j] * color_to_size[j];
    }

    cout << "starting obj_val: " << obj_val << endl;

    // bookkeeping the number of times random neighbor fails you
    int count = 0;

    while (true) {
      // randomly pick a neighbor first, iterate over all colors to see if obj_val is improved
      int nd = distr(generator);
      int cur_color = ls_sol[nd];

      // if (count != 0)
        // cout << "counter: " << count << endl;

      int Bi = color_to_bad_edges[cur_color];
      int Ci = color_to_size[cur_color];
      int Bj;
      int Cj;
      int new_Bi = color_to_bad_edges[cur_color] - node_to_bad_edges[nd];
      int new_Ci = color_to_size[cur_color] - 1;
      int new_Bj; // number of bad edges in color j by changing node n color
      int new_Cj; // number of nodes in color j by change node n color
      int num_bn_cj; // number of bad neighbors in color j
      int temp_obj_val;

      for (size_t j = 0; j < ls_num_colors; j++) {
        // decide if we want to change to color j
        // by changing color from i to j, we potentially decrease Bi
        // and increase Bj, we also decrease Ci and increase Cj
        if ((int) j != cur_color) {
          Bj = color_to_bad_edges[j];
          Cj = color_to_size[j];
          new_Bj = color_to_bad_edges[j];
          new_Cj = color_to_size[j] + 1;
          num_bn_cj = 0;

          for (auto nb: neighbors[nd]) {
            if (ls_sol[nb] == (int) j) {
              new_Bj++;
              num_bn_cj++;
            }
          }

          // remove old Ci^2, Cj^2, 2*Bi*Ci and 2*Bj*Cj from obj_val
          temp_obj_val = obj_val + Ci * (Ci - (Bi << 1)) + Cj * (Cj - (Bj << 1));

          // add new Ci^2, Cj^2, 2*Bi*Ci, and 2*Bj*Cj to obj_val
          temp_obj_val += new_Ci * ((new_Bi << 1) - new_Ci) + new_Cj * ((new_Bj << 1) - new_Cj);

          // first legal neighbor
          if (temp_obj_val < obj_val) {
            // cout << temp_obj_val << endl;
            // cout << "found new obj_val" << endl;
            // maintain new obj_val
            obj_val = temp_obj_val;
            // color config
            ls_sol[nd] = j;
            // total number of bad edges
            bad_edges += (new_Bi - Bi + new_Bj - Bj);
            // cout << "bad edges: " << bad_edges << endl;

            color_to_bad_edges[cur_color] = new_Bi;
            color_to_bad_edges[j] = new_Bj;
            color_to_size[cur_color] = new_Ci;
            color_to_size[j] = new_Cj;

            if (new_Ci == 0) {
              ls_num_colors--;
            }

            if (Cj == 0) {
              ls_num_colors++;
            }

            // cout << "2nd if done: bad_edges = " << bad_edges << endl;

            count = 0;
            break;
          }
        }

        if (j == ls_num_colors - 1) {
          // cout << "neighbor not found for a random pick" << endl;
          count++;
        }
      }

      // if too many times we are not able to update
      // assume we reach the local minima
      if (count >= threshold && bad_edges == 0) {
        // cout << "local minima num_colors: " << ls_num_colors << endl;
        break;
      }
    }

    if (ls_num_colors < num_colors) {
      num_colors = ls_num_colors;
      sol = ls_sol;
    }
  }

  // for (size_t i = 0; i < num_colors; i++) {
  //   for_each(color_to_nodes[i].begin(), color_to_nodes[i].end(), [](int node) { cout << node << " "; });
  //   cout << endl;
  // }

  unordered_set<int> temp_colors(sol, sol + node_count);

  for (auto &elem: temp_colors) {
    cout << elem << " ";
  }

  cout << endl;

  return make_tuple(num_colors, sol);
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

    int mode = 0;

    if (argc == 3) {
      mode = stoi(argv[2]);
      if (mode < 0 || mode > 1) {
        mode = 0;
      }
    }

    clock_t begin = clock();
    if (mode == 0) {
      tie(num_colors, node_colors) = local_search(node_count, edge_count, edges);
    } else {
      map<int, vector<int>> ntn = node_to_neighbors(edge_count, edges);
      tie(num_colors, node_colors) = random_greedy(node_count, edge_count, edges, ntn);
    }
    clock_t end = clock();
    double elapsed = double(end - begin) / CLOCKS_PER_SEC;

    cout << "time passed " << elapsed << endl;;

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
