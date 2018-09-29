#include "local_search.h"

unsigned int condensed_index(unsigned int i, unsigned int j, unsigned int n) {
  if (i < j) {
    unsigned int temp = j;
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
float* load_dist_table(const size_t node_count, string file_name, string ds_name) {
  float* dist_table;
  dist_table = new float[node_count * node_count];

  H5File file(file_name, H5F_ACC_RDONLY);
  DataSet dataset = file.openDataSet(ds_name);
  DataSpace dataspace = dataset.getSpace();
  hsize_t dims_out[1];
  int rank = dataspace.getSimpleExtentDims(dims_out, NULL);
  cout << "rank " << rank << ", dimensions " <<
      (unsigned long)(dims_out[0]) << " x " <<
      (unsigned long)(dims_out[1]) << endl;
  DataSpace memspace(1, dims_out);
  dataset.read(dist_table, PredType::NATIVE_FLOAT, memspace, dataspace);

  return dist_table;
}

tuple<double, int*> local_search(size_t node_count, const double x_coords[], const double y_coords[]) {
  double distance;
  int* predecessors = new int[node_count];
  int* successors = new int[node_count];
  int* tour = new int[node_count];

  for (size_t i = 0; i < node_count; i++) {
    tour[i] = i;
  }

  random_device ran_dev;
  srand(ran_dev());

  random_shuffle(&tour[0], &tour[node_count - 1]);

  successors[tour[0]] = tour[1];
  predecessors[tour[0]] = tour[node_count - 1];

  successors[tour[node_count - 1]] = tour[0];
  predecessors[tour[node_count - 1]] = tour[node_count - 2];

  for (size_t i = 1; i < node_count - 1; i++) {
    int node = tour[i];
    successors[node] = tour[i + 1];
    predecessors[node] = tour[i - 1];
  }

  return make_tuple(distance, tour);
}

int main(int argc, char const *argv[]) {
  if (argc > 1) {
    string file_name = argv[1];
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
    clock_t begin = clock();
    float* dist_table = load_dist_table(node_count, "data/dist_tables.h5", dataset);
    clock_t end = clock();
    double elapsed = double(end - begin) / CLOCKS_PER_SEC;

    cout << "time passed: " << elapsed << endl;

    // for (size_t i = 0; i < 51; i++) {
    //   for (size_t j = 0; j < 51; j++) {
    //     cout << dist_table[i*51 + j] << " ";
    //   }
    //   cout << endl;
    // }

  }
  return 0;
}
