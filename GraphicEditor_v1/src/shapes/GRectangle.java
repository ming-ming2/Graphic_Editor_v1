package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class GRectangle extends GShape {
	private int width;
	private int height;

	public GRectangle(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics g) {
		g.drawRect(point.x, point.y, width, height);

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
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(point.x, point.y, width, height);
	}
}