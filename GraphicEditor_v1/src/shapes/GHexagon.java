package shapes;

import java.awt.Point;
import java.awt.Polygon;

public class GHexagon extends GPolygonShape {
	private static final long serialVersionUID = 1L;

	public GHexagon(Point point, int width, int height) {
		super(point, width, height);
	}

	@Override
	protected void updatePolygon() {
		int[] xPoints = new int[6];
		int[] yPoints = new int[6];

		int centerX = point.x + width / 2;
		int centerY = point.y + height / 2;
		int radiusX = width / 2;
		int radiusY = height / 2;

		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI * i / 6 - Math.PI / 2;
			xPoints[i] = (int) (centerX + radiusX * Math.cos(angle));
			yPoints[i] = (int) (centerY + radiusY * Math.sin(angle));
		}

		polygon = new Polygon(xPoints, yPoints, 6);
	}
}
