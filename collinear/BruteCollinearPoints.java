import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

public class BruteCollinearPoints {
	private List<LineSegment> segments = new ArrayList<LineSegment>();

	// finds all line segments containing 4 points
	public BruteCollinearPoints(Point[] points) {

		if( points == null ) throw new NullPointerException();

		int length = points.length;
		// Arrays.sort(points);

		// Check for duplicates
		for (int i = 0; i < points.length; i++) { 
			if( points[i] == null ) throw new NullPointerException();
			for (int j = i + 1 ; j < points.length; j++) { 
				if (points[i].equals(points[j])) { // got the duplicate element 
					throw new IllegalArgumentException();
				} 
			}  
		}

		for (int i = 0; i < length - 3; i++ ) {

			Point p = points[i];

			for (int j = i + 1; j < length - 2; j++ ) {
				Point q = points[j];

				for (int k = j + 1; k < length - 1; k++ ) {
					Point r = points[k];

					if( p.slopeTo(q) != p.slopeTo(r) ) continue;

					for (int l = k + 1; l < length; l++ ) {
						Point s = points[l];

						if( p.slopeTo(q) != p.slopeTo(s) ) continue;

						Point start = p;
						Point end = p;

						if( q.compareTo(end) == 1 ) {
							end   = q;
						} else if( q.compareTo(start) == -1 ) {
							start = q;
						}
						
						if( r.compareTo(end) == 1 ) {
							end   = r;
						} else if( r.compareTo(start) == -1 ) {
							start = r;
						}

						if( s.compareTo(end) == 1 ) {
							end   = s;
						} else if( s.compareTo(start) == -1 ) {
							start = s;
						}

						segments.add( new LineSegment(start, end) );
						
					}
				}
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
	    StdDraw.enableDoubleBuffering();
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
    }
}