import numpy as np
from numpy.random import default_rng
from scipy.linalg import null_space

def one_dimensional_null_space():
    A = np.array([[1, 0, 1, 3], [2, 3, 4, 7], [-1, -3, -3, -4]])
    ns = null_space(A)
    ns * np.copysign(1, ns[0,0])  # Remove the sign ambiguity of the vector
    print(f"A: {A}\nNull Space Of A: {ns}")

def two_dimensional_null_space():
    rng = default_rng()
    B = rng.random((3, 5))
    Z = null_space(B)
    print(f"B: {B}")
    print(f"Z: {Z}")
    print(f"Vector Z shape: {Z.shape}")
    print(f"Are all values close to 0 ? : {np.allclose(B.dot(Z), 0)}")
    print(Z.T.dot(Z))


if __name__ == "__main__":
    one_dimensional_null_space() 
    two_dimensional_null_space()

