import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

public class FastCollinearPoints {
	private List<LineSegment> segments = new ArrayList<LineSegment>();
	private List<Double> prevSlopes = new ArrayList<Double>();

	// finds all line segments containing 4 points
	public FastCollinearPoints(Point[] points) {

		if( points == null ) throw new NullPointerException();

		Arrays.sort(points);

		int length = points.length;

		Point[] aux = new Point[length];

		// Check for duplicates
		for (int i = 0; i < points.length; i++) { 
			if( points[i] == null ) throw new NullPointerException();
			aux[i] = points[i];
			for (int j = i + 1 ; j < points.length; j++) { 
				if (points[i].equals(points[j])) { // got the duplicate element 
					throw new IllegalArgumentException();
				} 
			}  
		}

		for (int i = 0; i < length; i++ ) {

			Point p = points[i];

			Arrays.sort(aux, p.slopeOrder());
				
			double prevSlope = -0.0;

			Point start = null;
			Point end = null;
			Point t;

			int l = 0;

			// System.out.println(p);
			for (int k = 0; k < length; k++ ) {
				Point q = aux[k];

				if( p.equals(q) ) continue;


				if( p.slopeTo(q) == prevSlope ) {

					if( l == 0 ) {
						start = p;
						end   = p;

						t = aux[k-1];

						if( t.compareTo(end) == 1 ) {
							end   = t;
						} else if( t.compareTo(start) == -1 ) {
							start = t;
						}
					}

					if( q.compareTo(end) == 1 ) {
						end   = q;
					} else if( q.compareTo(start) == -1 ) {
						start = q;
					}

					l++;

				}

				if( l > 1 && ( p.slopeTo(q) != prevSlope || k == length - 1 ) ) {

					if( start.equals(p) )
						segments.add( new LineSegment( start, end ) );

					start = null;
					end = null;

					l = 0;
				} else if( l > 0 && p.slopeTo(q) != prevSlope ) {

					start = null;
					end = null;

					l = 0;
				}

				prevSlope = p.slopeTo(q);

			}

		}

	}

	// the number of line segments
	public int numberOfSegments() {
		return segments.size();
	}

	// the line segments
	public LineSegment[] segments() {
		return segments.toArray( new LineSegment[segments.size()] );
	}


    public static void main(String[] args) {

	    // read the n points from a file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
	    // StdDraw.enableDoubleBuffering();
	    // StdDraw.setXscale(0, 32768);
	    // StdDraw.setYscale(0, 32768);
	    // for (Point p : points) {
	    //     p.draw();
	    // }
	    // StdDraw.show();

	    // print and draw the line segments
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        // segment.draw();
	    }

	        StdOut.println("=========");
	    FastCollinearPoints collinear2 = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear2.segments()) {
	        StdOut.println(segment);
	        // segment.draw();
	    }
    }
}