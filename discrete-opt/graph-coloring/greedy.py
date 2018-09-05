#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import collections
import random

"""
As required by grader, returned color list must be in the ascending order of the nodes
i.e., color of node 0, color of node 1, ..., color of node 2
If the color list indices don't represent nodes, it's hard to check the correctness
of the solution against the graph
colors are ints
"""


def node_to_neighbors(edges):
    node_to_neighbors = collections.defaultdict(list)
    for edge in edges:
        node_to_neighbors[edge[0]].append(edge[1])
        node_to_neighbors[edge[1]].append(edge[0])

    return node_to_neighbors


def order_nodes_descending_in_degree(node_count, edges, neighbors=None):
    node_degrees = []
    if neighbors is None:
        neighbors = node_to_neighbors(edges)

    for key, value in neighbors.items():
        node_degrees.append((key, len(value)))

    node_degrees.sort(key=lambda x: x[1], reverse=True)
    nodes_descending_in_degree = [x[0] for x in node_degrees]
    return nodes_descending_in_degree


def naive_greedy(node_count, edge_count, edges, nodes=None, neighbors=None):
    if nodes is None:
        nodes = range(node_count)
    if neighbors is None:
        neighbors = node_to_neighbors(edges)

    colors = range(node_count)
    node_colors = [-1 for x in colors]
    neighbor_colors = collections.defaultdict(set)

    for n in nodes:
        for c in colors:
            # loop over the neighbor colors
            # O(n*c*num(neighbor_colors))
            if c not in neighbor_colors[n]:
                node_colors[n] = c
                # loop over the neighbors
                # O(n*num(neighbors))
                for neighbor in neighbors[n]:
                    neighbor_colors[neighbor].add(c)
                break

    return node_colors


def naive_greedy_alternative(node_count, edge_count, edges, nodes=None, neighbors=None):
    """
    Output
    ------
    node_colors: a list of node colors where index i of the list represents node i
    """
    if nodes is None:
        nodes = range(node_count)
    if neighbors is None:
        neighbors = node_to_neighbors(edges)

    colors = range(node_count)
    # -1 is better than 0 in some orderings
    # -1 means all node are unassigned
    # 0 means all node are assigned 0
    node_colors = [-1 for x in colors]
    for node in nodes:
        my_neighbors = neighbors[node]
        # try to assign smallest color code possible
        # do we need to use neighbor_colors more than once?
        # loop over the neighbors, O(n*num(neighbor))
        neighbor_colors = set([node_colors[n] for n in my_neighbors])
        for c in colors:
            # loop over the neighbor colors
            if c not in neighbor_colors:
                node_colors[node] = c
                break

    # a list where the indices of the list corresponds to the nodes
    # in which case the order is ascending by nature
    # and the elements are the colors assigned to the corresponding nodes

    return node_colors


def random_greedy(node_count, edge_count, edges, num_shuffle=100):
    solution = None
    num_color = node_count
    nodes = [x for x in range(node_count)]
    for i in range(num_shuffle):
        random.shuffle(nodes)
        temp_sol = apply_naive_greedy(node_count, edge_count, edges,
                                      nodes=nodes, descending_sort_in_degree=False)
        temp_num_color = len(set(temp_sol))
        if temp_num_color < num_color:
            solution = temp_sol
            num_color = temp_num_color

    return solution


def simplified_iterated_greedy():
    pass


def apply_naive_greedy(node_count, edge_count, edges, nodes=None, descending_sort_in_degree=True):
    neighbors = node_to_neighbors(edges)
    if descending_sort_in_degree:
        # list
        nodes = order_nodes_descending_in_degree(edges, node_count, neighbors=neighbors)
    elif nodes is None:
        # iterator
        nodes = range(node_count)

    return naive_greedy_alternative(node_count, edge_count,
                                    edges, nodes=nodes,
                                    neighbors=neighbors)


def solve_it(input_data, mode='naive_greedy', descending_sort_in_degree=True):
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

    if mode == 'naive_greedy':
        solution = apply_naive_greedy(node_count, edge_count, edges,
                                      descending_sort_in_degree=descending_sort_in_degree)
    elif mode == 'random_greedy':
        solution = random_greedy(node_count, edge_count, edges)
    # prepare the solution in the specified output format
    output_data = str(node_count) + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, solution))

    return output_data


if __name__ == '__main__':
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data, mode='random_greedy', descending_sort_in_degree=False))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/gc_4_1)')
