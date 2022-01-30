package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

	// calculDiametre: ArrayList<Point> --> Line
	// renvoie une pair de points de la liste, de distance maximum.
	public Line calculDiametre(ArrayList<Point> points) {
		if (points.size() < 3) {
			return null;
		}

		Point p = points.get(0);
		Point q = points.get(1);

		double distance = Math
				.sqrt((q.getX() - p.getX()) * (q.getX() - p.getX()) + (q.getY() - p.getY()) * (q.getY() - p.getY()));
		double distanceMax = distance;

		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				Point tmpP = points.get(i);
				Point tmpQ = points.get(j);
				distance = Math.sqrt((tmpQ.getX() - tmpP.getX()) * (tmpQ.getX() - tmpP.getX())
						+ (tmpQ.getY() - tmpP.getY()) * (tmpQ.getY() - tmpP.getY()));
				if (distance > distanceMax) {
					distanceMax = distance;
					p = tmpP;
					q = tmpQ;
				}
			}
		}

		return new Line(p, q);
	}

	// calculDiametreOptimise: ArrayList<Point> --> Line
	// renvoie une pair de points de la liste, de distance maximum.
	public Line calculDiametreOptimise(ArrayList<Point> points) {
		if (points.size() < 3) {
			return null;
		}

		ArrayList<Point> tmp = tri(points);

		return calculDiametre(tmp);
	}

	private ArrayList<Point> tri(ArrayList<Point> points) {
		if (points.size()<4) return points;
        int maxX=points.get(0).x;
        for (Point p: points) if (p.x>maxX) maxX=p.x;
        Point[] maxY = new Point[maxX+1];
        Point[] minY = new Point[maxX+1];
        for (Point p: points) {
            if (maxY[p.x]==null||p.y>maxY[p.x].y) maxY[p.x]=p;
            if (minY[p.x]==null||p.y<minY[p.x].y) minY[p.x]=p;
        }
        ArrayList<Point> result = new ArrayList<Point>();
        for (int i=0;i<maxX+1;i++) if (maxY[i]!=null) result.add(maxY[i]);
        for (int i=maxX;i>=0;i--) if (minY[i]!=null && !result.get(result.size()-1).equals(minY[i])) result.add(minY[i]);

        if (result.get(result.size()-1).equals(result.get(0))) result.remove(result.size()-1);

        return result;
	}

	// 1 ET 2
	// calculCercleMin: ArrayList<Point> --> Circle
	// renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	public Circle calculCercleMin(ArrayList<Point> points) {
		if (points.isEmpty()) {
			return null;
		}
		// REPONSE A LA QUESTION 1
		System.out.println("size points "+tri(points).size());
		//return minCircleNaif(points);

		// 2 REPONSE A LA QUESTION 2
		return B_MINIDISK(tri(points), new ArrayList<Point>());

	}

	private Circle minCircleNaif(ArrayList<Point> points) {
		
		Point center = points.get(0).getLocation();
		int radius = 100;
		Circle c = new Circle(center, radius);

		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				Point p = points.get(i);
				Point q = points.get(j);
				int x = (int) (p.getX() + q.getX()) / 2;
				int y = (int) (p.getY() + q.getY()) / 2;
				radius = (int) Math.sqrt(
						(q.getX() - p.getX()) * (q.getX() - p.getX()) + (q.getY() - p.getY()) * (q.getY() - p.getY()));
				center.setLocation(x, y);
				c = new Circle(center, radius);
				boolean allPoints = true;
				for (Point p1 : points)
					if (Math.pow(center.getX() - p1.getX(), 2) + Math.pow(center.getY() - p1.getY(), 2) > radius
							* radius) {
						allPoints = false;
						break;
					}
				if (allPoints)
					return c;
			}
		}
		return new Circle(center, radius);
	}

	// enveloppeConvexe: ArrayList<Point> --> ArrayList<Point>
	// renvoie l'enveloppe convexe de la liste.
	public ArrayList<Point> enveloppeConvexe(ArrayList<Point> points) {
		if (points.size() < 4)
			return points;

		ArrayList<Point> enveloppe = new ArrayList<Point>();

		for (Point p : points) {
			for (Point q : points) {
				if (p.equals(q))
					continue;
				Point ref = p;
				for (Point r : points)
					if (crossProduct(p, q, p, r) != 0) {
						ref = r;
						break;
					}
				if (ref.equals(p)) {
					enveloppe.add(p);
					enveloppe.add(q);
					continue;
				}
				double signeRef = crossProduct(p, q, p, ref);
				boolean estCote = true;
				for (Point r : points)
					if (signeRef * crossProduct(p, q, p, r) < 0) {
						estCote = false;
						break;
					} // ici sans le break le temps de calcul devient horrible
				if (estCote) {
					enveloppe.add(p);
					enveloppe.add(q);
				}
			}
		}

		return enveloppe; // ici l'enveloppe n'est pas trie dans le sens trigonometrique, et contient des
							// doublons, mais tant pis!
	}

	private double crossProduct(Point p, Point q, Point s, Point t) {
		return ((q.x - p.x) * (t.y - s.y) - (q.y - p.y) * (t.x - s.x));
	}

	private double dist(Point p, Point q) {
		return Math.sqrt((p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y));
	}

	private boolean isInside(Circle c, Point p) {
		return dist(c.getCenter(), p) <= c.getRadius();
	}

	private Point get_circle_center(double bx, double by, double cx, double cy) {
		double B = bx * bx + by * by;
		double C = cx * cx + cy * cy;
		double D = bx * cy - by * cx;
		// int centerX=(int)((cy * B - by * C) / (2.0 * D));
		Point center = new Point((int) ((cy * B - by * C) / (2.0 * D)), (int) ((bx * C - cx * B) / (2.0 * D)));
		return center;
	}

	private Circle circle_from(Point A, Point B, Point C) {
		Point I = get_circle_center(B.x - A.x, B.y - A.y,
				C.x - A.x, C.y - A.y);
		System.out.println("value before I.x "+I.x+" I.y "+I.y);
		I.x += A.x;
		I.y += A.y;
		System.out.println("value after I.x "+I.x+" I.y "+I.y);
		return new Circle(I, (int) (dist(I, A)));

	}

	private Circle circle_from(Point A, Point B) {
		// Set the center to be the midpoint of A and B
		Point C = new Point((int) ((A.x + B.x) / 2.0), (int) ((A.y + B.y) / 2.0));

		// Set the radius to be half the distance AB
		return new Circle(C, (int) (dist(A, B) / 2.0));
	}

	// Function to check whether a circle
	// encloses the given points
	private boolean is_valid_circle(Circle c, ArrayList<Point> P) {
		// Iterating through all the points
		// to check whether the points
		// lie inside the circle or not
		for (Point p : P)
			if (!isInside(c, p))
				return false;
		return true;
	}

	// Function to return the minimum enclosing
	// circle for N <= 3
	private Circle min_circle_trivial(ArrayList<Point> P) {
		assert (P.size() <= 3);
		if (P.size() == 0) {
			return new Circle(new Point(), 0);
		} else if (P.size() == 1) {
			return new Circle(P.get(0), 0);
		} else if (P.size() == 2) {
			return circle_from(P.get(0), P.get(1));
		}

		// To check if MEC can be determined
		// by 2 points only
		for (int i = 0; i < 3; i++) {
			for (int j = i + 1; j < 3; j++) {

				Circle c = circle_from(P.get(i), P.get(j));
				if (is_valid_circle(c, P))
					return c;
			}
		}
		return circle_from(P.get(0), P.get(1), P.get(2));
	}

	public Circle B_MINIDISK(ArrayList<Point> P, ArrayList<Point> R) {
		// Circle D = null;
		Random random = new Random();

		// System.out.println("P size = " + P.size());
		// System.out.println("R size = " + R.size());

		if (P.size()==0 || R.size() == 3 || R.size()==2){return min_circle_trivial(R);}
		else if(P.size() == 0 && R.size() == 2){return min_circle_trivial(R);}
		else if(P.size() == 1 && R.size() == 0){return min_circle_trivial(P);}
		else if(P.size() == 1 && R.size() == 1){
			R.add(P.get(0));
			return min_circle_trivial(R);}
		Point p = P.get((random.nextInt(P.size())));
		// ArrayList<Point> tmpP = new ArrayList<Point>();

		P.remove(p);
		System.out.println("P1 = " + P.size());
		System.out.println("R1 = " + R.size());
		Circle D = B_MINIDISK(P, R);

		if (isInside(D, p)) {
			System.out.println("I finished");
			return D;
		}
		R.add(p);

		// System.out.println("P = " + P.size());
		// System.out.println("R = " + R.size());
		return B_MINIDISK(P, R);
	}

	// Gianni

	// private Circle b_md(ArrayList<Point> P, ArrayList<Point> R) {
	// Circle D = null;

	// if (P.size() == 0) {
	// if (R.size() == 0)
	// D = new Circle(new Point(), 0);
	// if (R.size() == 1)
	// D = new Circle(R.get(0), 0);
	// if (R.size() == 2) {
	// System.out.println("2");
	// Point p = R.get(0);
	// Point q = R.get(1);
	// int x = (int) (p.getX() + q.getX()) / 2;
	// int y = (int) (p.getY() + q.getY()) / 2;
	// Point center = new Point(x, y);
	// int radius = (int) Math.sqrt(
	// (q.getX() - p.getX()) * (q.getX() - p.getX()) + (q.getY() - p.getY()) *
	// (q.getY() - p.getY()));
	// D = new Circle(center, radius);
	// }
	// if (R.size() == 3) {
	// System.out.println("3");
	// Point p = R.get(0);
	// Point q = R.get(1);
	// int x = (int) (p.getX() + q.getX()) / 2;
	// int y = (int) (p.getY() + q.getY()) / 2;
	// Point center = new Point(x, y);
	// int radius = (int) Math.sqrt(
	// (q.getX() - p.getX()) * (q.getX() - p.getX()) + (q.getY() - p.getY()) *
	// (q.getY() - p.getY()));
	// D = new Circle(center, radius);
	// }
	// }

	// return D;
	// }

	// public Circle B_MINIDISK(ArrayList<Point> P, ArrayList<Point> R) {
	// //Circle D = null;
	// Random random = new Random();

	// System.out.println("P = " + P.size());
	// System.out.println("R = " + R.size());

	// if (P.size() == 0) {
	// return b_md(P, R);
	// } else {
	// Point p = P.get((random.nextInt(P.size())));
	// // ArrayList<Point> tmpP = new ArrayList<Point>();

	// P.remove(p);
	// System.out.println("P1 = " + P.size());
	// System.out.println("R1 = " + R.size());
	// D = B_MINIDISK(P, R);

	// if (Math.pow(D.getCenter().getX() - p.getX(), 2)
	// + Math.pow(D.getCenter().getY() - p.getY(), 2) > D.getRadius() *
	// D.getRadius()) {
	// R.add(p);
	// D = B_MINIDISK(P, R);
	// }

	// }
	// System.out.println("P = " + P.size());
	// System.out.println("R = " + R.size());
	// return D;
	// }

}
