#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import timeit
import numpy as np
from math import sqrt
from collections import namedtuple, defaultdict
from scipy.spatial.distance import pdist, cdist

Point = namedtuple("Point", ['x', 'y'])


class Node:
    def __init__(self, value=None, next=None, dist_to_next=None):
        self.value = value
        self.next = next
        self.dist_to_next = dist_to_next


def distance(p1, p2):
    return sqrt((p1.x - p2.x)**2 + (p1.y - p2.y)**2)


def dist_table(node_count, coords):
    table = defaultdict(list)

    for u in range(node_count):
        for p in range(u):
            table[u].append(table[p][u])
        for v in range(u, node_count):
            dist = distance(coords[u], coords[v])
            table[u].append(dist)

    return table


def line_to_coords(line):
    coords = line.split()
    return [float(coords[0]), float(coords[1])]


def line_to_coord_points(line):
    coords = line.split()
    return Point(float(coords[0]), float(coords[1]))


def parse_data(input_data, mode=2):
    """
    mode 0: store coordinates as list of Points
    mode 1: store coordinates as n by 2 matrix
    points: list of Points with coords as members
    """
    lines = input_data.split('\n')

    node_count = int(lines[0])
    if mode == 0 or mode == 3:
        points = list(map(line_to_coord_points, lines[1:node_count + 1]))
    elif mode == 1 or mode == 2:
        points = np.array(list(map(line_to_coords, lines[1:node_count + 1])))
    else:
        print("mode must be in 0 or 1")
        return

    return node_count, points


def square_to_condensed(i, j, n):
    if i < j:
        i, j = j, i
    return n * j - ((j * (j + 1)) >> 1) + i - 1 - j


def load_dist_table(file_name, node_count, coords, mode=2):
    fn_parts = file_name.split('_')
    table_file_name = 'data/dist_table_' + fn_parts[1] + '_' + fn_parts[2] + '.csv'

    try:
        table = np.genfromtxt(table_file_name, delimiter=',')
        # print(table.shape)
    except IOError:
        table = gen_dist_table(node_count, coords, mode)
        np.savetxt(table_file_name, table, delimiter=',')

    return table


def gen_dist_table(node_count, coords, mode=2):
    """
    Output
    ---------
    0 gives default dictionary
    1 and 2 give numpy array
    """
    if mode == 0:
        table = dist_table(node_count, coords)
    elif mode == 1:
        table = cdist(coords, coords)
    elif mode == 2:
        table = pdist(coords)
        # table = squareform(table)
    else:
        print("mode must be 0, 1 or 2")
        return

    return table


if __name__ == '__main__':
    if len(sys.argv) > 2:
        file_location = sys.argv[1].strip()
        mode = int(sys.argv[2].strip())
        with open(file_location, 'r') as f:
            input_data = f.read()
        node_count, coords = parse_data(input_data, mode)
        print("testing mode:", mode)
        start = timeit.default_timer()
        for i in range(1):
            gen_dist_table(node_count, coords, mode)
        print("time passed: ", timeit.default_timer() - start)
    else:
        print("need two arguments: file path and working mode")
