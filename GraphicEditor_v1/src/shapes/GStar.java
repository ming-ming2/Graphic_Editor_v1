package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class GStar extends GShape {
	private int width;
	private int height;
	private Polygon starPolygon;

	public GStar(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
		updatePolygon();
	}

	private void updatePolygon() {
		int centerX = point.x + width / 2;
		int centerY = point.y + height / 2;
		int outerRadius = Math.min(width, height) / 2;
		int innerRadius = outerRadius / 2;
		int points = 5;

		int[] xPoints = new int[points * 2];
		int[] yPoints = new int[points * 2];

		for (int i = 0; i < points * 2; i++) {
			double angle = Math.PI / points * i - Math.PI / 2;
			int radius = (i % 2 == 0) ? outerRadius : innerRadius;

			xPoints[i] = (int) (centerX + radius * Math.cos(angle));
			yPoints[i] = (int) (centerY + radius * Math.sin(angle));
		}

		starPolygon = new Polygon(xPoints, yPoints, points * 2);
	}

	@Override
	public void draw(Graphics g) {
		updatePolygon();
		g.drawPolygon(starPolygon);

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
		return starPolygon.contains(p);
	}
}
