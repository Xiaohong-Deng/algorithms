#!/usr/bin/env python
# inference.py
# Base code by George H. Chen (georgehc@mit.edu) -- updated 10/18/2016
import collections
import sys

import graphics
import numpy as np
import robot


# Throughout the code, we use these variables.
# Do NOT change these (but you'll need to use them!):
# - all_possible_hidden_states: a list of possible hidden states
# - all_possible_observed_states: a list of possible observed states
# - prior_distribution: a distribution over states
# - transition_model: a function that takes a hidden state and returns a
#     Distribution for the next state
# - observation_model: a function that takes a hidden state and returns a
#     Distribution for the observation from that hidden state
all_possible_hidden_states = robot.get_all_hidden_states()
all_possible_observed_states = robot.get_all_observed_states()
# note prior is a dict where each of its key is a triple tuple, its value
# is a real number
prior_distribution = robot.initial_distribution()
transition_model = robot.transition_model
observation_model = robot.observation_model


# You may find this function helpful for computing logs without yielding a
# NumPy warning when taking the log of 0.
def careful_log(x):
    # computes the log of a non-negative real number
    if x == 0:
        return -np.inf
    else:
        return np.log(x)


# -----------------------------------------------------------------------------
# Functions for you to implement
#

def forward_backward(observations):
    """
    Input
    -----
    observations: a list of observations, one per hidden state
        (a missing observation is encoded as None)

    Output
    ------
    A list of marginal distributions at each time step; each distribution
    should be encoded as a Distribution (see the Distribution class in
    robot.py and see how it is used in both robot.py and the function
    generate_data() above, and the i-th Distribution should correspond to time
    step i
    """

    # -------------------------------------------------------------------------
    # YOUR CODE GOES HERE
    #
    # observations give us all yi
    num_time_steps = len(observations)
    forward_messages = [None] * num_time_steps
    # forward_messages[0] represents message(alpha)0->1
    # forward_messages[n-1] represents alpha(n-1)->n
    forward_messages[0] = prior_distribution
    obs_for_steps = [None] * num_time_steps
    # n - 1 trans, one less than the states number
    # so leave trans_for_steps[0] None
    # trans_for_steps[1] is a distribution for m1->2
    # its current states are states at time step 1
    # its vals are dicts, in which keys are the possible
    # next states, vals are real numbers
    trans_for_steps = [None] * num_time_steps
    # TODO: Compute the forward messages
    # take n steps
    for index in range(1, num_time_steps):
        forward_messages[index] = robot.Distribution()
        transition_distributions = {}
        # yi's given different xi's
        obs = robot.Distribution()
        ob_i = observations[index - 1]
        next_states_map_current_states = {}
        # take k steps
        for current_state in forward_messages[index - 1]:
            # each val is a dict, the keys are triple tuples, indicate the
            # next states and their prob.
            transition_distributions[
                current_state] = transition_model(current_state)
            # take at most five steps, at least two steps
            for next_state in transition_distributions[current_state]:
                if next_states_map_current_states.get(next_state) is None:
                    next_states_map_current_states[
                        next_state] = {current_state}
                else:
                    next_states_map_current_states[
                        next_state].add(current_state)
            # dict, the keys are double tuples, indicate the obs and their
            # prob.
            observation_distribution = observation_model(current_state)
            # keys of observation_distribution are represented as 2D
            # cooradinates same as Yi
            #
            # obs[current_state] = P(yi|x_current)
            obs[current_state] = 1 if ob_i is None else \
                observation_distribution[ob_i]
        # obs_for_steps[n - 1] is None because at the last step
        # we compute alpha(n-1)->n, we use p(y(n-1)|x(n-1))
        obs_for_steps[index - 1] = obs
        trans_for_steps[index] = transition_distributions
        # take k steps
        for next_state in next_states_map_current_states:
            message = 0
            # take at most five steps, at least two steps
            for current_state in next_states_map_current_states[next_state]:
                pre_message = forward_messages[index - 1][current_state]
                ob = obs[current_state]
                tran = transition_distributions[current_state][next_state]
                message += pre_message * ob * tran
            forward_messages[index][next_state] = message

    backward_messages = [None] * num_time_steps
    # backward_messages[n-1] represents message(beta)(n+1)->n
    # backward_messages[0] represents beta2->1
    beta_n_plus_one = robot.Distribution()
    obs_for_step_n = robot.Distribution()
    ob_for_step_n = observations[num_time_steps - 1]
    # take k steps
    for state in all_possible_hidden_states:
        obs_for_step_n[state] = 1 if ob_for_step_n is None else \
            observation_model(state)[ob_for_step_n]
        beta_n_plus_one[state] = 1
    backward_messages[num_time_steps - 1] = beta_n_plus_one
    # before start computing backward messages, fill in obs_for_steps[n-1]
    obs_for_steps[num_time_steps - 1] = obs_for_step_n
    # TODO: Compute the backward messages
    # what we need to know first is at time step n, what are the possible
    # states the robot could be in, because in the forward process at time
    # step two all possible hidden states are candidates, so we just draw
    # from all_possible_hidden_states
    #
    # one thing we need to know to be able to calculate backward message
    # is given a particular next_state, what are its possible current_states?

    # take n steps
    for index in range(num_time_steps - 2, -1, -1):
        backward_messages[index] = robot.Distribution()
        obs = obs_for_steps[index + 1]
        # recall keys are current states, vals are dicts
        # in which keys are next states, vals are p(x_next|x_current)
        transition_distributions = trans_for_steps[index + 1]
        for current_state in transition_distributions:
            message = 0
            # given a current state we need to know all possible next states
            # we can reuse transition_distributions, in which keys are current
            # states, vals are dicts in which keys are all possible next states
            for next_state in transition_distributions[current_state]:
                next_message = backward_messages[index + 1][next_state]
                ob = obs[next_state]
                tran = transition_distributions[current_state][next_state]
                message += next_message * ob * tran
            backward_messages[index][current_state] = message

    marginals = [None] * num_time_steps  # remove this
    # TODO: Compute the marginals
    # backward and forward message for a particular time step
    # are dicts in which keys are states values and vals are
    # probability numbers. So is phi
    for i in range(num_time_steps):
        marginals[i] = robot.Distribution()
        for state in forward_messages[i]:
            marginals[i][state] = forward_messages[i][state] * \
                obs_for_steps[i][state] * backward_messages[i][state]
        marginals[i].renormalize()

    return marginals


