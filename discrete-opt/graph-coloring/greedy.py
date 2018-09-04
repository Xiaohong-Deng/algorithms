#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import collections


def order_nodes_descending_in_degree(node_count, edges):
    node_to_neighbors = dict()
    node_degrees = []
    for edge in edges:
        key_a = edge[0]
        key_b = edge[1]
        if node_to_neighbors.get(key_a) is None:
            node_to_neighbors[key_a] = [key_b]
        else:
            node_to_neighbors[key_a].append(key_b)
        if node_to_neighbors.get(key_b) is None:
            node_to_neighbors[key_b] = [key_a]
        else:
            node_to_neighbors[key_b].append(key_a)

    for key, value in node_to_neighbors.items():
        node_degrees.append((key, len(value)))

    node_degrees.sort(key=lambda x: x[1], reverse=True)
    nodes_descending_in_degree = [x[0] for x in node_degrees]
    return nodes_descending_in_degree, node_to_neighbors


def naive_greedy(node_count, edge_count, edges, nodes=None):
    if nodes is None:
        nodes = range(node_count)
    colors = range(node_count)
    # node_color[node_n] = color_k
    node_color = []
    neighbor_colors = collections.defaultdict(set())
    neighbors = collections.defaultdict(set())

    for n in nodes:
        for c in colors:
            if c not in neighbor_colors[n]:
                node_color.append = c
                for neighbor in neighbors[n]:
                    neighbor_colors[neighbor].add(c)
                break

    output_data = str(node_count) + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, node_color))

    return output_data


def random_greedy():
    pass


def simplified_iterated_greedy():
    pass


def solve_it(input_data):
    # Modify this code to run your optimization algorithm

    # parse the input
    lines = input_data.split('\n')

    first_line = lines[0].split()
    node_count = int(first_line[0])
    edge_count = int(first_line[1])

    edges = []
    for i in range(1, edge_count + 1):
        line = lines[i]
        parts = line.split()
        edges.append((int(parts[0]), int(parts[1])))

    nodes_descending_in_degree, node_to_neighbors = \
        order_nodes_descending_in_degree(edges, node_count)

    node_to_color = {x: 0 for x in range(node_count)}
    for node in nodes_descending_in_degree:
        neighbors = node_to_neighbors[node]
        for neighbor in neighbors:
            if node_to_color[neighbor] == node_to_color[node]:
                node_to_color[node] = node_to_color[node] + 1

    solution = [val for key, val in node_to_color.items()]
    # build a trivial solution
    # every node has its own color
    # solution = range(0, node_count)

    # prepare the solution in the specified output format
    output_data = str(node_count) + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, solution))

    return output_data


if __name__ == '__main__':
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/gc_4_1)')
