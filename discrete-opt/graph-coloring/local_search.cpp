#include "local_search.h"

// long and unsigned long are 64-bit in Ubuntu and 32-bit in Windows
double fast_exp(double p) {
  unsigned long bits = (unsigned long) (1512775 * p + (1072693248 - 60801));
  bits = bits << 32;
  double result;
  memcpy(&result, &bits, sizeof(bits));
  return result;
}


void update_state(int node, int current_color, size_t new_color, int num_bn_new_color,
                  int new_Bi, int new_Ci, int new_Bj, int new_Cj,
                  int* sol, int* color_to_bad_edges, int* color_to_size, int* node_to_bad_edges) {
  // color config
  sol[node] = new_color;

  color_to_bad_edges[current_color] = new_Bi;
  color_to_bad_edges[new_color] = new_Bj;
  color_to_size[current_color] = new_Ci;
  color_to_size[new_color] = new_Cj;
  node_to_bad_edges[node] = num_bn_new_color;
}


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


unordered_map<int, vector<int>> node_to_neighbors(size_t node_count, size_t edge_count, const tuple<int, int> edges[]) {
  unordered_map<int, vector<int>> neighbors;

  for (size_t i = 0; i < node_count; i++) {
    neighbors[i] = vector<int>();
  }

  // the last place we need edges tuple array
  // since we need to extract two end points
  // seperately why we just store them in seperate
  // arrays in the 1st place?
  for (size_t i = 0; i < edge_count; i++) {
    int v1 = get<0>(edges[i]);
    int v2 = get<1>(edges[i]);

    neighbors[v1].push_back(v2);
    neighbors[v2].push_back(v1);
  }

  return neighbors;

}


int* naive_greedy(size_t node_count, const int nodes[], unordered_map<int, vector<int>> neighbors) {
  int* node_colors = new int[node_count];

  for (size_t i = 0; i < node_count; i++) {
    node_colors[i] = -1;
  }

  // loop over nodes
  for (size_t i = 0; i < node_count; i++) {
    int cur_node = nodes[i];
    vector<int> my_neighbors = neighbors[cur_node];
    // index for color, value 1 for neighbor color yes and 0 for no
    int neighbor_colors[node_count] = {};

    // loop over neighbors
    for (auto nb: my_neighbors) {
      int neighbor_color = node_colors[nb]; // might be -1, which is the element before array[0]
      if (neighbor_color >= 0 && neighbor_colors[neighbor_color] == 0) {
        neighbor_colors[neighbor_color] = 1;
      }
    }

    // use max_colors
    for (size_t j = 0; j < node_count; j++) {
      if (neighbor_colors[j] == 0) {
        node_colors[cur_node] = j;
        break;
      }
    }
  }

  return node_colors;
}


tuple<size_t, int*> random_greedy(size_t node_count, size_t edge_count,
                                  unordered_map<int, vector<int>> neighbors, size_t num_iter) {
  int* sol; // allocate mem on the heap so the local var remains after function call, delete[] it later
  size_t num_colors = node_count;
  int nodes[node_count];

  for (size_t i = 0; i < node_count; i++) {
    nodes[i] = i;
  }

  random_device ran_dev;
  srand(ran_dev());

  for (size_t i = 0; i < num_iter; i++) {
    random_shuffle(&nodes[0], &nodes[node_count]);

    // clock_t begin = clock();
    int* temp_sol = naive_greedy(node_count, nodes, neighbors);
    // clock_t end = clock();
    // double elapsed = double(end - begin) / CLOCKS_PER_SEC;
    // cout << "time for naive greedy: " << elapsed << endl;

    unordered_set<int> temp_colors(temp_sol, temp_sol + node_count);

    if (temp_colors.size() < num_colors) {
      sol = temp_sol;
      num_colors = temp_colors.size();
    }
  }

  return make_tuple(num_colors, sol);
}


