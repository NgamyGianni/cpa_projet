package algorithms;

import java.awt.Point;

import supportGUI.Circle;

public class UsefulFunction {

	public static Circle CircleWith3Points(Point p1, Point p2, Point p3) {
		Point center = null;
		double radius = 0;
		double p2Subp1Y = p2.y - p1.y;
		double p3Subp2Y =  p3.y - p2.y;

		if (p2Subp1Y == 0 || p3Subp2Y == 0) {
			center = new Point(0, 0);
			radius = 0;
		}
		else {
			double a = -(p2.x - p1.x) / p2Subp1Y;
			double aPrime = -(p3.x - p2.x) / p3Subp2Y;
			double aprimeSuba = aPrime - a;
			
			if (aprimeSuba == 0) {
				center = new Point(0, 0);
				radius = 0;
			}
			else {
				double p2CarreX = p2.x * p2.x;
				double p2CarreY = p2.y * p2.y;

				
				double b = (p2CarreX - p1.x * p1.x + p2CarreY - p1.y * p1.y) /
						         (2.0 * p2Subp1Y);
				double bPrime = (p3.x * p3.x - p2CarreX + p3.y * p3.y - p2CarreY) /
						               (2.0 * p3Subp2Y);
				
		
				double centerX = (b - bPrime) / aprimeSuba;
				double centerY = a * centerX + b;
				
				double distanceCenterX = p1.x - centerX;
				double DYC = p1.y - centerY;
				
				center = new Point((int)centerX, (int)centerY);
				radius = Math.sqrt(distanceCenterX * distanceCenterX + DYC * DYC);
			}
		}
		return new Circle(center, (int)radius);
	}
	
	
}
