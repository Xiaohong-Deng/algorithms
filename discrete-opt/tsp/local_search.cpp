#include "local_search.h"
#define EPSILON 1e-6


// a flaw is this function can't take care of i=j
size_t condensed_index(size_t i, size_t j, size_t n) {
  if (i < j) {
    size_t temp = j;
    j = i;
    i = temp;
  }

  return n * j - ((j * (j + 1)) >> 1) + i - 1 - j;
}


size_t parse_node_count(string file_name) {
  size_t node_count;

  ifstream infile(file_name);
  string line;
  getline(infile, line);
  istringstream iss(line);

  if (!(iss >> node_count)) {
    cout << "empty file!" << endl;
    node_count = 0;
    return node_count;
  }

  return node_count;
}


tuple<size_t, double*, double*> parse_data(string file_name) {
  size_t node_count;
  double* x_coords;
  double* y_coords;

  ifstream infile(file_name);
  string line;
  getline(infile, line);
  istringstream iss(line);

  if (!(iss >> node_count)) {
    cout << "empty file!" << endl;
    return make_tuple(node_count, x_coords, y_coords);
  }

  x_coords = new double[node_count];
  y_coords = new double[node_count];

  for (size_t i = 0; i < node_count; i++) {
    getline(infile, line);
    istringstream iss(line);
    double x, y;
    if (!(iss >> x >> y)) {
      break;
    }

    x_coords[i] = x;
    y_coords[i] = y;
  }

  return tie(node_count, x_coords, y_coords);
}


// use condensed array, make sure dimension is right
double* load_dist_table(const size_t node_count, string file_name, string ds_name) {
  double* dist_table;
  dist_table = new double[node_count * node_count];

  H5File file(file_name, H5F_ACC_RDONLY);
  DataSet dataset = file.openDataSet(ds_name);
  DataSpace dataspace = dataset.getSpace();
  hsize_t dims_out[1];
  int rank = dataspace.getSimpleExtentDims(dims_out, NULL);
  cout << "rank " << rank << ", dimensions " <<
      (unsigned long)(dims_out[0]) << endl;
  DataSpace memspace(1, dims_out);
  dataset.read(dist_table, PredType::NATIVE_DOUBLE, memspace, dataspace);
  return dist_table;
}


double update_initial_state(const size_t node_count, const size_t tour[],
                            const double dist_table[], double edges[]) {
  double total_dist = 0;

  size_t idx = condensed_index(tour[node_count - 1], tour[0], node_count);
  edges[node_count - 1] = dist_table[idx];
  total_dist += edges[node_count - 1];

  size_t node = tour[0];
  for (size_t i = 0; i < node_count - 1; i++) {
    size_t next_node = tour[i + 1];

    idx  = condensed_index(node, next_node, node_count);
    edges[i] = dist_table[idx];
    total_dist += edges[i];

    node = next_node;
  }

  return total_dist;
}


void update_penalty_and_active_neighborhood(const size_t node_count, const double lambda,
                                            const size_t tour[], const double edges[], double &aug_total_dist,
                                            size_t penalties[], unordered_set<size_t>& is_active) {
  double max_util = 0;
  vector<tuple<size_t, size_t, size_t>> penalized;
  for (size_t i = 0; i < node_count - 1; i++) {
    size_t ni = tour[i];
    size_t nj = tour[i + 1];
    size_t idx = condensed_index(ni, nj, node_count); // holds if ni != nj
    double util = edges[i] / (penalties[idx] + 1);
    if (util > max_util) {
      max_util = util;
      penalized.clear();
      penalized.push_back(make_tuple(ni, nj, idx));
    } else if (util == max_util) {
      penalized.push_back(make_tuple(ni, nj, idx));
    }
  }

  size_t ni = tour[node_count - 1];
  size_t nj = tour[0];
  size_t idx = condensed_index(ni, nj, node_count);
  double util = edges[node_count - 1] / (penalties[idx] + 1);
  if (util > max_util) {
    max_util = util;
    penalized.clear();
    penalized.push_back(make_tuple(ni, nj, idx));
  } else if (util == max_util) {
    penalized.push_back(make_tuple(ni, nj, idx));
  }
  // cout << "max_util: " << max_util << endl;
  for (auto p : penalized) {
    penalties[get<2>(p)]++;
    is_active.insert({get<0>(p), get<1>(p)});
    aug_total_dist += lambda;
  }
}