def Viterbi(observations):
    """
    Input
    -----
    observations: a list of observations, one per hidden state
        (a missing observation is encoded as None)

    Output
    ------
    A list of esimated hidden states, each encoded as a tuple
    (<x>, <y>, <action>)
    """

    # -------------------------------------------------------------------------
    # YOUR CODE GOES HERE
    #

    num_time_steps = len(observations)
    estimated_hidden_states = [None] * num_time_steps  # remove this

    # we need n-1 forward message tables and n-1 traceback tables
    forward_messages = [None] * num_time_steps
    traceback_tables = [None] * (num_time_steps - 1)
    forward_messages[0] = robot.Distribution()
    for state in prior_distribution:
        forward_messages[0][state] = -np.log2(prior_distribution[state])
    for index in range(1, num_time_steps):
        message_table = robot.Distribution()
        traceback_table = robot.Distribution()
        obs = robot.Distribution()
        ob_i = observations[index - 1]
        transition_distributions = robot.Distribution()
        next_states_map_current_states = {}
        for current_state in forward_messages[index - 1]:
            transition_distributions[
                current_state] = transition_model(current_state)
            for next_state in transition_distributions[current_state]:
                if next_states_map_current_states.get(next_state) is None:
                    next_states_map_current_states[
                        next_state] = {current_state}
                else:
                    next_states_map_current_states[
                        next_state].add(current_state)
            observation_distribution = observation_model(current_state)
            obs[current_state] = 1 if ob_i is None else \
                observation_distribution[ob_i]

        for next_state in next_states_map_current_states:
            message_min = np.Infinity
            traceback_state = None
            for current_state in next_states_map_current_states[next_state]:
                tran = - \
                    np.log2(transition_distributions[
                            current_state][next_state])
                pre_message = forward_messages[index - 1][current_state]
                ob = -np.log2(obs[current_state])
                message = tran + ob + pre_message
                if message < message_min:
                    message_min = message
                    traceback_state = current_state
            message_table[next_state] = message_min
            traceback_table[next_state] = traceback_state
        forward_messages[index] = message_table
        traceback_tables[index - 1] = traceback_table

    MAP_step_n = np.Infinity
    hidden_state_step_n = None
    ob_step_n = observations[num_time_steps - 1]
    for state in all_possible_hidden_states:
        ob_given_state = 1 if ob_step_n is None else \
            observation_model(state)[ob_step_n]
        MAP_candidate = forward_messages[
            num_time_steps - 1][state] - np.log2(ob_given_state)
        if MAP_candidate < MAP_step_n:
            MAP_step_n = MAP_candidate
            hidden_state_step_n = state
    estimated_hidden_states[num_time_steps - 1] = hidden_state_step_n
    for index in range(num_time_steps - 2, -1, -1):
        estimated_hidden_states[index] = traceback_tables[
            index][estimated_hidden_states[index + 1]]

    return estimated_hidden_states


