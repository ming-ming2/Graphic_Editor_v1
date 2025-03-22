package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class GOval extends GShape {
	private int width;
	private int height;

	public GOval(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics g) {
		g.drawOval(point.x, point.y, width, height);

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
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(point.x, point.y, width, height);
	}

	@Override
	public boolean contains(Point p) {
		// 타원 내부 포함 여부 계산
		double a = width / 2.0;
		double b = height / 2.0;
		double centerX = point.x + a;
		double centerY = point.y + b;

		double normalizedX = (p.x - centerX) / a;
		double normalizedY = (p.y - centerY) / b;

		return (normalizedX * normalizedX + normalizedY * normalizedY) <= 1.0;
	}
}