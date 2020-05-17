"""
Due to a lecture demo I followed there is an extra complication in PLA
Every time weight is updated we start looking for next misclassified sample
indexed after the one we just looked. This is not necessarily the better strategy.
One can shuffle the sample array every time before trying to update weight
"""
import numpy as np
import sys
import time


def load_data(fname):
    """
    The method is designed to load data of the form as follows:
    feat1, feat2, ..., featn, label
    where label is the value we want to predict for new data

    Input
    ---------------
    fname: path to you data file
    num_feat: number of features each data instance contains, note the last feature is label

    Output
    ---------------
    samples: input data we use to feed our model
    labels: output data we expect from our model
    """
    raw = np.loadtxt(fname, dtype=float)

    Xn = raw[:, :-1]
    X0 = np.ones((Xn.shape[0], 1))
    samples = np.hstack((X0, Xn))

    labels = raw[:, -1]

    return samples, labels.reshape(-1, )


def PLA(fname, num_iters=2000):
    """Must be provided with linear separable datasets otherwise will be stuck in infinite loop!"""
    samples, labels = load_data(fname)
    indices = np.arange(samples.shape[0])
    updates = []

    for i in range(num_iters):  # repeat the PLA process for 2000 times
        np.random.shuffle(indices)
        samples_shuffled = samples[indices]
        labels_shuffled = labels[indices]
        update = 0
        weights = np.zeros((samples_shuffled.shape[1], ))
        start_idx = 0

        while True:  # do not stop until it is perfectly separable
            predictions = np.matmul(samples_shuffled, weights)

            predictions = np.sign(predictions)
            zero_indices = np.where(predictions == 0)
            predictions[zero_indices] = -1
            is_correct = np.multiply(labels_shuffled, predictions)
            incorrect_indices = np.where(is_correct == -1)[0]
            if incorrect_indices.size == 0:
                break
            # we want to find the first misclassified sample starting from the
            # index where the last misclassified sample happened
            valid_indices = np.where(incorrect_indices >= start_idx)[0]

            if valid_indices.size == 0:
                start_idx = 0
                target_idx = incorrect_indices[0]
            else:
                target_idx = incorrect_indices[valid_indices[0]]

            # update weights, note weights are of the shape (num_feat, )
            # labels_shuffled[incorrect_indices[0][0]] is a scalar
            # samples_shuffled[incorrect_indices[0][0]] is of the shape (1, num_feat)
            weights += 0.999 * labels_shuffled[target_idx] * samples_shuffled[target_idx, :]
            # print(np.matmul(samples_shuffled[target_idx, :], weights))
            update += 1
            start_idx = target_idx + 1

        updates.append(update)
    return np.mean(updates)


def pocket(fn_train, fn_test, num_iters=2000, num_updates=50):
    samples_train, labels_train = load_data(fn_train)
    samples_test, labels_test = load_data(fn_test)
    num_samples, num_feats = samples_train.shape
    err_rates = []

    for i in range(num_iters):
        np.random.seed(int(time.time()))
        update = 0
        weights = np.zeros((num_feats, ))
        best_weights = weights
        min_num_errors = num_samples
        predictions = np.matmul(samples_train, weights)

        while update < num_updates:
            target_idx = np.random.randint(num_samples)
            prediction = predictions[target_idx]

            if prediction == 0:
                prediction = -1
            else:
                prediction = np.sign(prediction)

            is_correct = prediction * labels_train[target_idx]

            if is_correct == -1:
                update += 1
                weights = weights + labels_train[target_idx] * samples_train[target_idx, :]
                predictions = np.matmul(samples_train, weights)
                predictions = np.sign(predictions)
                predictions[np.where(predictions == 0)] = -1
                are_correct = np.multiply(labels_train, predictions)
                num_errors = np.where(are_correct == -1)[0].size

                if num_errors < min_num_errors:
                    best_weights = weights
                    min_num_errors = num_errors

        predictions = np.matmul(samples_test, best_weights)
        predictions = np.sign(predictions)
        predictions[np.where(predictions == 0)] = -1
        is_correct = predictions * labels_test
        err_rate = np.where(is_correct == -1)[0].size / is_correct.size
        err_rates.append(err_rate)

        return np.mean(err_rates)


if __name__ == '__main__':
    if len(sys.argv) == 4:
        pla_file = sys.argv[1].strip()
        pocket_train = sys.argv[2].strip()
        pocket_test = sys.argv[3].strip()

        print(PLA(pla_file))
        print(pocket(pocket_train, pocket_test))
    else:
        print('Please provide paths to files corresponding to PLA dataset, pocket training set and pocket test set')