def second_best(observations):
    """
    Input
    -----
    observations: a list of observations, one per hidden state
        (a missing observation is encoded as None)

    Output
    ------
    A list of esimated hidden states, each encoded as a tuple
    (<x>, <y>, <action>)
    """

    # -------------------------------------------------------------------------
    # YOUR CODE GOES HERE
    #

    num_time_steps = len(observations)
    estimated_hidden_states = [None] * num_time_steps  # remove this

    # we need n-1 forward message tables and n-1 traceback tables
    forward_messages = [None] * num_time_steps
    traceback_tables = [None] * (num_time_steps - 1)
    forward_messages[0] = robot.Distribution()
    for state in prior_distribution:
        log_prob = -np.log2(prior_distribution[state])
        # the second largest message0->1 is infinity, why?
        forward_messages[0][state] = (log_prob, np.Infinity)
    for index in range(1, num_time_steps):
        message_table = robot.Distribution()
        traceback_table = robot.Distribution()
        obs = robot.Distribution()
        ob_i = observations[index - 1]
        transition_distributions = robot.Distribution()
        next_states_map_current_states = {}
        for current_state in forward_messages[index - 1]:
            transition_distributions[
                current_state] = transition_model(current_state)
            for next_state in transition_distributions[current_state]:
                if next_states_map_current_states.get(next_state) is None:
                    next_states_map_current_states[
                        next_state] = {current_state}
                else:
                    next_states_map_current_states[
                        next_state].add(current_state)
            observation_distribution = observation_model(current_state)
            obs[current_state] = 1 if ob_i is None else \
                observation_distribution[ob_i]

        for next_state in next_states_map_current_states:
            message_min = np.Infinity
            message_min_second = np.Infinity
            traceback_state = None
            traceback_state_second = None
            for current_state in next_states_map_current_states[next_state]:
                tran = - \
                    np.log2(transition_distributions[
                            current_state][next_state])
                pre_message = forward_messages[index - 1][current_state][0]
                pre_message_second = forward_messages[
                    index - 1][current_state][1]
                ob = -np.log2(obs[current_state])
                message = tran + ob + pre_message
                message_second = tran + ob + pre_message_second
                if message < message_min:
                    message_min_second = message_min
                    message_min = message
                    traceback_state_second = traceback_state
                    traceback_state = current_state
                    if message_second < message_min_second:
                        message_min_second = message_second
                        traceback_state_second = current_state
                elif message < message_min_second:
                    message_min_second = message
                    traceback_state_second = current_state
            message_table[next_state] = (message_min, message_min_second)
            traceback_table[next_state] = (
                traceback_state, traceback_state_second)
        forward_messages[index] = message_table
        traceback_tables[index - 1] = traceback_table

    MAP_step_n = np.Infinity
    MAP_step_n_second = np.Infinity
    hidden_state_step_n = None
    hidden_state_step_n_second = None
    ob_step_n = observations[num_time_steps - 1]
    for state in all_possible_hidden_states:
        ob_given_state = 1 if ob_step_n is None else \
            observation_model(state)[ob_step_n]
        MAP_candidate = forward_messages[
            num_time_steps - 1][state][0] - np.log2(ob_given_state)
        MAP_second_candidate = forward_messages[
            num_time_steps - 1][state][1] - np.log2(ob_given_state)
        if MAP_candidate < MAP_step_n:
            MAP_step_n_second = MAP_step_n
            MAP_step_n = MAP_candidate
            hidden_state_step_n_second = hidden_state_step_n
            hidden_state_step_n = state
            if MAP_second_candidate < MAP_step_n_second:
                MAP_step_n_second = MAP_second_candidate
                hidden_state_step_n_second = state
        elif MAP_candidate < MAP_step_n_second:
            MAP_step_n_second = MAP_candidate
            hidden_state_step_n_second = state

    # flag, one to mark if the best two paths have
    is_final_state_same = True
    if hidden_state_step_n == hidden_state_step_n_second:
        estimated_hidden_states[num_time_steps - 1] = hidden_state_step_n
    else:
        is_final_state_same = False
        estimated_hidden_states[
            num_time_steps - 1] = hidden_state_step_n_second

    if is_final_state_same:
        # flag to track if branched yet or not
        has_branched = False
        for index in range(num_time_steps - 2, -1, -1):
            pre_states = traceback_tables[
                index][estimated_hidden_states[index + 1]]
            if pre_states[0] == pre_states[1]:
                estimated_hidden_states[index] = pre_states[1]
            elif has_branched:
                estimated_hidden_states[index] = pre_states[0]
            else:
                estimated_hidden_states[index] = pre_states[1]
                has_branched = True

    else:
        for index in range(num_time_steps - 2, -1, -1):
            estimated_hidden_states[index] = traceback_tables[
                index][estimated_hidden_states[index + 1]][0]

    return estimated_hidden_states


