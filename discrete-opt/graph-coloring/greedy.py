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
    """
    Output
    ------
    node_to_neighbors: default dictionary where keys are nodes and values are lists of neighbor nodes of the keys
    """
    node_to_neighbors = collections.defaultdict(list)
    for edge in edges:
        node_to_neighbors[edge[0]].append(edge[1])
        node_to_neighbors[edge[1]].append(edge[0])

    return node_to_neighbors


def order_nodes_descending_in_degree(neighbors):
    node_degrees = []

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


def apply_naive_greedy(node_count, edge_count, edges,
                       nodes=None, node_colors=None, neighbors=None,
                       descending_sort_in_degree=True):
    if neighbors is None:
        neighbors = node_to_neighbors(edges)

    if descending_sort_in_degree:
        # list
        nodes = order_nodes_descending_in_degree(neighbors)
    elif nodes is None:
        # iterator
        nodes = range(node_count)

    return naive_greedy_alternative(node_count, edge_count,
                                    edges, nodes=nodes,
                                    neighbors=neighbors)


def random_greedy(node_count, edge_count, edges, num_iter=100):
    """
    in each iteration we unassign all the node colors
    if you don't, while you are iterating through the nodes, you are not updating anything
    """
    solution = None
    num_colors = node_count
    nodes = [x for x in range(node_count)]
    for i in range(num_iter):
        random.shuffle(nodes)
        temp_sol = apply_naive_greedy(node_count, edge_count, edges,
                                      nodes=nodes, descending_sort_in_degree=False)
        temp_num_colors = len(set(temp_sol))
        if temp_num_colors < num_colors:
            solution = temp_sol
            num_colors = temp_num_colors

    return solution


def random_greedy_with_color(node_count, edge_count, edges, num_iter=1000):
    """
    Run apply_naive_greedy with default settings for the 1st time
    then pick node groups by color randomly. i.e., all nodes colored by 8, all nodes colored by 3, ...
    we shuffle each of the groups
    we sort each group by degree
    we concatenate all the groups to form a newly ordered node list
    in the following iterations we have two cases based on the principle that we update only on
    better solutions
    case 1:
        temp_sol is not better than solution, we work on the same solution in the next iteration
        only the order of picking colors will be different
    case 2:
        temp_sol is better than solution, work work on new solution in the next iteration
        colors and node colors are different
    we can also work on the solution generated in the current iteration. Even if colors is the same
    node colors will be different each time. So we need to maintain last_sol and last_colors variables
    in case they didn't update the global best.
    """
    nodes = [x for x in range(node_count)]
    neighbors = node_to_neighbors(edges)
    solution = apply_naive_greedy(node_count, edge_count, edges,
                                  nodes=nodes, neighbors=neighbors,
                                  descending_sort_in_degree=True)
    colors = list(set(solution))
    num_colors = len(colors)
    updated = True
    # group nodes by color
    color_to_nodes = collections.defaultdict(list)

    for i in range(num_iter):
        # if i % 50 == 0:
            # print(num_colors)
        nodes = []
        random.shuffle(colors)

        # get nodes in new order
        if updated:
            color_to_nodes = collections.defaultdict(list)

            for idx, node_color in enumerate(solution):
                color_to_nodes[node_color].append(idx)
            for c in colors:
                nodes_by_color = color_to_nodes[c]
                # we need a subset of neighbors corresponds to nodes_by_color
                # to feed order_nodes_descending_in_degree()
                neighbors_by_color = collections.defaultdict()
                for n in nodes_by_color:
                    neighbors_by_color[n] = neighbors[n]
                # we should store ordered subsets in color_to_nodes for future uses
                color_to_nodes[c] = order_nodes_descending_in_degree(neighbors_by_color)
                nodes.extend(color_to_nodes[c])
        else:
            for c in colors:
                # already sorted
                nodes.extend(color_to_nodes[c])

        temp_sol = apply_naive_greedy(node_count, edge_count, edges,
                                      nodes=nodes, neighbors=neighbors,
                                      descending_sort_in_degree=False)
        temp_colors = set(temp_sol)
        temp_num_colors = len(temp_colors)
        # if temp_sol is not better than solution we work on solution in the next iteration
        # only order of picking colors is different.
        # in such case we can inherit color_to_nodes, sorted node groups and neighbors_by_color
        if temp_num_colors < num_colors:
            solution = temp_sol
            num_colors = temp_num_colors
            colors = list(temp_colors)
            updated = True
        else:
            updated = False

    # print(num_colors)
    return solution


def random_greedy_with_color_alternative(node_count, edge_count, edges, num_iter=3000):
    """
    try to work on solutions generated in the last iteration each time
    """
    nodes = [x for x in range(node_count)]
    neighbors = node_to_neighbors(edges)
    solution = apply_naive_greedy(node_count, edge_count, edges,
                                  nodes=nodes, neighbors=neighbors,
                                  descending_sort_in_degree=True)

    running_sol = solution
    running_colors = list(set(solution))
    num_colors = len(running_colors)

    # group nodes by color
    color_to_nodes = collections.defaultdict(list)

    # even if num_colors doesn't change, solution may change, so is color_to_nodes
    for i in range(num_iter):
        # if i % 50 == 0:
            # print(num_colors)
            # print(len(running_colors))
            # print()
        nodes = []
        random.shuffle(running_colors)

        color_to_nodes = collections.defaultdict(list)

        for idx, node_color in enumerate(running_sol):
            color_to_nodes[node_color].append(idx)
        for c in running_colors:
            nodes_by_color = color_to_nodes[c]
            # we need a subset of neighbors corresponds to nodes_by_color
            # to feed order_nodes_descending_in_degree()
            neighbors_by_color = collections.defaultdict()
            for n in nodes_by_color:
                neighbors_by_color[n] = neighbors[n]
            # we should store ordered subsets in color_to_nodes for future uses
            color_to_nodes[c] = order_nodes_descending_in_degree(neighbors_by_color)
            nodes.extend(color_to_nodes[c])

        running_sol = apply_naive_greedy(node_count, edge_count, edges,
                                         nodes=nodes, neighbors=neighbors,
                                         descending_sort_in_degree=False)
        running_colors = list(set(running_sol))
        temp_num_colors = len(running_colors)

        if temp_num_colors < num_colors:
            solution = running_sol
            num_colors = len(running_colors)

    return solution


def simplified_iterated_greedy():
    pass


def solve_it(input_data, mode=0, descending_sort_in_degree=True):
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

    if mode == 0:  # 'naive_greedy'
        print("naive greedy mode")
        solution = apply_naive_greedy(node_count, edge_count, edges,
                                      descending_sort_in_degree=descending_sort_in_degree)
    elif mode == 1:  # 'random_greedy'
        print("random greedy mode")
        solution = random_greedy(node_count, edge_count, edges)
    elif mode == 2:  # 'random_greedy_with_color'
        print("random_greedy_with_color mode")
        solution = random_greedy_with_color_alternative(node_count, edge_count, edges)
    # prepare the solution in the specified output format
    output_data = str(len(set(solution))) + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, solution))

    return output_data


if __name__ == '__main__':
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        mode = int(sys.argv[2].strip()) if sys.argv[2].strip() > 0 else 0
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data, mode=mode, descending_sort_in_degree=False))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/gc_4_1)')
