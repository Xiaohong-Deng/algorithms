import collections
import numpy as np
from math import sqrt, pi, exp


def log_gaussian_prob(obs, mu, sig):
  num = (obs - mu) ** 2
  denum = 2 * sig ** 2
  norm = 1 / sqrt(2 * pi * sig ** 2)
  log_prob = (-num / denum) + 0.5 * (np.log(2) + np.log(pi) + 2 * np.log(sig))
  return log_prob


class GNB(object):

  def __init__(self):
    self.possible_labels = ['left', 'keep', 'right']
    self.is_trained = False
    self._log_prior_by_label = collections.defaultdict(float)
    # order in the list is [s, d, s_dot, d_dot]
    self._label_to_feature_means = {key: [] for key in self.possible_labels}
    self._label_to_feature_stds = {key: [] for key in self.possible_labels}

  def _get_label_counts(self, labels):
    label_to_counts = collections.defaultdict(int)
    for l in labels:
      label_to_counts[l] += 1

    return label_to_counts

  def _group_data_by_label(self, data, labels):
    label_to_data = dict()
    for label in self.possible_labels:
      label_to_data[label] = []

    for label, data_point in zip(labels, data):
      label_to_data[label].append(data_point)

    return label_to_data

  def train(self, data, labels):
    """
    Trains the classifier with N data points and labels.

    INPUTS
    data - array of N observations
    - Each observation is a tuple with 4 values: s, d,
      s_dot and d_dot.
      - Example : [
          [3.5, 0.1, 5.9, -0.02],
          [8.0, -0.3, 3.0, 2.2],
          ...
        ]

    labels - array of N labels
      - Each label is one of "left", "keep", or "right".
    """
    # prior: p(label = left_or_kepp_or_right)
    # likelihood: p(feature1, feature2, ..., feature_n | label)
    N = len(labels)
    label_to_counts = self._get_label_counts(labels)

    for key in self.possible_labels:
      self._log_prior_by_label[key] = np.log(label_to_counts[key]) - np.log(N)

    label_to_data = self._group_data_by_label(data, labels)

    for label, data in label_to_data.items():
      data = np.array(data)
      data = np.transpose(data)
      means = np.mean(data, axis=1)
      stds = np.std(data, axis=1)

      self._label_to_feature_means[label] = means
      self._label_to_feature_stds[label] = stds

    self.is_trained = True

  def predict(self, observation):
    """
    Once trained, this method is called and expected to return
    a predicted behavior for the given observation.

    INPUTS

    observation - a 4 tuple with s, d, s_dot, d_dot.
      - Example: [3.5, 0.1, 8.5, -0.2]

    OUTPUT

    A label representing the best guess of the classifier. Can
    be one of "left", "keep" or "right".
    """
    if not self.is_trained:
      print("Classifier has not been trained! ")
      print("Please train it before predicting!")
      return

    MAP_estimates = dict()
    for label in self.possible_labels:
      # use log convert product to sum
      log_product = self._log_prior_by_label[label]
      for i, feature_val in enumerate(observation):
        log_product += log_gaussian_prob(feature_val,
                                         self._label_to_feature_means[label][i],
                                         self._label_to_feature_stds[label][i])

      MAP_estimates[label] = log_product

    # MAP_estimates contains likelihood*prior that is not normalized
    # in other words, it is not divided by the total probability
    # P(s=observation[0], d=observation[1], s_dot=observation[2], d_dot=observation[3])
    # because it is the same for all four labels, we only need to compare them to decide
    # which label to take. so comparing numerators suffices to do the job

    prediction = 'None'
    max_prob = 0
    for label, prob in MAP_estimates.items():
      if prob > max_prob:
        prediction = label
        max_prob = prob

    return prediction
