# Gaussian Naive Bayes Classifier
This is a quiz from Udacity Self-Driving Car Nano Degree, Path Planning module. I made some tweaks to the original design.
1. I applied natural log to the MAP estimation, effectively converting product to summation, reducing the complexity of the computation.
2. I skipped computing the total probability of the given observation. That is Prob(s=observation[0], d=observation[1], s_dot=observation[2], d_dot=observation[3]). The reason is that it's the same for all 4 labels in the MAP estimation. We just need to compare them to pick the one that maximizes the estimation. Why not skip it to save some computing power and speed things up?
3. For this continuous case I think Laplace Smoothing is not applicable. Gaussian distribution covers all the possible values no matter how unlikely it is.
4. In the continuous universe, the likelihood we use to calculate posteriori is actually probability density function which does not represent probability. But it is good enough for the sake of comparison.

In this implementation the features are just the 4 raw measurements.

## Test
No test suite available at the moment. But with `test.json` it achieves 85.2% accuracy. I suppose that means the implementation is correct.