# -----------------------------------------------------------------------------
# Generating data from the hidden Markov model
#

def generate_data(num_time_steps, make_some_observations_missing=False,
                  random_seed=None):
    # generate samples from this project's hidden Markov model
    hidden_states = []
    observations = []

    # if the random seed is not None, then this makes the randomness
    # deterministic, which may be helpful for debug purposes
    np.random.seed(random_seed)

    # draw initial state and emit an observation
    initial_state = prior_distribution.sample()
    initial_observation = observation_model(initial_state).sample()

    hidden_states.append(initial_state)
    observations.append(initial_observation)

    for time_step in range(1, num_time_steps):
        # move the robot
        prev_state = hidden_states[-1]
        new_state = transition_model(prev_state).sample()

        # maybe emit an observation
        if not make_some_observations_missing:
            new_observation = observation_model(new_state).sample()
        else:
            if np.random.rand() < .1:  # 0.1 prob. of observation being missing
                new_observation = None
            else:
                new_observation = observation_model(new_state).sample()

        hidden_states.append(new_state)
        observations.append(new_observation)

    return hidden_states, observations


# -----------------------------------------------------------------------------
# Main
#

def main():
    # flags
    make_some_observations_missing = False
    use_graphics = True
    need_to_generate_data = True

    # parse command line arguments
    for arg in sys.argv[1:]:
        if arg == '--missing':
            make_some_observations_missing = True
        elif arg == '--nographics':
            use_graphics = False
        elif arg.startswith('--load='):
            filename = arg[7:]
            hidden_states, observations = robot.load_data(filename)
            need_to_generate_data = False
            num_time_steps = len(hidden_states)

    # if no data is loaded, then generate new data
    if need_to_generate_data:
        num_time_steps = 100
        hidden_states, observations = \
            generate_data(num_time_steps,
                          make_some_observations_missing)

    print('Running forward-backward...')
    marginals = forward_backward(observations)
    print("\n")

    timestep = 99
    print("Most likely parts of marginal at time %d:" % (timestep))
    if marginals[timestep] is not None:
        print(sorted(marginals[timestep].items(),
                     key=lambda x: x[1],
                     reverse=True)[:10])
    else:
        print('*No marginal computed*')
    print("\n")

    print('Running Viterbi...')
    estimated_states = Viterbi(observations)
    print("\n")

    print("Last 10 hidden states in the MAP estimate:")
    for time_step in range(num_time_steps - 10 - 1, num_time_steps):
        if estimated_states[time_step] is None:
            print('Missing')
        else:
            print(estimated_states[time_step])
    print("\n")

    print('Finding second-best MAP estimate...')
    estimated_states2 = second_best(observations)
    print("\n")

    print("Last 10 hidden states in the second-best MAP estimate:")
    for time_step in range(num_time_steps - 10 - 1, num_time_steps):
        if estimated_states2[time_step] is None:
            print('Missing')
        else:
            print(estimated_states2[time_step])
    print("\n")

    difference = 0
    difference_time_steps = []
    for time_step in range(num_time_steps):
        if estimated_states[time_step] != hidden_states[time_step]:
            difference += 1
            difference_time_steps.append(time_step)
    print("Number of differences between MAP estimate and true hidden " +
          "states:", difference)
    if difference > 0:
        print("Differences are at the following time steps: " +
              ", ".join(["%d" % time_step
                         for time_step in difference_time_steps]))
    print("\n")

    difference = 0
    difference_time_steps = []
    for time_step in range(num_time_steps):
        if estimated_states2[time_step] != hidden_states[time_step]:
            difference += 1
            difference_time_steps.append(time_step)
    print("Number of differences between second-best MAP estimate and " +
          "true hidden states:", difference)
    if difference > 0:
        print("Differences are at the following time steps: " +
              ", ".join(["%d" % time_step
                         for time_step in difference_time_steps]))
    print("\n")

    difference = 0
    difference_time_steps = []
    for time_step in range(num_time_steps):
        if estimated_states[time_step] != estimated_states2[time_step]:
            difference += 1
            difference_time_steps.append(time_step)
    print("Number of differences between MAP and second-best MAP " +
          "estimates:", difference)
    if difference > 0:
        print("Differences are at the following time steps: " +
              ", ".join(["%d" % time_step
                         for time_step in difference_time_steps]))
    print("\n")

    # display
    if use_graphics:
        app = graphics.playback_positions(hidden_states,
                                          observations,
                                          estimated_states,
                                          marginals)
        app.mainloop()


if __name__ == '__main__':
    main()