tuple<size_t, int*> local_search(size_t node_count, size_t edge_count,
                                 unordered_map<int, vector<int>> neighbors, int threshold,
                                 mt19937 gen, uniform_int_distribution<int> distr) {
  size_t num_colors;
  int* sol;
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
  tie(num_colors, sol) = random_greedy(node_count, edge_count, neighbors, 1);

  // initialize color_to_size
  for (size_t j = 0; j < node_count; j++) {
    int color = sol[j];
    color_to_size[color]++;
  }

  // initialize Ci and obj_val
  for (size_t j = 0; j < num_colors; j++) {
    obj_val -= color_to_size[j] * color_to_size[j];
  }

  // bookkeeping the number of times random neighbor fails you
  int count = 0;

  while (true) {
    // randomly pick a neighbor first, iterate over all colors to see if obj_val is improved
    int nd = distr(gen);
    int cur_color = sol[nd];

    int Bi = color_to_bad_edges[cur_color];
    int Ci = color_to_size[cur_color];
    int Bi_nd = node_to_bad_edges[nd];
    int Bj;
    int Cj;
    int new_Bi = Bi - node_to_bad_edges[nd];
    int new_Ci = Ci - 1;
    int new_Bj; // number of bad edges in color j by changing node n color
    int new_Cj; // number of nodes in color j by change node n color
    int num_bn_cj; // number of bad neighbors in color j for nd
    int delta;
    int term_i;
    int term_j;

    // should we allow the node to take color that doesn't exist in the current config
    // by convert < to <=, or even more lenient, j < node_count
    for (size_t j = 0; j < num_colors; j++) {
      // decide if we want to change to color j
      // by changing color from i to j, we potentially decrease Bi
      // and increase Bj, we also decrease Ci and increase Cj
      // some colors in the middle might be reduced to 0 members,
      if ((int) j != cur_color) {
        Bj = color_to_bad_edges[j];
        Cj = color_to_size[j];
        new_Bj = Bj;
        new_Cj = Cj + 1;
        num_bn_cj = 0;

        for (auto nb: neighbors[nd]) {
          if (sol[nb] == (int) j) {
            num_bn_cj++;
          }
        }

        new_Bj += num_bn_cj;

        term_i = Bi_nd - Bi - Bi_nd*Ci; // might be negative, shift left is sometimes wrong if signed
        term_j = Bj + Cj * num_bn_cj + num_bn_cj; // positive, shift left is wrong sometimes if signed
        // delta needs to be < 0
        // delta = (Ci << 1) - (Cj << 1) - 2 + term_i * 2 + (term_j << 1);
        // can we use above delta instead? No, shifting signed numbers gives incorrect results sometimes
        // shift unsigned then cast them to signed may overflow
        delta = (Ci - Cj - 1 + term_i + term_j) * 2;
        // remove old Ci^2, Cj^2, 2*Bi*Ci and 2*Bj*Cj from obj_val

        // add new Ci^2, Cj^2, 2*Bi*Ci, and 2*Bj*Cj to obj_val

        // first legal neighbor
        if (delta < 0) {
          // maintain new obj_val
          obj_val += delta;
          // color config
          sol[nd] = j;
          // total number of bad edges
          bad_edges += num_bn_cj - Bi_nd;

          color_to_bad_edges[cur_color] = new_Bi;
          color_to_bad_edges[j] = new_Bj;
          color_to_size[cur_color] = new_Ci;
          color_to_size[j] = new_Cj;
          node_to_bad_edges[nd] = num_bn_cj;

          if (new_Ci == 0) {
            num_colors--;
          }

          if (Cj == 0) {
            num_colors++;
          }

          count = 0;
          break;
        }
      }

      if (j == num_colors - 1) {
        count++;
      }
    }

    // if too many times we are not able to update
    // assume we reach the local minima
    if (count >= threshold && bad_edges == 0) {
      break;
    }
  }

  return make_tuple(num_colors, sol);
}


