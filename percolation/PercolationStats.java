/******************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats
 *  Dependencies: -
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private double[] fractions;

	private int experiments;

	public PercolationStats(int n, int t) {
		experiments = t;
		fractions = new double[t];
		for(int k = 0; k < t; k++) {
			Percolation percolation = new Percolation(n);
			double sites = 0;
			int i;
			int j;


			while(!percolation.percolates()) {

				i = StdRandom.uniform(1, n+1);
				j = StdRandom.uniform(1, n+1);

				if (!percolation.isOpen(i, j)) {
					percolation.open(i, j);
					sites++;
				}
			}

			fractions[k] = sites/(n*n);

		}
	}   


	public double mean() {
		return StdStats.mean(fractions);
	}                  


	public double stddev() {
		return StdStats.stddev(fractions);
	}                


	public double confidenceLo() {
		double mean = mean();
		double stddev = stddev();
		double lo = mean - 1.96*stddev/Math.sqrt(experiments);
		
		return lo;
	}          


	public double confidenceHi() {
		double mean = mean();
		double stddev = stddev();
		double hi = mean + 1.96*stddev/Math.sqrt(experiments);
		
		return hi;
	}           


	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int t = Integer.parseInt(args[1]);
		PercolationStats percolationStats = new PercolationStats(n,t);
		System.out.println("Mean                    = " + percolationStats.mean());
		System.out.println("stddev                  = " + percolationStats.stddev());
		System.out.println("95% confidence interval = " + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi());
	}  

}