void update_tour(size_t seg_start, size_t seg_end, size_t tour[]) {
  size_t mid = ((seg_start + seg_end) >> 1) + 1;
  for (size_t i = seg_start, j = seg_end; i < mid; i++, j--) {
    size_t temp = tour[j];
    tour[j] = tour[i];
    tour[i] = temp;
  }
}


void update_tour(size_t seg_start, size_t seg_end, size_t tour[], double edges[]) {
  size_t mid = ((seg_start + seg_end) >> 1) + 1;
  for (size_t i = seg_start, j = seg_end; i < mid; i++, j--) {
    size_t temp = tour[j];
    double temp_edge = edges[j - 1];
    if (i != j) {
      tour[j] = tour[i];
      tour[i] = temp;

      edges[j - 1] = edges[i];
      edges[i] = temp_edge;
    }
  }
}


// new_t1->new_t2, new_t3->new_t4
void update_tour(const size_t node_count, size_t seg_start, size_t seg_end, size_t tour[], const size_t aug_tour[]) {
  memcpy(tour, aug_tour, node_count * sizeof *aug_tour);
  update_tour(seg_start, seg_end, tour);
}


void update_state(const size_t node_count, const size_t seg_start, const size_t seg_end,
                  size_t aug_tour[], double new_e1, double new_e2, double edges[],
                  size_t pred[], size_t suc[], size_t index[], unordered_set<size_t>& is_active) {
  // cout <<"updating..."<<endl;
  // cout << "before: tour[seg_start] is " << aug_tour[seg_start] << endl;
  // cout << seg_start << " " << seg_end <<endl;
  // update aug_tour
  update_tour(seg_start, seg_end, aug_tour, edges);
  // cout << "after: tour[seg_start] is " << aug_tour[seg_start] << endl;
  // corner case where seg_start = 0 or node_count - 1
  // note we rule out the possibility of seg_start = 0 and seg_end = node_count - 1
  // because that implies t1->t2 = t3-t4
  // the code can handle seg_start = seg_end = 0 or seg_start = seg_end = node_count - 1

  // node in the segment needs to update both its pred and suc
  // the two nodes adjacent to the segment need to update their suc or pred
  size_t t1, t2, t3, t4, t1_idx;
  t2 = aug_tour[seg_start];
  t3 = aug_tour[seg_end];
  if (seg_start == 0) {
    t1_idx = node_count - 1;
    t1 = aug_tour[node_count - 1];
  } else {
    t1_idx = seg_start - 1;
    t1 = aug_tour[seg_start - 1];
  }

  suc[t1] = t2;

  edges[t1_idx] = new_e1;
  pred[t2] = t1;
  suc[t2] = aug_tour[seg_start + 1];
  index[t2] = seg_start;
  is_active.insert(t1);
  is_active.insert(t2);

  if (seg_end == node_count - 1) {
    t4 = aug_tour[0];
  } else {
    t4 = aug_tour[seg_end + 1];
  }

  pred[t4] = t3;

  edges[seg_end] = new_e2;
  pred[t3] = aug_tour[seg_end - 1];
  suc[t3] = t4;
  index[t3] = seg_end;
  is_active.insert(t3);
  is_active.insert(t4);

  for (size_t i = seg_start + 1; i < seg_end; i++) {
    size_t cur = aug_tour[i];
    index[cur] = i;
    pred[cur] = aug_tour[i - 1];
    suc[cur] = aug_tour[i + 1];
  }
}


