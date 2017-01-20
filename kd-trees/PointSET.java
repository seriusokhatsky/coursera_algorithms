import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

	private SET<Point2D> points;

	// construct an empty set of points 
	public PointSET() {
		points = new SET<Point2D>();
	}


	// is the set empty? 
	public boolean isEmpty() {
		return points.isEmpty();
	}


	// number of points in the set 
	public int size() {
		return points.size();
	}


	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		points.add(p);
	}


	// does the set contain point p? 
	public boolean contains(Point2D p) {
		return points.contains(p);
	}


	// draw all points to standard draw 
	public void draw() {
		for ( Point2D p : points ) {
			p.draw();
		}
	}


	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		SET<Point2D> range = new SET<Point2D>();

		for ( Point2D a : points ) {
			if( rect.contains(a) ) {
				range.add(a);
			}
		}

		return range;
	}


	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {

		Point2D nearest = null;
		double minDis = 999;
		double iDis;

		for ( Point2D a : points ) {
			iDis = p.distanceTo(a);
			if( iDis < minDis ) {
				nearest = a;
				minDis = iDis;
			}
		}

		return nearest;
	}

	// unit testing of the methods (optional) 
	public static void main(String[] args) {


		Point2D p = new Point2D(1,2);
		Point2D p2 = new Point2D(3,2);
		Point2D p3 = new Point2D(1,6);
		Point2D p4 = new Point2D(1114,0);

		PointSET set = new PointSET();

		set.insert(p);
		set.insert(p2);
		set.insert(p3);
		set.insert(p4);

		System.out.println(set.points);

	}
}