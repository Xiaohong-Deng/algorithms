#!/usr/bin/env python

import classifier
from importlib import reload
import json
# gnb = GNB()
# with open('train.json', 'rb') as f:
# 	j = json.loads(f.read().decode('utf8'))
# print(j.keys())
# X = j['states']
# Y = j['labels']
# print(Y[:5])
reload(classifier)
def main():
	gnb = classifier.GNB()
	with open('train.json', 'rb') as f:
		j = json.loads(f.read().decode('utf8'))
	print(j.keys())
	X = j['states']
	Y = j['labels']
	gnb.train(X, Y)

	with open('test.json', 'rb') as f:
		j = json.loads(f.read().decode('utf8'))

	X = j['states']
	Y = j['labels']
	score = 0
	for coords, label in zip(X,Y):
		predicted = gnb.predict(coords)
		if predicted == label:
			score += 1
	fraction_correct = float(score) / len(X)
	print("You got {} percent correct".format(100 * fraction_correct))

if __name__ == "__main__":
	main()
