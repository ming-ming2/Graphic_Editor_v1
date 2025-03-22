package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class GHexagon extends GShape {

	private static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private Polygon hexagonPolygon;

	public GHexagon(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
		updatePolygon();
	}

	private void updatePolygon() {
		int[] xPoints = new int[6];
		int[] yPoints = new int[6];

		int centerX = point.x + width / 2;
		int centerY = point.y + height / 2;

		int radiusX = width / 2;
		int radiusY = height / 2;

		for (int i = 0; i < 6; i++) {
			double angle = 2 * Math.PI * i / 6;
			xPoints[i] = (int) (centerX + radiusX * Math.cos(angle));
			yPoints[i] = (int) (centerY + radiusY * Math.sin(angle));
		}

		hexagonPolygon = new Polygon(xPoints, yPoints, 6);
	}

	@Override
	public void draw(Graphics g) {
		updatePolygon();
		g.drawPolygon(hexagonPolygon);

		if (isSelected) {
			drawSelectionBox(g);
		}
	}

	@Override
	public boolean isInside(Rectangle rect) {
		return rect.contains(getBounds());
	}

	@Override
	public void move(int dx, int dy) {
		point.x += dx;
		point.y += dy;
		updatePolygon();
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(point.x, point.y, width, height);
	}

	@Override
	public boolean contains(Point p) {
		return hexagonPolygon.contains(p);
	}
}
