/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:    java Percolation
 *  Dependencies: -
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private boolean[][] grid;

	private int size;

	private WeightedQuickUnionUF strucutre;

	public Percolation(int n) {
		size = n;
		grid = new boolean[n+1][n+1];

		strucutre = new WeightedQuickUnionUF(n*n+2);

		// for(int i = 0; i < size - 1; i++) {
		for(int i = 1; i <= size; i++) {
			strucutre.union(0, i); // Create abstract point that connected with top row
			if( size == 1 ) {
				strucutre.union(1, 2); // And point that connected with last row
			} else {
				strucutre.union(size*size+1, size*size-i); // And point that connected with last row
			}
		}

	}

	private void union(int i, int j, int k, int l) {
		i--;
		k--;
		strucutre.union(i*size+j, k*size+l);
	}

	private boolean connected(int i, int j, int k, int l) {
		boolean result = false;
		i--;
		k--;
		if (strucutre.connected(i*size+j, k*size+l)) result = true;
		return result;
	}

	public void open(int i, int j) {

		if (isOpen(i, j)) return;

		grid[i][j] = true;

		if ( j > 1 && isOpen(i, j-1)) union(i, j, i, j-1);

		if ( j < size && isOpen(i, j+1)) union(i, j, i, j+1);

		if ( i > 1 && isOpen(i-1, j)) union(i, j, i-1, j);

		if ( i < size && isOpen(i+1, j)) union(i, j, i+1, j);
	}
	public boolean isOpen(int i, int j) {
		boolean result = false;

		if (i <= 0 || i > size) throw new IndexOutOfBoundsException("row index i out of bounds");
		if (j <= 0 || j > size) throw new IndexOutOfBoundsException("row index j out of bounds");

		if (grid[i][j]) result = true;
		return result;
 	}
	public boolean isFull(int i, int j) {
		boolean result = false;
		if( i > size && j > size ) {
			if( connected(size, 1, 1, 0) ) result = true;
		} else if (isOpen(i,j) && connected(i, j, 1, 0)) {
			result = true;
		}
		return result;
	}

	public boolean percolates() {
		boolean result = false;

		if (isFull(size+1, size+1)) {
			result = true;
		}
		return result;
	}

	public int numberOfOpenSites() {
		int num = 0;

		for(int i = 1; i <= size; i++ ) {
			for (int k = 1; k <= size; k++ ) {
				if( isOpen(i,k) ) {
					num++;
				}
			}
		}

		return num;
	}

	public static void main(String[] args) {
		System.out.println("Start program");
		Percolation perc = new Percolation(3);
		perc.open(1,1);
		perc.open(1,2);
		perc.open(2,2);
		perc.open(3,2);
		perc.open(3,3);


		if( perc.percolates() ) {
			System.out.println("yes!");
		}
	}
}
