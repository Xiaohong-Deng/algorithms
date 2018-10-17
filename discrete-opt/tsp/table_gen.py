import glob
import h5py
import os
from tsp_utils import parse_data, gen_dist_table


def table_names():
    with h5py.File('./data/dist_tables.h5', 'r') as fp:
        for k in list(fp.keys()):
            print(k)


def append_dataset(ds, fn):
    with h5py.File(fn, 'a') as fp:
        with open(ds, 'r') as dsp:
            input_data = dsp.read()
        node_count, coord_mat = parse_data(input_data)
        condensed_array = gen_dist_table(node_count, coord_mat)
        fp.create_dataset(os.path.basename(ds), data=condensed_array, dtype='float64')


def main():
    file_pat = './data/tsp_*_*'
    data_files = glob.glob(file_pat)

    fn = './data/dist_tables.h5'
    fp = h5py.File(fn, 'w')

    for f in data_files:
        with open(f, 'r') as fh:
            input_data = fh.read()
        node_count, coord_mat = parse_data(input_data)
        condensed_array = gen_dist_table(node_count, coord_mat)
        fp.create_dataset(os.path.basename(f), data=condensed_array, dtype='float64')

    fp.close()


if __name__ == '__main__':
    main()
    # append_dataset('./data/tsp_100_3', './data/dist_tables.h5')
    # append_dataset('./data/tsp_200_2', './data/dist_tables.h5')
    table_names()
