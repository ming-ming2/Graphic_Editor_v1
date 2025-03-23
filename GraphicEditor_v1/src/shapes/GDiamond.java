package shapes;

import java.awt.Point;
import java.awt.Polygon;

public class GDiamond extends GPolygonShape {
	private static final long serialVersionUID = 1L;

	public GDiamond(Point point, int width, int height) {
		super(point, width, height);
	}

	@Override
	protected void updatePolygon() {
		int[] xPoints = { point.x + width / 2, point.x + width, point.x + width / 2, point.x };
		int[] yPoints = { point.y, point.y + height / 2, point.y + height, point.y + height / 2 };

		polygon = new Polygon(xPoints, yPoints, 4);
	}
}