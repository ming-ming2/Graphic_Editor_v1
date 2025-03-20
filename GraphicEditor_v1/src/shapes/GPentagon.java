package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class GPentagon extends GShape {
	private int width;
	private int height;
	private Polygon pentagonPolygon;

	public GPentagon(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
		updatePolygon();
	}

	private void updatePolygon() {
		int[] xPoints = new int[5];
		int[] yPoints = new int[5];

		int centerX = point.x + width / 2;
		int centerY = point.y + height / 2;
		int radius = Math.min(width, height) / 2;

		for (int i = 0; i < 5; i++) {
			double angle = 2 * Math.PI * i / 5 - Math.PI / 2;
			xPoints[i] = (int) (centerX + radius * Math.cos(angle));
			yPoints[i] = (int) (centerY + radius * Math.sin(angle));
		}

		pentagonPolygon = new Polygon(xPoints, yPoints, 5);
	}

	@Override
	public void draw(Graphics g) {
		updatePolygon();
		g.drawPolygon(pentagonPolygon);

		if (isSelected) {
			drawSelectionMarkers(g);
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
		return pentagonPolygon.contains(p);
	}
}