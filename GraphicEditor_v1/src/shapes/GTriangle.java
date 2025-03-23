package shapes;

import java.awt.Point;
import java.awt.Polygon;

public class GTriangle extends GPolygonShape {
	private static final long serialVersionUID = 1L;

	public GTriangle(Point point, int width, int height) {
		super(point, width, height);
	}

	@Override
	protected void updatePolygon() {
		int[] xPoints = { point.x + width / 2, point.x + width, point.x };
		int[] yPoints = { point.y, point.y + height, point.y + height };

		polygon = new Polygon(xPoints, yPoints, 3);
	}
}