tuple<size_t, size_t, double, double, double, double> compare_edges(const size_t node_count, const size_t i, const size_t j, const double lambda,
                                                    const size_t t1, const size_t t2, const size_t t1_idx, const double e1, const size_t p1,
                                                    const double edges[], const size_t penalties[], const double dist_table[],
                                                    const size_t tour[]) {
  size_t t3 = tour[i];
  size_t t4 = tour[j];
  size_t e2_idx = condensed_index(t3, t4, node_count);
  size_t p2 = penalties[e2_idx];
  double e2 = edges[i];
  assert(fabs(e2 - dist_table[e2_idx]) < EPSILON);

  size_t new_e1_idx = condensed_index(t1, t3, node_count);
  size_t new_e2_idx = condensed_index(t2, t4, node_count);

  size_t p3 = penalties[new_e1_idx];
  size_t p4 = penalties[new_e2_idx];
  double new_e1 = dist_table[new_e1_idx];
  double new_e2 = dist_table[new_e2_idx];
  double delta = new_e1 + new_e2 - e1 - e2;
  int delta_p = p3 + p4 - p1 - p2;
  double aug_delta = delta + lambda * delta_p;

  // case 1a and 1b: edge in is_active is before or after the other edge in the tour
  size_t seg_start, seg_end;
  // i = node_count - 1, t1_idx = node_count - 2, i > t1_idx
  // i = node_count - 2, t1_idx = node_count - 1, i < t1_idx
  // we rule out the possibility of i = t1_idx in compare_edges_to_update()
  // so seg_start is always larger than or equal to seg_end
  if (i > t1_idx) {
    seg_start = t1_idx + 1;
    seg_end = i;
  } else {
    // includes when t1_idx = node_count - 1
    seg_start = i + 1;
    seg_end = t1_idx;
  }

  return make_tuple(seg_start, seg_end, new_e1, new_e2, delta, aug_delta);
}


tuple<bool, double, double, double> compare_edges_to_update_best_legal(const size_t node_count, double total_dist, double aug_total_dist, double min_total_dist,
                                                      size_t t1, size_t t1_idx, size_t t2, size_t p1, double e1, double lambda,
                                                      size_t tour[], size_t aug_tour[], double edges[], size_t pred[], size_t suc[], size_t index[],
                                                      const double dist_table[], const size_t penalties[], unordered_set<size_t>& is_active) {
  bool updated = false;
  double max_aug_delta = numeric_limits<double>::max();
  vector<tuple<size_t, size_t, double, double, double>> seg_index_candidates;
  for (size_t i = 0; i < node_count - 1; i++) {
    if (i != t1_idx) {
      size_t seg_start, seg_end;
      double new_e1, new_e2, delta, aug_delta;
      tie(seg_start, seg_end, new_e1, new_e2, delta, aug_delta) = compare_edges(node_count, i, i + 1, lambda, t1, t2,
                                                                                t1_idx, e1, p1, edges, penalties, dist_table, aug_tour);
      if (delta < -EPSILON && (min_total_dist - total_dist - delta) > EPSILON) {
        min_total_dist = total_dist + delta;
        update_tour(node_count, seg_start, seg_end, tour, aug_tour);
      }
      if (aug_delta < -EPSILON && aug_delta < max_aug_delta) {
        max_aug_delta = aug_delta;
        seg_index_candidates.clear();
        seg_index_candidates.push_back(make_tuple(seg_start, seg_end, new_e1, new_e2, delta));
      } else if (aug_delta < -EPSILON && aug_delta == max_aug_delta) {
        seg_index_candidates.push_back(make_tuple(seg_start, seg_end, new_e1, new_e2, delta));
      }
    }
  }

  if (t1_idx != node_count -1) {
    size_t seg_start, seg_end;
    double new_e1, new_e2, delta, aug_delta;
    tie(seg_start, seg_end, new_e1, new_e2, delta, aug_delta) = compare_edges(node_count, node_count - 1, 0, lambda, t1, t2,
                                                                              t1_idx, e1, p1, edges, penalties, dist_table, aug_tour);
    if (delta < -EPSILON && (min_total_dist - total_dist - delta) > EPSILON) {
      min_total_dist = total_dist + delta;
      update_tour(node_count, seg_start, seg_end, tour, aug_tour);
    }

    if (aug_delta < -EPSILON && aug_delta < max_aug_delta) {
      max_aug_delta = aug_delta;
      seg_index_candidates.clear();
      seg_index_candidates.push_back(make_tuple(seg_start, seg_end, new_e1, new_e2, delta));
    } else if (aug_delta < -EPSILON && aug_delta == max_aug_delta) {
      seg_index_candidates.push_back(make_tuple(seg_start, seg_end, new_e1, new_e2, delta));
    }
  }

  if (!seg_index_candidates.empty()) {
    random_device ran_dev;
    default_random_engine rng(ran_dev());
    uniform_int_distribution<size_t> distr(0, seg_index_candidates.size() - 1);
    size_t idx = distr(rng);

    size_t seg_start, seg_end;
    double new_e1, new_e2, delta;
    tie(seg_start, seg_end, new_e1, new_e2, delta) = seg_index_candidates[idx];
    total_dist += delta;
    aug_total_dist += max_aug_delta;
    update_state(node_count, seg_start, seg_end, aug_tour, new_e1, new_e2, edges, pred, suc, index, is_active);
    updated = true;
  }

  return make_tuple(updated, min_total_dist, total_dist, aug_total_dist);
}


