import glob
import h5py
import os
from tsp_utils import parse_data, gen_dist_table


def table_names():
    fp = h5py.File('./data/dist_tables.h5', 'r')
    for k in list(fp.keys()):
        print(k)
    fp.close()


def main():
    file_pat = './data/tsp_*_1'
    data_files = glob.glob(file_pat)

    fn = './data/dist_tables.h5'
    fp = h5py.File(fn, 'w')

    for f in data_files:
        with open(f, 'r') as fh:
            input_data = fh.read()
        node_count, coord_mat = parse_data(input_data)
        condensed_array = gen_dist_table(node_count, coord_mat)
        fp.create_dataset(os.path.basename(f), data=condensed_array, dtype='float32')

    fp.close()


if __name__ == '__main__':
    main()
    table_names()
