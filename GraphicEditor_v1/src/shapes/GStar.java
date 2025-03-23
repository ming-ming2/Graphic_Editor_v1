package shapes;

import java.awt.Point;
import java.awt.Polygon;

public class GStar extends GPolygonShape {
	private static final long serialVersionUID = 1L;

	public GStar(Point point, int width, int height) {
		super(point, width, height);
	}

	@Override
	protected void updatePolygon() {
		int centerX = point.x + width / 2;
		int centerY = point.y + height / 2;

		int outerRadiusX = width / 2;
		int outerRadiusY = height / 2;
		int innerRadiusX = outerRadiusX / 2;
		int innerRadiusY = outerRadiusY / 2;

		int points = 5;

		int[] xPoints = new int[points * 2];
		int[] yPoints = new int[points * 2];

		for (int i = 0; i < points * 2; i++) {
			double angle = Math.PI / points * i - Math.PI / 2;
			int radiusX = (i % 2 == 0) ? outerRadiusX : innerRadiusX;
			int radiusY = (i % 2 == 0) ? outerRadiusY : innerRadiusY;

			xPoints[i] = (int) (centerX + radiusX * Math.cos(angle));
			yPoints[i] = (int) (centerY + radiusY * Math.sin(angle));
		}

		polygon = new Polygon(xPoints, yPoints, points * 2);
	}
}