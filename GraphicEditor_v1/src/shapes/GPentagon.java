package shapes;

import java.awt.Point;
import java.awt.Polygon;

public class GPentagon extends GPolygonShape {
	private static final long serialVersionUID = 1L;

	public GPentagon(Point point, int width, int height) {
		super(point, width, height);
	}

	@Override
	protected void updatePolygon() {
		int[] xPoints = new int[5];
		int[] yPoints = new int[5];

		int centerX = point.x + width / 2;
		int centerY = point.y + height / 2;

		int radiusX = width / 2;
		int radiusY = height / 2;

		for (int i = 0; i < 5; i++) {
			double angle = 2 * Math.PI * i / 5 - Math.PI / 2;
			xPoints[i] = (int) (centerX + radiusX * Math.cos(angle));
			yPoints[i] = (int) (centerY + radiusY * Math.sin(angle));
		}

		polygon = new Polygon(xPoints, yPoints, 5);
	}
}