import os
import sys
import timeit
from io import StringIO
from subprocess import Popen, PIPE
from multiprocessing.pool import ThreadPool
from tsp_utils import parse_data, load_dist_table


def run_prog(file_name, prog, mode):
    if prog == './ls.out':
        process = Popen([prog, file_name], stdout=PIPE)
    else:
        process = Popen(['python', prog, file_name, mode], stdout=PIPE)
    (stdout, stderr) = process.communicate()

    return stdout.strip()


def solve_it(file_name, threads=4):
    pool = ThreadPool(threads)
    results = []

    with open(file_name, 'r') as input_data_file:
        input_data = input_data_file.read()

    fn_parts = file_name.split('_')
    dist_table_name = 'data/dist_table_' + fn_parts[1] + '_' + fn_parts[2] + '.csv'

    node_count = int(fn_parts[1])

    if node_count > 2500:
        mode = 0
    else:
        mode = 1

    if mode == 1 and not os.path.isfile(dist_table_name):
        node_count, coords = parse_data(input_data, mode)
        _ = load_dist_table(file_name, node_count, coords, mode)

    for i in range(threads):
        results.append(pool.apply_async(run_prog, args=(file_name, 'greedy.py', str(mode))))

    pool.close()
    pool.join()
    results = [r.get().decode('utf-8') for r in results]

    opt_dist = sys.float_info.max
    output_data = 'no sol'

    for i in results:
        buf = StringIO(i)
        all_lines = buf.readlines()
        dist_and_proof = all_lines[0].split()
        cur_dist = float(dist_and_proof[0])
        if cur_dist < opt_dist:
            output_data = i
            opt_dist = cur_dist

    return output_data


if __name__ == '__main__':
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        start_time = timeit.default_timer()
        print(solve_it(file_location))
        print("time passed:", timeit.default_timer() - start_time)
    else:
        print('This test requires an input file.  Please select one from the data directory.')
