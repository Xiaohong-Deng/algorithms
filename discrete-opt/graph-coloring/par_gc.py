import sys
import timeit
from io import StringIO
from subprocess import Popen, PIPE
from multiprocessing.pool import ThreadPool


def run_prog(file_name, mode, prog):
    if prog == './ls.out':
        process = Popen([prog, file_name, mode], stdout=PIPE)
    else:
        process = Popen(['python', prog, file_name, mode], stdout=PIPE)
    (stdout, stderr) = process.communicate()

    return stdout.strip()


def solve_it(file_name, mode=1, threads=4):
    pool = ThreadPool(threads)
    results = []

    with open(file_name, 'r') as input_data_file:
        input_data = input_data_file.read()

    buf = StringIO(input_data)
    first_line = buf.readline().split()
    node_count = int(first_line[0])

    if node_count == 1000:
        for i in range(threads):
            results.append(pool.apply_async(run_prog, args=(file_name, str(mode), './ls.out')))
    else:
        for i in range(threads):
            results.append(pool.apply_async(run_prog, args=(file_name, str(2), 'greedy.py')))

    pool.close()
    pool.join()
    results = [r.get().decode('utf-8') for r in results]

    opt_num_colors = sys.maxsize
    output_data = 'no sol'

    for i in results:
        buf = StringIO(i)
        all_lines = buf.readlines()
        if node_count == 1000:
            colors_and_proof = all_lines[4].split()
        else:
            colors_and_proof = all_lines[2].split()
        cur_num_colors = int(colors_and_proof[0])
        if cur_num_colors < opt_num_colors:
            if node_count == 1000:
                output_data = all_lines[4] + all_lines[5]
            else:
                output_data = all_lines[2] + all_lines[3]
            opt_num_colors = cur_num_colors

    # if node_count == 1000:
    #     for i in results:
    #         buf = StringIO(i)
    #         all_lines = buf.readlines()
    #         colors_and_proof = all_lines[4].split()
    #         cur_num_colors = int(colors_and_proof[0])
    #         if cur_num_colors < opt_num_colors:
    #             output_data = all_lines[4] + all_lines[5]
    #             opt_num_colors = cur_num_colors
    # else:
    #     for i in results:
    #         buf = StringIO(i)
    #         all_lines = buf.readlines()
    #         colors_and_proof = all_lines[2].split()
    #         cur_num_colors = int(colors_and_proof[0])
    #         if cur_num_colors < opt_num_colors:
    #             output_data = all_lines[2] + all_lines[3]
    #             opt_num_colors = cur_num_colors

    return output_data


if __name__ == '__main__':
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        start_time = timeit.default_timer()
        print(solve_it(file_location))
        print("time passed:", timeit.default_timer() - start_time)
    else:
        print('This test requires an input file.  Please select one from the data directory.')
