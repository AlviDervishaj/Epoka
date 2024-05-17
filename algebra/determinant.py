# importing Numpy package 
import numpy as np 

# Enter matrix here.
# It can be any matrix, not just 3X3
matrix_ = np.array([
    [1, 2, 3],
    [1, 1, 1],
    [0, 0, 1],
])
  
# Displaying the Matrix 
print("Numpy Matrix is:") 
print(matrix_) 
  
# calculating the determinant of matrix 
det = np.linalg.det(matrix_) 
  
print(f"\nDeterminant of given {matrix_.shape}") 
print(int(det)) 
