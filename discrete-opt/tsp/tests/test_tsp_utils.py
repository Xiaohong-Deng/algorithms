#!/usr/bin/python
# -*- coding: utf-8 -*-
import numpy as np
import os
import inspect
from tsp_utils import gen_dist_table, load_dist_table, Point

currentdir = os.path.dirname(os.path.abspath(inspect.getfile(inspect.currentframe())))
parentdir = os.path.dirname(currentdir)
# sys.path.insert(0, parentdir)


FILE_PATH = parentdir + '/data/tsp_5_1'
NODE_COUNT = 5
COORDS_1 = [Point(0.0, 0.0), Point(0.0, 0.5), Point(0.0, 1.0), Point(1.0, 0.0), Point(1.0, 1.0)]
COORDS_2 = np.array([[0, 0], [0, 0.5], [0, 1], [1, 0], [1, 1]])


class TestTSPUtils:
    def test_gen_dist_table(self):
        table_self_def = gen_dist_table(NODE_COUNT, COORDS_1, 0)
        table_cdist = gen_dist_table(NODE_COUNT, COORDS_2, 2)

        row_1 = len(table_self_def)
        col_1 = len(table_self_def[2])
        row_2, col_2 = table_cdist.shape

        assert row_1 == row_2
        assert col_1 == col_2

        for i in range(NODE_COUNT):
            for j in range(NODE_COUNT):
                assert table_self_def[i][j] == table_cdist[i, j]
                if i == j:
                    assert table_self_def[i][j] == 0.0

    def test_load_dist_table(self):
        table_path = parentdir + '/data/dist_table_5_1.csv'
        assert not os.path.isfile(table_path)

        table_1 = load_dist_table(FILE_PATH, NODE_COUNT, COORDS_2)

        assert os.path.isfile(table_path)

        table_2 = load_dist_table(FILE_PATH, NODE_COUNT, COORDS_2)

        assert table_1.shape == table_2.shape

        os.remove(table_path)
