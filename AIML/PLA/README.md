![alt-text][dichotomy]

In a Cartesian coordinate system, a straight line can represent a point (**w0**, **w1**) where any point on the line (**x0**, **x1**) satisfies w0\*x0 + w1\*x1 = 0. Any point (**y0**, **y1**) on one side of the line satisfies w0\*y0 + w1\*y1 < 0 while any point (**z0**, **z1**) on the other side of the line satisfies w0\*z0 + w1\*z1 > 0

There is a caveat in this interpretation. All the points can be treated as vectors in a 2d vector space without transformations. In such vector space the point (**w0**, **w1**) can be interpreted as the vector that is orthogonal to all the vectors on that line. We know any vector on the line has its tail at the origin and its head on the line. Unless the line goes through the origin it is impossible to find a **w** in this 2D vector space that is orthogonal to the line. For example, a line that contains (2, 0) and (0, 2) can be represented as -x + 2 = 0 in the Cartesian system. There does not exist a nonzero vector **w** such that w0\*2 = 0 and w1\*2 = 0.

The main takeaway is in general the vector represented by the line is not on the plane (the vector space) which contains the line. But it does exist.

---
[dichotomy]: ./dichotomy.png