tuple<bool, double, double, double> compare_edges_to_update_first_legal(const size_t node_count, double total_dist, double aug_total_dist, double min_total_dist,
                                                      size_t t1, size_t t1_idx, size_t t2, size_t p1, double e1, double lambda,
                                                      size_t tour[], size_t aug_tour[], double edges[], size_t pred[], size_t suc[], size_t index[],
                                                      const double dist_table[], const size_t penalties[], unordered_set<size_t>& is_active) {
  bool updated = false;
  for (size_t i = 0; i < node_count - 1; i++) {
    // if 2 edges overlap swap breaks the circle
    if (i != t1_idx) {
      // search sub-neighborhood n
      size_t seg_start, seg_end;
      double new_e1, new_e2, delta, aug_delta;
      tie(seg_start, seg_end, new_e1, new_e2, delta, aug_delta) = compare_edges(node_count, i, i + 1, lambda, t1, t2,
                                                                                t1_idx, e1, p1, edges, penalties, dist_table, aug_tour);
      // is it better in vanilla objective function
      // if so keep the move as the gobal best
      if (delta < -EPSILON && (min_total_dist - total_dist - delta) > EPSILON) {
        // update tour and total_dist
        min_total_dist = total_dist + delta;
        update_tour(node_count, seg_start, seg_end, tour, aug_tour);
      }

      // can we move
      if (aug_delta < -EPSILON) {
        aug_total_dist += aug_delta;
        total_dist += delta;
        update_state(node_count, seg_start, seg_end, aug_tour, new_e1, new_e2, edges, pred, suc, index, is_active);
        // early return to search in the next sub-neighborhood
        updated = true;
        return make_tuple(updated, min_total_dist, total_dist, aug_total_dist);
      }
    }
  }

  if (t1_idx != node_count - 1) {
    size_t seg_start, seg_end;
    double new_e1, new_e2, delta, aug_delta;
    tie(seg_start, seg_end, new_e1, new_e2, delta, aug_delta) = compare_edges(node_count, node_count - 1, 0, lambda, t1, t2,
                                                                              t1_idx, e1, p1, edges, penalties, dist_table, aug_tour);
    // is it better in vanilla objective function
    // if so keep the move as the gobal best
    if (delta < -EPSILON && (min_total_dist - total_dist - delta) > EPSILON) {
      min_total_dist = total_dist + delta;
      update_tour(node_count, seg_start, seg_end, tour, aug_tour);
    }

    // can we move
    if (aug_delta < -EPSILON) {
      aug_total_dist += aug_delta;
      total_dist += delta;
      update_state(node_count, seg_start, seg_end, aug_tour, new_e1, new_e2, edges, pred, suc, index, is_active);
      updated = true;
    }
  }

  return make_tuple(updated, min_total_dist, total_dist, aug_total_dist);
}


