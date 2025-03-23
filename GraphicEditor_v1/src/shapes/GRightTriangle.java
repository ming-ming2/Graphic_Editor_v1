package shapes;

import java.awt.Point;
import java.awt.Polygon;

public class GRightTriangle extends GPolygonShape {
	private static final long serialVersionUID = 1L;

	public GRightTriangle(Point point, int width, int height) {
		super(point, width, height);
	}

	@Override
	protected void updatePolygon() {
		int[] xPoints = { point.x, point.x + width, point.x };
		int[] yPoints = { point.y, point.y, point.y + height };

		polygon = new Polygon(xPoints, yPoints, 3);
	}
}