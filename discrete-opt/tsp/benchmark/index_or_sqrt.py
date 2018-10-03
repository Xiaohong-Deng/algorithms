from functools import partial
import h5py
import inspect
import math
import numpy as np
import os
import timeit

currentdir = os.path.dirname(os.path.abspath(inspect.getfile(inspect.currentframe())))
parentdir = os.path.dirname(currentdir)
file_name = parentdir + '/data/dist_tables.h5'


def distance(x1, x2, y1, y2):
    return math.sqrt((x1 - x2)**2 + (y1 - y2)**2)


def square_to_condensed(i, j, n):
    if i < j:
        i, j = j, i
    return n * j - ((j * (j + 1)) >> 1) + i - 1 - j


def load_dist_table(file_name, table_name):
    fp = h5py.File(file_name, 'r')
    return fp[table_name][:]


def test_sqrt():
    coords = np.random.uniform(100, 3000, 4)
    x1, x2, y1, y2 = coords
    _ = distance(x1, x2, y1, y2)


def test_condensed(array):
    row, col = np.random.randint(0, 33809, 2)
    _ = array[square_to_condensed(row, col, 33810)]


def test_2d(array):
    row, col = np.random.randint(0, 33809, 2)
    _ = array[row, col]


def main():
    dist_table = load_dist_table(file_name, 'tsp_33810_1')
    table_1d = load_dist_table(file_name, 'tsp_33810_1_1d')

    print(timeit.Timer(test_sqrt).repeat())
    print(timeit.Timer(partial(test_condensed, table_1d)).repeat())
    print(timeit.Timer(partial(test_2d, dist_table)).repeat())


if __name__ == '__main__':
    main()
