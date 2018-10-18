#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import timeit
from random import shuffle
from tsp_utils import Node, parse_data, load_dist_table, distance, square_to_condensed

"""
#3 200 nodes
#4 574 nodes
#5 1889 nodes
#6 33810 nodes
"""


def probe_and_insert(seq_start, point, dist_table):
    """
    point is an int
    this is a method relies on look-up table entirely
    no distance computing
    """
    min_increment = sys.float_info.max
    cur_node = seq_start

    cur_u = None
    cur_v = None

    while cur_node.next is not None:
        u = cur_node
        v = cur_node.next

        pu = u.value
        pv = v.value

        uv = dist_table[pu, pv]
        ui = dist_table[pu, point]
        vi = dist_table[point, pv]

        increment = ui + vi - uv

        if increment < min_increment:
            min_increment = increment
            cur_u = u
            cur_v = v

        cur_node = v

    u = cur_node
    v = seq_start

    pu = u.value
    pv = v.value

    uv = dist_table[pu, pv]
    ui = dist_table[pu, point]
    vi = dist_table[point, pv]

    increment = ui + vi - uv

    if increment < min_increment:
        min_increment = increment
        cur_u = u
        cur_v = v

    if cur_u.next is None:
        cur_v = None

    cur_u.next = Node(point, cur_v)

    return min_increment


def probe_and_insert_no_cache(seq_start, point, coords):
    """
    point is an int
    """
    min_increment = sys.float_info.max
    cur_node = seq_start

    cur_u = None
    cur_v = None

    while cur_node.next is not None:
        u = cur_node
        v = cur_node.next

        pu = u.value
        pv = v.value

        uv = distance(coords[pu], coords[pv])
        ui = distance(coords[pu], coords[point])
        vi = distance(coords[point], coords[pv])

        increment = ui + vi - uv

        if increment < min_increment:
            min_increment = increment
            cur_u = u
            cur_v = v

        cur_node = v

    u = cur_node
    v = seq_start

    pu = u.value
    pv = v.value

    uv = distance(coords[pu], coords[pv])
    ui = distance(coords[pu], coords[point])
    vi = distance(coords[point], coords[pv])

    increment = ui + vi - uv

    if increment < min_increment:
        min_increment = increment
        cur_u = u
        cur_v = v

    if cur_u.next is None:
        cur_v = None

    cur_u.next = Node(point, cur_v)

    return min_increment


def probe_and_insert_fly_cache(seq_start, point, coords):
    """
    point is an int, coords is a list of Points with coordinates as members
    this compute distance for once, cache those will be used again later
    """
    min_increment = sys.float_info.max
    cur_node = seq_start

    cur_u = None
    cur_v = None
    cur_ui = None
    cur_vi = None
    prev_vi = None

    while cur_node.next is not None:
        u = cur_node
        v = cur_node.next

        pu = u.value
        pv = v.value

        uv = u.dist_to_next
        ui = prev_vi if prev_vi is not None else distance(coords[pu], coords[point])
        vi = distance(coords[point], coords[pv])

        increment = ui + vi - uv

        if increment < min_increment:
            min_increment = increment
            cur_u = u
            cur_v = v
            cur_ui = ui
            cur_vi = vi

        cur_node = v
        prev_vi = vi

    # last node in the sequence
    u = cur_node
    v = seq_start

    pu = u.value
    pv = v.value

    uv = u.dist_to_next
    ui = prev_vi if prev_vi is not None else distance(coords[pu], coords[point])
    vi = distance(coords[point], coords[pv])

    increment = ui + vi - uv

    if increment < min_increment:
        min_increment = increment
        cur_u = u
        cur_v = v
        cur_ui = ui
        cur_vi = vi

    if cur_u.next is None:
        cur_v = None

    # insertion here
    cur_u.dist_to_next = cur_ui
    cur_u.next = Node(point, cur_v, cur_vi)

    return min_increment