tuple<size_t, int*> sim_annealing(size_t node_count, size_t edge_count,
                                 unordered_map<int, vector<int>> neighbors,
                                 mt19937 gen, uniform_int_distribution<int> distr,
                                 uniform_real_distribution<double> acceptance_distr,
                                 double threshold, double temperature, double alpha, double alpha_rate) {
  size_t num_colors;
  int* sol;
  int obj_val = 0; // 2 * sum(Bi * Ci) - sum(Ci^2)

  size_t opt_num_colors;
  int* opt_sol = new int[node_count];

  // we have a feasible sol to start with
  int bad_edges = 0;
  // bookkeeping Ci, initially 0s, extra slots stay that way
  int color_to_size[node_count] = {};
  // bookkeeping Bi, initially 0s, extra slots stay that way
  int color_to_bad_edges[node_count] = {};
  // bookkeeping the number of bad edges each node is involved, initially 0s
  int node_to_bad_edges[node_count] = {};
  // vector<vector<int>> color_to_nodes(num_colors, vector<int>());
  tie(num_colors, sol) = random_greedy(node_count, edge_count, neighbors, 1);

  size_t start_num_colors = num_colors;
  uniform_int_distribution<size_t> color_distr(0, start_num_colors);

  opt_num_colors = num_colors;
  // we change the state of sol constantly, so we need a deep copy of sol to opt_sol
  for (size_t i = 0; i < node_count; i++) {
    opt_sol[i] = sol[i];
  }
  // initialize color_to_size
  for (size_t i = 0; i < node_count; i++) {
    int color = sol[i];
    color_to_size[color]++;
  }

  // initialize Ci and obj_val
  for (size_t i = 0; i < num_colors; i++) {
    obj_val -= color_to_size[i] * color_to_size[i];
  }

  // bookkeeping the number of times you choose a random neighbor
  size_t alpha_count = 0;
  // size_t overall_count = 0;
  size_t alpha_update_threshold = 640;

  while (temperature > threshold) {
    // randomly pick a neighbor first, iterate over all colors to see if obj_val is improved
    int nd = distr(gen);
    int cur_color = sol[nd];

    int Bi = color_to_bad_edges[cur_color];
    int Ci = color_to_size[cur_color];
    int Bi_nd = node_to_bad_edges[nd];
    int new_Bi = Bi - node_to_bad_edges[nd];
    int new_Ci = Ci - 1;

    // say we start with 5 colors
    // during the loop color 3 is reduced to 0 members
    // num_colors--, but color 4 still has members
    // we want to consider color 4 when changing colors
    // so we loop over start_num_colors
    // that said, we do not consider larger colors not in our starting point sol
    //
    // not longer iterate through all the colors becasue we are willing to move to a worse neighbor
    // it is only fair to pick a color at random, but if the node has no better neighbors it will get stuck
    // when temperature is low

    size_t j = color_distr(gen);
    if ((int) j != cur_color) {
      int Bj = color_to_bad_edges[j];
      int Cj = color_to_size[j];
      int new_Bj = Bj; // number of bad edges in color j by changing node n color
      int new_Cj = Cj + 1; // number of nodes in color j by change node n color
      int num_bn_cj = 0; // number of bad neighbors in color j

      for (auto nb: neighbors[nd]) {
        if (sol[nb] == (int) j) {
          num_bn_cj++;
        }
      }

      new_Bj += num_bn_cj;

      int term_i = Bi_nd - Bi - Bi_nd*Ci; // might be negative, no shift
      int term_j = Bj + Cj * num_bn_cj + num_bn_cj; // positive
      // delta needs to be < 0
      int delta = (Ci << 1) - (Cj << 1) - 2 + term_i + term_i + (term_j << 1);


      if (delta < 0) {
        obj_val += delta;
        bad_edges += num_bn_cj - Bi_nd;

        if (new_Ci == 0) {
          num_colors--;
        }

        if (Cj == 0) {
          num_colors++;
        }

        update_state(nd, cur_color, j, num_bn_cj,
                     new_Bi, new_Ci, new_Bj, new_Cj,
                     sol, color_to_bad_edges, color_to_size, node_to_bad_edges);

        if (bad_edges == 0 && num_colors < opt_num_colors) {
          opt_num_colors = num_colors;
          for (size_t l = 0; l < node_count; l++) {
            opt_sol[l] = sol[l];
          }
        }
        // update temperature
        temperature *= alpha;
        alpha_count++;
        if (alpha_count == alpha_update_threshold) {
          alpha *= alpha_rate;
          // alpha_count = 0;
          // alpha_update_threshold *= 7.196;
        }
      } else {
        double accept_rate = fast_exp(-delta / temperature);
        // accept_rate = exp(-delta / temperature);
        double accept = acceptance_distr(gen);

        if (accept < accept_rate) {
          obj_val += delta;
          bad_edges += num_bn_cj - Bi_nd;

          if (new_Ci == 0) {
            num_colors--;
          }

          if (Cj == 0) {
            num_colors++;
          }

          update_state(nd, cur_color, j, num_bn_cj,
                       new_Bi, new_Ci, new_Bj, new_Cj,
                       sol, color_to_bad_edges, color_to_size, node_to_bad_edges);

         // update temperature
         temperature *= alpha;
         alpha_count++;
         if (alpha_count == alpha_update_threshold) {
           alpha *= alpha_rate;
           // alpha_count = 0;
           // alpha_update_threshold *= 7.196;
         }
        }
      }
    }
    // overall_count++;
  }
  // cout << "overall_count: " << overall_count << endl;
  delete[] sol;
  return make_tuple(opt_num_colors, opt_sol);
}


