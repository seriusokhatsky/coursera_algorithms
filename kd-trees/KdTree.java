import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

	private Node root;

	private int size;

	private Node chemp;

	private double minDist;
	private double neighDist;

	// construct an empty set of points 
	public KdTree() {
		root = null;
		size = 0;
	}


	// is the set empty? 
	public boolean isEmpty() {
		return root == null;
	}


	// number of points in the set 
	public int size() {
		return size;
	}


	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		Node node = new Node(p);
		if( root == null ) {
			root = node;
			root.setRect(0,0,1,1);
		} else {
			put( root, node );
		}

		size++;
	}

	private void put(Node root, Node node ) {

		double x0 = 0;
		double y0 = 0;
		double x1 = 0;
		double y1 = 0;

		node.byX = ! node.byX;

		if( root.equals(node) ) {
			size--;
			return;
		}

		if( root.compareTo(node) == -1 ) {

			if( root.left == null ) {
				root.left = node;
				if( root.byX ) {
					x0 = root.rect.xmin();
					y0 = root.rect.ymin();
					x1 = root.point.x();
					y1 = root.rect.ymax();
				} else {
					x0 = root.rect.xmin();
					y0 = root.rect.ymin();
					x1 = root.rect.xmax();
					y1 = root.point.y();
				}
				node.setRect( x0, y0, x1, y1 );
			}

			else put(root.left, node);

		} else if( root.compareTo(node) == 1 ) {

			if( root.right == null ) {
				root.right = node;
				if( root.byX ) {
					x0 = root.point.x();
					y0 = root.rect.ymin();
					x1 = root.rect.xmax();
					y1 = root.rect.ymax();
				} else {
					x0 = root.rect.xmin();
					y0 = root.point.y();
					x1 = root.rect.xmax();
					y1 = root.rect.ymax();
				}
				node.setRect( x0, y0, x1, y1 );
			}

			else put(root.right, node);

		}


	}


	// does the set contain point p? 
	public boolean contains(Point2D p) {
		boolean contains = false;

		Node find = new Node(p);

		contains = has(root, find);

		return contains;
	}

	private boolean has(Node x, Node find) {

		if( x.equals(find) ) return true;

		if( x.compareTo(find) == 1 && x.right != null ) return has( x.right, find );

		if( x.compareTo(find) == -1 && x.left != null ) return has( x.left, find );

		return false;

	}


	private static class Node implements Comparable<Node> {
		public Node left = null;
		public Node right = null;
		public Point2D point;
		public RectHV rect;
		public boolean byX = true;
		public Node(Point2D point) {
			this.point = point;
		}

		public boolean equals(Node node) {
			return this.point.equals(node.point);
		}

		public void setRect(double x0, double y0, double x1, double y1) {

			// Point2D x0 = null;
			// Point2D y0 = null;
			// Point2D x1 = null;
			// Point2D y1 = null;

			rect = new RectHV(x0,y0,x1,y1);

			// // Horizontal division
			// if( this.byX ) {	

			// } else {
			// 	// Vertical division
			// }
		}
		public int compareTo(Node that) {

			if( this.equals(that) ) return 1;

			if( this.byX && this.point.x() > that.point.x() ) 
				return -1;

			if( ! this.byX && this.point.y() > that.point.y() ) 
				return -1;

			return 1;
		}

	}

	// draw all points to standard draw 
	public void draw() {
		double x0 = 0;
		double y0 = 0;
		double x1 = 1;
		double y1 = 1;
		// System.out.println("======== drawing =========");
		drawNode(root, x0, y0, x1, y1);
		// StdDraw.pause(4000);
	}

	private void drawNode( Node node, double x0, double y0, double x1, double y1 ) {
		// StdDraw.pause(1300);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);

		node.point.draw();

        StdDraw.setPenRadius(0.001);
		if( node.byX ) {
        	StdDraw.setPenColor(StdDraw.RED);
			StdDraw.line(node.point.x(), y0, node.point.x(), y1);
		} else {
        	StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.line(x0, node.point.y(), x1, node.point.y());
		}

		if( node.left != null ) {
		
			if( node.byX ) {
				drawNode(node.left, x0, y0, node.point.x(), y1);
			} else {
				drawNode(node.left, x0, y0, x1, node.point.y());
			}

		}

		if( node.right != null ) {
			if( node.byX ) {
				drawNode(node.right, node.point.x(), y0, x1, y1);
			} else {
				drawNode(node.right, x0, node.point.y(), x1, y1);
			}

		}

	}


	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		Stack<Point2D> range = new Stack<Point2D>();

		range = rangeSearch( root, rect, range );

		return range;
	}

	private Stack<Point2D> rangeSearch( Node node, RectHV rect, Stack<Point2D> range) {

		// if node point is in the rectangle
		if( rect.contains(node.point) ) range.push(node.point);

		// Go left. If rectangle intersect with left node rectangle

		if( node.left != null && rect.intersects( node.left.rect ) ) {
			range = rangeSearch( node.left, rect, range );
		}

		// Go right. If rectangle intersect with left node rectangle

		if( node.right != null && rect.intersects( node.right.rect ) ) {
			range = rangeSearch( node.right, rect, range );
		}

		return range;

	}


	// a nearest neighbor in the set to point p; null if the set is empty 
	public Point2D nearest(Point2D p) {

		chemp = null;
		// it = 0;
		minDist = root.point.distanceTo(p);

		closest( root, new Node(p) );

		if( chemp == null ) chemp = root;

		return chemp.point;
	}


	private void closest( Node x, Node find ) {
		// it++;

		double curDist = x.point.distanceTo(find.point);

		if( curDist <= minDist ) {
			minDist = curDist;
			chemp = x;
		}

		if( x.equals(find) ) {
			chemp = x;
			return;
		}


		if( x.left == null && x.right == null ) return;

		// if the point left to the node

		if( x.left != null && x.compareTo(find) == -1 ) 	 closest( x.left, find );
		else if( x.right != null && x.compareTo(find) == 1 ) closest( x.right, find );

		// if the query point is closer to the best point then to the middle line and to the right side
		Point2D middlePoint = null;

		if( x.byX ) 		 middlePoint = new Point2D( x.point.x(), find.point.y() );
		else if( x != null ) middlePoint = new Point2D( find.point.x(), x.point.y() );
		

		// Look to the opposite side if needed
		if( minDist > find.point.distanceTo(middlePoint) && x.right != null && x.compareTo(find) == -1 ) 
			closest( x.right, find );
		else if( minDist > find.point.distanceTo(middlePoint) && x.left != null && x.compareTo(find) == 1 )
			closest( x.left, find );


	} 

	// unit testing of the methods (optional) 
	public static void main(String[] args) {

		// Point2D p = new Point2D(0.206107, 0.095492);
		// Point2D p2 = new Point2D(0.975528, 0.654508);
		// Point2D p3 = new Point2D(0.024472, 0.345492);
		// Point2D p4 = new Point2D(0.793893, 0.095492);
		// Point2D p5 = new Point2D(0.793893, 0.904508);
		// Point2D p6 = new Point2D(0.975528, 0.345492);
		// Point2D p7 = new Point2D(0.206107, 0.904508);
		// Point2D p8 = new Point2D(0.500000, 0.000000);
		// Point2D p9 = new Point2D(0.024472, 0.654508);
		// Point2D p10 = new Point2D(0.500000, 1.000000);
		Point2D p = new Point2D(0.5, 0.1);
		Point2D p2 = new Point2D(0.1, 0.8);
		Point2D p3 = new Point2D(0.9, 0.1);
		Point2D p4 = new Point2D(0.8, 0.5);
		Point2D p5 = new Point2D(0.55, 0.8);
		Point2D p6 = new Point2D(0.9, 0.345492);
		Point2D p7 = new Point2D(0.95, 0.904508);
		Point2D p8 = new Point2D(0.05, 0.000000);
		Point2D p9 = new Point2D(0.08, 0.654508);
		Point2D p10 = new Point2D(0.12, 1.000000);

		KdTree kdtree = new KdTree();
		PointSET brute = new PointSET();

		kdtree.insert(p);
		kdtree.insert(p2);
		kdtree.insert(p3);
		kdtree.insert(p4);
		kdtree.insert(p5);
		kdtree.insert(p6);
		kdtree.insert(p7);
		kdtree.insert(p8);
		kdtree.insert(p9);
		kdtree.insert(p10);

		brute.insert(p);
		brute.insert(p2);
		brute.insert(p3);
		brute.insert(p4);
		brute.insert(p5);
		brute.insert(p6);
		brute.insert(p7);
		brute.insert(p8);
		brute.insert(p9);
		brute.insert(p10);


		Iterable<Point2D> range = kdtree.range(new RectHV(0,0,0.3,1));

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        for (Point2D i : range) {
        	System.out.println(i);
            i.draw();
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.002);
		kdtree.root.right.right.rect.draw();

		// System.out.println(kdtree.nearest(new Point2D(0,0)));


        // while (true) {

        //     // the location (x, y) of the mouse
        //     double x = StdDraw.mouseX();
        //     double y = StdDraw.mouseY();
        //     Point2D query = new Point2D(x, y);

        //     // draw all of the points
        //     StdDraw.clear();
        //     StdDraw.setPenColor(StdDraw.BLACK);
        //     StdDraw.setPenRadius(0.02);
        //     kdtree.draw();

        //     // draw in red the nearest neighbor (using brute-force algorithm)
        //     StdDraw.setPenRadius(0.04);
        //     StdDraw.setPenColor(StdDraw.RED);
        //     brute.nearest(query).draw();
        //     StdDraw.setPenRadius(0.03);

        //     // draw in blue the nearest neighbor (using kd-tree algorithm)
        //     StdDraw.setPenColor(StdDraw.BLUE);
        //     kdtree.nearest(query).draw();
        //     StdDraw.show();
        //     // System.out.println(kdtree.it);
        //     StdDraw.pause(200);
        // }

		// if( kdtree.contains(p3) ) 
		// 	System.out.println( "something work" );

	}
}