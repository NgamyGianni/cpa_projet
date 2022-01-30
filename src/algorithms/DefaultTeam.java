package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import supportGUI.Circle;
import supportGUI.Line;

public class DefaultTeam {

	// calculDiametre: ArrayList<Point> --> Line
	// renvoie une paire de points de la liste, de distance maximum.
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
	// renvoie une paire de points de la liste, de distance maximum.
	public Line calculDiametreOptimise(ArrayList<Point> points) {
		if (points.size() < 3) {
			return null;
		}

		ArrayList<Point> tmp = tri(points);

		return calculDiametre(tmp);
	}

	private ArrayList<Point> tri(ArrayList<Point> points) {
		int maxX = points.get(0).x;
		for (Point p : points)
			if (p.x > maxX)
				maxX = p.x;

		Point[] puitsMin = new Point[maxX + 1];
		Point[] puitsMax = new Point[maxX + 1];

		for (Point p : points) {
			if (puitsMin[p.x] == null)
				puitsMin[p.x] = p;
			else {
				if (puitsMin[p.x].y < p.y) {
					puitsMin[p.x] = p;
				}
			}
			if (puitsMax[p.x] == null)
				puitsMax[p.x] = p;
			else {
				if (puitsMax[p.x].y > p.y) {
					puitsMax[p.x] = p;
				}
			}
		}

		ArrayList<Point> result = new ArrayList<Point>();
		for (int i = 0; i < maxX + 1; i++)
			if (puitsMax[i] != null)
				result.add(puitsMax[i]);
		for (int i = maxX; i >= 0; i--)
			if (puitsMin[i] != null && !result.get(result.size() - 1).equals(puitsMin[i]))
				result.add(puitsMin[i]);

		if (result.get(result.size() - 1).equals(result.get(0)))
			result.remove(result.size() - 1);

		return result;
	}

	// calculCercleMin: ArrayList<Point> --> Circle
	// renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	public Circle calculCercleMin(ArrayList<Point> points) {
		if (points.isEmpty()) {
			return null;
		}

		// return minCircleNaif(points);

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

	private Circle b_md(ArrayList<Point> P, ArrayList<Point> R) {
		Circle D = null;

		if (P.size() == 0) {
			if (R.size() == 0)
				D = new Circle(new Point(), 0);
			if (R.size() == 1)
				D = new Circle(R.get(0), 0);
			if (R.size() == 2) {
				System.out.println("2");
				Point p = R.get(0);
				Point q = R.get(1);
				int x = (int) (p.getX() + q.getX()) / 2;
				int y = (int) (p.getY() + q.getY()) / 2;
				Point center = new Point(x, y);
				int radius = (int) Math.sqrt(
						(q.getX() - p.getX()) * (q.getX() - p.getX()) + (q.getY() - p.getY()) * (q.getY() - p.getY()));
				D = new Circle(center, radius);
			}
			if (R.size() == 3) {
				System.out.println("3");
				Point p = R.get(0);
				Point q = R.get(1);
				int x = (int) (p.getX() + q.getX()) / 2;
				int y = (int) (p.getY() + q.getY()) / 2;
				Point center = new Point(x, y);
				int radius = (int) Math.sqrt(
						(q.getX() - p.getX()) * (q.getX() - p.getX()) + (q.getY() - p.getY()) * (q.getY() - p.getY()));
				D = new Circle(center, radius);
			}
		}

		return D;
	}

	public Circle B_MINIDISK(ArrayList<Point> P, ArrayList<Point> R) {
		Circle D = null;
		Random random = new Random();

		// System.out.println("P = " + P.size());
		// System.out.println("R = " + R.size());
		//
		// if(P.size() == 0) {
		// return b_md(P, R);
		// }else {
		// Point p = P.get((random.nextInt(P.size())));
		// //ArrayList<Point> tmpP = new ArrayList<Point>();
		//
		// P.remove(p);
		// System.out.println("P1 = " + P.size());
		// System.out.println("R1 = " + R.size());
		// D = B_MINIDISK(P, R);
		//
		// if(Math.pow(D.getCenter().getX() - p.getX(), 2) +
		// Math.pow(D.getCenter().getY()-p.getY(), 2) > D.getRadius() * D.getRadius()) {
		// R.add(p);
		// D = B_MINIDISK(P, R);
		// }
		//
		// }
		// System.out.println("P = " + P.size());
		// System.out.println("R = " + R.size());
		// return D;
		//

		if (R.size() == 3) {
			D = UsefulFunction.CircleWith3Points(R.get(0), R.get(1), R.get(2));
		} else if (P.isEmpty() && R.size() == 2) {
			double x = (R.get(0).x + R.get(1).x) * 0.5;
			double y = (R.get(0).y + R.get(1).y) * 0.5;
			Point circleCenter = new Point((int) x, (int) y);
			int r = (int) distance2Points(circleCenter, R.get(0));
			D = new Circle(circleCenter, r);
		} else if (P.size() == 1 && R.isEmpty()) {
			Point centerCircle = new Point(P.get(0).x, P.get(0).y);
			D = new Circle(centerCircle, 0);
		} else if (P.size() == 1 && R.size() == 1) {
			double x = (R.get(0).x + R.get(0).x) * 0.5;
			double y = (R.get(0).y + R.get(0).y) * 0.5;
			Point circleCenter = new Point((int) x, (int) y);
			int r = (int) distance2Points(circleCenter, R.get(0));
			D = new Circle(circleCenter, r);
		} else {
			Point p = P.remove(random.nextInt(P.size()));
			D = B_MINIDISK(P, R);

			if (D != null && !containsPoint(p, D)) {
				R.add(p);
				D = B_MINIDISK(P, R);
				R.remove(p);
				P.add(p);
			}
		}

		return D;
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

	private double distanceCarre2Points(Point p1, Point p2) {
		final double distX = p1.x - p2.x;
		final double DY = p1.y - p2.y;

		return distX * distX + DY * DY;
	}

	private double distance2Points(Point p1, Point p2) {
		return Math.sqrt(distanceCarre2Points(p1, p2));
	}

	private boolean containsPoint(Point p, Circle D) {
		return distanceCarre2Points(p, D.getCenter()) <= D.getRadius() * D.getRadius();
	}

}