def probe_and_insert_dist_array(node_count, seq_start, point, dist_array):
    """
    use condensed matrix as dist_table, compute index each time
    """
    min_increment = sys.float_info.max
    cur_node = seq_start

    cur_u = None
    cur_v = None

    while cur_node.next is not None:
        u = cur_node
        v = cur_node.next

        pu = u.value
        pv = v.value

        # assume pu != pv in the while loop
        idx_uv = square_to_condensed(pu, pv, node_count)
        idx_ui = square_to_condensed(pu, point, node_count)
        idx_vi = square_to_condensed(point, pv, node_count)

        uv = dist_array[idx_uv]
        ui = dist_array[idx_ui]
        vi = dist_array[idx_vi]

        increment = ui + vi - uv

        if increment < min_increment:
            min_increment = increment
            cur_u = u
            cur_v = v

        cur_node = v

    u = cur_node
    v = seq_start

    pu = u.value
    pv = v.value

    if pu == pv:
        uv = 0
    else:
        idx_uv = square_to_condensed(pu, pv, node_count)
        uv = dist_array[idx_uv]

    idx_ui = square_to_condensed(pu, point, node_count)
    idx_vi = square_to_condensed(point, pv, node_count)

    ui = dist_array[idx_ui]
    vi = dist_array[idx_vi]

    increment = ui + vi - uv

    if increment < min_increment:
        min_increment = increment
        cur_u = u
        cur_v = v

    if cur_u.next is None:
        cur_v = None

    cur_u.next = Node(point, cur_v)

    return min_increment


def random_greedy(node_count, dist_table_coords, num_iter=5, mode=0):
    """
    dist_table_coords: a dist_table or coords
    loop over n - 1 nodes for n - 1 times, each time the number of nodes
    reduces by 1

    ideally the running time should be O(n*(n-1)/2). to achieve that we can
    use deque. Plus O(n) deque construction. Insertion may incur O(n*(n-1)/2)
    running time

    """
    points = [x for x in range(node_count)]
    best_seq = None
    best_dist = sys.float_info.max

    if mode == 0:
        for i in range(num_iter):
            shuffle(points)

            total_dist = 0
            seq_start = Node(points[0], dist_to_next=0)

            for p in points[1:]:
                total_dist += probe_and_insert_fly_cache(seq_start, p, dist_table_coords)

            if total_dist < best_dist:
                best_seq = seq_start
                best_dist = total_dist
    elif mode == 1:
        for i in range(num_iter):
            shuffle(points)

            total_dist = 0
            seq_start = Node(points[0])

            for p in points[1:]:
                total_dist += probe_and_insert(seq_start, p, dist_table_coords)

            if total_dist < best_dist:
                best_seq = seq_start
                best_dist = total_dist
    elif mode == 2:
        for i in range(num_iter):
            shuffle(points)

            total_dist = 0
            seq_start = Node(points[0])

            for p in points[1:]:
                total_dist += probe_and_insert_dist_array(node_count, seq_start, p, dist_table_coords)

            if total_dist < best_dist:
                best_seq = seq_start
                best_dist = total_dist
    elif mode == 3:
        for i in range(num_iter):
            shuffle(points)

            total_dist = 0
            seq_start = Node(points[0])

            for p in points[1:]:
                total_dist += probe_and_insert_no_cache(seq_start, p, dist_table_coords)

            if total_dist < best_dist:
                best_seq = seq_start
                best_dist = total_dist
    else:
        print("need mode as an integer ranged from 0 to 3 inclusive")

    cur_node = best_seq
    solution = [best_seq.value]

    while cur_node.next is not None:
        solution.append(cur_node.next.value)
        cur_node = cur_node.next

    return solution, best_dist


def solve_it(input_data, file_location, mode=1):
    """
    mode 0: fly cache, use minimum memory at decent speed, good for large datasets around tens of thousands
    mode 1: load dist_table, fastest and take max memory, good for small datasets up to a couple thousand nodes before memory explosion
    mode 2: load dist_array, use half of the size as mode 1. don't know how large it can handle, larger than mode 1 is for sure
    mode 3: no cache, slowest with least memory use, good for small dataset but workable for all datasets
    """
    node_count, coords = parse_data(input_data, mode)
    # if this program is run as a subprocess load_dist_table might create a race
    # because it might try to write a file while other program is trying to write to
    # the same file. make sure to check the existence of the file in the parent thread
    if mode == 1 or mode == 2:
        dist_table = load_dist_table(file_location, node_count, coords, mode)
    # sequence of points
    if mode == 0 or mode == 3:
        solution, total_dist = random_greedy(node_count, coords, 1, mode)
    elif mode == 1 or mode == 2:
        if node_count > 1000:
            num_iter = 100
        else:
            num_iter = 4000

        solution, total_dist = random_greedy(node_count, dist_table, num_iter, mode)

    output_data = '%.2f' % total_dist + ' ' + str(0) + '\n'
    output_data += ' '.join(map(str, solution))

    return output_data


if __name__ == "__main__":
    if len(sys.argv) > 2:
        file_location = sys.argv[1].strip()
        mode = int(sys.argv[2].strip())
        with open(file_location, 'r') as input_data_file:
            input_data = input_data_file.read()
        print(solve_it(input_data, file_location, mode))
    else:
        print('This test requires an input file.  Please select one from the data directory. (i.e. python solver.py ./data/tsp_51_1)')