tuple<double, double, double> fast_local_search(double aug_total_dist, double total_dist, double min_total_dist,
                                        const size_t node_count, const double lambda, const double dist_table[],
                                        const size_t penalties[], size_t tour[], size_t pred[], size_t suc[], size_t index[],
                                        size_t aug_tour[], double edges[], unordered_set<size_t>& is_active) {
  // compare, swap, update
  while (!is_active.empty()) {
    for (auto it = is_active.begin(); it != is_active.end();) {
      // n involves two edges, one edge with all other edges
      bool updated;
      size_t t1, t2, p1, e1_idx, t1_idx, t2_idx;
      double e1;
      // case 1: compare edges where value is the incident node
      t2 = *it;
      t2_idx = index[t2];
      if (t2_idx == 0) {
        t1_idx = node_count - 1;
      } else {
        t1_idx = t2_idx - 1;
      }
      t1 = aug_tour[t1_idx];
      // t1->t2
      e1 = edges[t1_idx];
      e1_idx = condensed_index(t1, t2, node_count);
      p1 = penalties[e1_idx];
      assert(fabs(e1 - dist_table[e1_idx]) < EPSILON);

      tie(updated, min_total_dist, total_dist, aug_total_dist) = compare_edges_to_update_best_legal(node_count, total_dist, aug_total_dist, min_total_dist,
                                                                                t1, t1_idx, t2, p1, e1, lambda, tour, aug_tour, edges, pred, suc, index,
                                                                                dist_table, penalties, is_active);
      // if we early returned we go to the next sub-neighborhood
      // the rest of this iteration won't be executed so we don't need
      // to update old_aug_total_dist
      if (updated) {
        ++it;
        continue;
      }

      // case 2: compare edges where value is outgoing node
      t1 = *it;
      t1_idx = index[t1];
      if (t1_idx == node_count - 1) {
        t2_idx = 0;
      } else {
        t2_idx = t1_idx + 1;
      }
      t2 = aug_tour[t2_idx];
      e1 = edges[t1_idx];
      e1_idx = condensed_index(t1, t2, node_count);
      p1 = penalties[e1_idx];
      assert(fabs(e1 - dist_table[e1_idx]) < EPSILON);
      tie(updated, min_total_dist, total_dist, aug_total_dist) = compare_edges_to_update_best_legal(node_count, total_dist, aug_total_dist, min_total_dist,
                                                                                t1, t1_idx, t2, p1, e1, lambda, tour, aug_tour, edges, pred, suc, index,
                                                                                dist_table, penalties, is_active);
      if (!updated) {
        it = is_active.erase(it);
      } else {
        ++it;
      }
    }
  }

  return make_tuple(min_total_dist, total_dist, aug_total_dist);
}


