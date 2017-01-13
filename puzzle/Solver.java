import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {


	private int moves = -1;

	private boolean solvable = false;


	private MinPQ<SearchNode> pq;
	private MinPQ<SearchNode> pq2;
	private Stack<Board> solution = new Stack<Board>();


	private class SearchNode implements Comparable<SearchNode> {
		public Board board;
		public int moves;
		public SearchNode previousNode;
		public int priority = 0;
		public SearchNode( Board board, int moves, SearchNode previousNode ) {
			this.board = board;
			this.moves = moves;
			this.previousNode = previousNode;

			this.priority = this.board.manhattan() + moves;
		}

		public int compareTo( SearchNode x ) {

			if( x.priority == this.priority ) {
				return 0;
			} else if( this.priority < x.priority ) {
				return -1;
			}

			return 1;
		}
	}


	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {

		SearchNode best = null;
		SearchNode best2 = null;
		boolean loop = true;

		Board twin = initial.twin();

		pq = new MinPQ<SearchNode>();
		pq2 = new MinPQ<SearchNode>();

		pq.insert(new SearchNode(initial, 0, null));
		pq2.insert(new SearchNode(twin, 0, null));

		while( loop ) {
			best = pq.delMin();
			best2 = pq2.delMin();

			if( best.board.isGoal() ) {
				solvable = true;
				loop = false;
				break;
			}

			if( best2.board.isGoal() ) {
				solvable = false;
				loop = false;
				moves = -1;
				solution = null;
				break;
			}

			for( Board neighbor : best.board.neighbors() ) {

				if( best.previousNode == null || ! neighbor.equals( best.previousNode.board ) )
					pq.insert(new SearchNode(neighbor, best.moves + 1, best));
			}

			for( Board neighbor : best2.board.neighbors() ) {

				if( best2.previousNode == null || ! neighbor.equals( best2.previousNode.board ) )
					pq2.insert(new SearchNode(neighbor, best2.moves + 1, best2));
			}

		}

		if( solvable ) {
			moves = 0;

			while( best.previousNode != null ) {
				solution.push(best.board);
				best = best.previousNode;
				moves++;
			}

			solution.push(initial);
		}


	}



	// is the initial board solvable?

	public boolean isSolvable() {
		return solvable;
	}
	// min number of moves to solve initial board; -1 if unsolvable         
	public int moves() {
		return moves;
	}

	// sequence of boards in a shortest solution; null if unsolvable             
	public Iterable<Board> solution() {
		return solution;
	}


	public static void main(String[] args) {
        // create initial board from file

	    // create initial board from file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}

}