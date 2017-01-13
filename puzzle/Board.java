import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Arrays;
import edu.princeton.cs.algs4.Stack;

public class Board {

    private int[][] tiles;

    private int n;

    private int hamming = 0;
    private int manhattan = 0;

    // construct a board from an n-by-n array of blocks // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {

        n = blocks.length;
        tiles = blocks;

        calcHamming();
        calcManhattan();
    }

    // board dimension n   
    public int dimension() {
        return n;
    }

    // number of blocks out of place   
    public int hamming() {
        return hamming;
    }
    // sum of Manhattan distances between blocks and goal            
    public int manhattan() {
        return manhattan;
    }
    // number of blocks out of place   
    private void calcHamming() {
        for (int i = 0; i < n; i++ ) {
            for (int j = 0; j < n; j++ ) {
                int block = tiles[i][j];
                if( block == 0 ) continue;
                if( i*n + j + 1 != block )hamming++;
            }
        }
    }

    // sum of Manhattan distances between blocks and goal            
    private void calcManhattan() {
        for (int i = 0; i < n; i++ ) {
            for (int j = 0; j < n; j++ ) {
                int block = tiles[i][j];
                if( block == 0 ) continue;
                manhattan += manhattanDis( j, i, getX(block), getY(block) );
            }
        }
    }

    private int manhattanDis( int x1, int y1, int x2, int y2 ) {
        int dis = 0;

        int delx = 0;
        int dely = 0;

        if( x1 > x2 ) delx = x1 - x2;
        else delx = x2 - x1;

        if( y1 > y2 ) dely = y1 - y2;
        else dely = y2 - y1;

        dis = dely + delx;

        return dis;
    }

    private int getX( int block ) {
        int x = 0;

        x = (block - 1 + n) % n;

        return x;
    }

    private int getY( int block ) {
        int y = 0;

        y = (block - 1) / n;

        return y;
    }

    private int[][] getTiles() {
        return tiles;
    }


    // is this board the goal board?             
    public boolean isGoal() {
        boolean result = true;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if( tiles[i][j] == 0 ) continue;
                if( i*n + j + 1 != tiles[i][j] ) result = false;
            }
        }

        return result;

    }
    // a board that is obtained by exchanging any pair of blocks           
    public Board twin() {
        
        // Random swap
        // int[] coords = randomCoords();

        int i = 0;
        int j = 0;
        while (tiles[i][j] == 0 || tiles[i][j + 1] == 0) {
            j++;
            if (j >= n - 1) {
                i++;
                j = 0;
            }
        }

        Board twin = createBoardSwap( i, j, i, j + 1 );

        return twin;
    }

    private int[] randomCoords() {
        int[] coords = new int[4];

        coords[0] = StdRandom.uniform(n);
        coords[1] = StdRandom.uniform(n);
        coords[2] = StdRandom.uniform(n);
        coords[3] = coords[1]; //StdRandom.uniform(n);

        if( coords[0] == coords[2]) coords = randomCoords(); // && coords[1] == coords[3] 

        return coords;
    }

    // does this board equal y?          
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;
        return this.n == that.n && tilesEquals(this.tiles, that.tiles);
    }

    private boolean tilesEquals(int[][] first, int[][] second) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (first[i][j] != second[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();

        int x0 = 0;
        int y0 = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if( tiles[i][j] == 0 ) {
                    x0 = i;
                    y0 = j;
                    break;
                }
            }
        }

        // Move left
        if( x0 > 0 ) {
            stack.push( createBoardSwap( x0, y0, x0 - 1, y0 ) );
        }

        // Move right
        if( x0 < n - 1 ) {
            stack.push( createBoardSwap( x0, y0, x0 + 1, y0 ) );
        }

        // Move up
        if( y0 > 0 ) {
            stack.push( createBoardSwap( x0, y0, x0, y0 - 1 ) );
        }

        // Move down
        if( y0 < n - 1 ) {
            stack.push( createBoardSwap( x0, y0, x0, y0 + 1 ) );
        }

        return stack;
    }

    private Board createBoardSwap( int x1, int y1, int x2, int y2 ) {
        int[][] copy = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = tiles[i][j];
            }
        }

        int temp = copy[x1][y1];
        copy[x1][y1] = copy[x2][y2];
        copy[x2][y2] = temp;

        Board board = new Board( copy );

        return board;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        System.out.println(initial);
        System.out.println(initial.twin());

        // System.out.println(initial);
        // for(Board board : initial.neighbors() ) {
        //     System.out.println(board);
        // }

        // System.out.println(initial.hamming());
        // System.out.println(initial.manhattan());

        // if( initial.equals(new Board(blocks)) )
        //     System.out.println(initial.toString());
        

        // if( initial.equals(initial.twin()) )
        //     System.out.println(initial.toString());
        
    }

}