tuple<double, size_t*> guided_fast_local_search(const size_t node_count, const double dist_table[], const size_t num_iter) {
  double total_dist;
  size_t* pred = new size_t[node_count];
  size_t* suc = new size_t[node_count];
  size_t* tour = new size_t[node_count];
  // node to idx in tour
  size_t* index = new size_t[node_count];
  // edges[0] = edge(tour[0], tour[1])
  // edges[node_count - 1] = edge(tour[node_count - 1], tour[0])
  double* edges = new double[node_count];

  // features are edges, feature_costs are edge length which are stored in dist_table
  // penalties is in condensed matrix form
  size_t edge_count = (node_count * (node_count - 1)) >> 1;
  size_t* penalties = new size_t[edge_count];
  for (size_t i = 0; i < edge_count; i++) {
    penalties[i] = 0;
  }

  for (size_t i = 0; i < node_count; i++) {
    tour[i] = i;
  }

  random_device ran_dev;
  auto rng = default_random_engine {ran_dev()};
  // mt19937 eng(ran_dev());
  shuffle(&tour[0], &tour[node_count], rng);

  size_t cur = tour[0];
  pred[cur] = tour[node_count - 1];
  suc[cur] = tour[1];
  index[cur] = 0;
  size_t prev = cur;
  for (size_t i = 1; i < node_count - 1; i++) {
    cur = tour[i];
    index[cur] = i;
    pred[cur] = prev;
    suc[cur] = tour[i + 1];
    prev = cur;
  }
  cur = tour[node_count - 1];
  pred[cur] = prev;
  suc[cur] = tour[0];
  index[cur] = node_count - 1;

  // node represents sub-neighborhood
  unordered_set<size_t> is_active;
  auto hint = is_active.begin();
  for (size_t i = 0; i < node_count; i++) {
    hint = is_active.insert(hint, tour[i]);
  }
  // return the distance and update all arrays
  total_dist = update_initial_state(node_count, tour, dist_table, edges);
  double aug_total_dist = total_dist;
  double min_total_dist = total_dist;
  size_t* aug_tour = new size_t[node_count];
  memcpy(aug_tour, tour, node_count * sizeof *tour);

  double alpha = 0.3;
  double lambda = 0.0;
  tie(min_total_dist, total_dist, aug_total_dist) = fast_local_search(aug_total_dist, total_dist, min_total_dist,
                                                      node_count, lambda, dist_table, penalties, tour,
                                                      pred, suc, index, aug_tour, edges, is_active);
  lambda = (alpha * min_total_dist) / node_count;
  update_penalty_and_active_neighborhood(node_count, lambda, aug_tour, edges, aug_total_dist, penalties, is_active);
  for (size_t i = 1; i < num_iter; i++) {
    // update configuration and return a local minima value for aug_total_dist and total_dist
    tie(min_total_dist, total_dist, aug_total_dist) = fast_local_search(aug_total_dist, total_dist, min_total_dist,
                                                        node_count, lambda, dist_table, penalties, tour,
                                                        pred, suc, index, aug_tour, edges, is_active);
    update_penalty_and_active_neighborhood(node_count, lambda, aug_tour, edges, aug_total_dist, penalties, is_active);
  }
  delete[] pred;
  delete[] suc;
  delete[] index;
  delete[] edges;
  delete[] penalties;
  delete[] aug_tour;
  return make_tuple(min_total_dist, tour);
}


int main(int argc, char const *argv[]) {
  if (argc > 2) {
    string file_name = argv[1];
    size_t num_iter = atoi(argv[2]);
    size_t node_count;
    double* x_coords;
    double* y_coords;
    bool parse_coords = false;

    int beginIdx = file_name.rfind('/');
    string dataset = file_name.substr(beginIdx + 1);

    if (parse_coords)
      tie(node_count, x_coords, y_coords) = parse_data(file_name);
    else
      node_count = parse_node_count(file_name);

    double* dist_table = load_dist_table(node_count, "data/dist_tables.h5", dataset);
    clock_t begin = clock();
    double total_dist;
    size_t* tour;
    tie(total_dist, tour) = guided_fast_local_search(node_count, dist_table, num_iter);
    clock_t end = clock();
    double elapsed = double(end - begin) / CLOCKS_PER_SEC;

    cout << "time passed: " << elapsed << endl;

    cout.precision(4);
    cout << fixed << total_dist << endl;
    for (size_t i = 0; i < node_count; i++) {
      cout << tour[i] << " ";
    }
    cout << endl;
    delete[] tour;
    delete[] x_coords;
    delete[] y_coords;
  }
  return 0;
}