tuple<size_t, int*> iterated_local_search(size_t node_count, size_t edge_count,
                                          const tuple<int, int> edges[], int mode, size_t num_iter,
                                          int threshold) {
  cout << "mode: " << mode << endl;
  cout << "num_iter in local search: " << num_iter << endl;

  size_t num_colors;
  int* sol;

  size_t ls_num_colors;
  int* ls_sol;

  unordered_map<int, vector<int>> neighbors = node_to_neighbors(node_count, edge_count, edges);

  // prepare random generator for picking nodes
  const int range_from = 0;
  const int range_to = node_count - 1;
  random_device ran_dev;
  mt19937 generator(ran_dev());
  uniform_int_distribution<int> distr(range_from, range_to); // inclusive

  tie(num_colors, sol) = random_greedy(node_count, edge_count, neighbors, 1);

  if (mode == 0) {
    for (size_t i = 0; i < num_iter; i++) {
      tie(ls_num_colors, ls_sol) = local_search(node_count, edge_count, neighbors, threshold,
                                                generator, distr);
      // in each local search we might have created some empty classes in the middle
      // e.g., color i has 0 members but color i+1 has non zero members
      // thus ls_num_colors =< largest color n
      if (ls_num_colors < num_colors) {
        num_colors = ls_num_colors;
        sol = ls_sol;
      }
    }
  } else {
    uniform_real_distribution<double> acceptance_distr(0.0, 1.0);
    for (size_t i = 0; i < num_iter; i++) {
      tie(ls_num_colors, ls_sol) = sim_annealing(node_count, edge_count, neighbors,
                                                 generator, distr, acceptance_distr);

      if (ls_num_colors < num_colors) {
        num_colors = ls_num_colors;
        sol = ls_sol;
      }
    }
  }

  unordered_set<int> temp_colors(sol, sol + node_count);

  for (auto &elem: temp_colors) {
    cout << elem << " ";
  }

  cout << endl;

  delete[] ls_sol;
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
      if (mode < 0 || mode > 2) {
        mode = 0;
      }
    }

    clock_t begin = clock();
    if (mode == 0 || mode == 1) {
      tie(num_colors, node_colors) = iterated_local_search(node_count, edge_count, edges, mode);
    } else {
      unordered_map<int, vector<int>> ntn = node_to_neighbors(node_count, edge_count, edges);
      tie(num_colors, node_colors) = random_greedy(node_count, edge_count, ntn);
    }
    clock_t end = clock();
    double elapsed = double(end - begin) / CLOCKS_PER_SEC;

    cout << "time passed: " << elapsed << endl;

    solution << num_colors << " 0" << endl;

    for (size_t i = 0; i < node_count; i++) {
      solution << node_colors[i] << " ";
    }

    cout << solution.str() << endl;

    delete[] edges;
    delete[] node_colors;
  } else {
    std::cout << "Please provide the file containing the graph." << '\n';
  }

  return 0;
}
