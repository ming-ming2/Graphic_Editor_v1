package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class GOval extends GShape {
	private static final long serialVersionUID = 1L;
	private int width;
	private int height;

	public GOval(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		if (rotationAngle != 0)
			applyRotationTransform(g2d, getBounds());
		g2d.drawOval(point.x, point.y, width, height);
		g2d.dispose();
		if (isSelected)
			drawSelectionBox(g);
	}

	@Override
	public boolean isInside(Rectangle rect) {
		return rect.contains(getBounds());
	}

	@Override
	public void move(int dx, int dy) {
		point.translate(dx, dy);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(point.x, point.y, width, height);
	}

	@Override
	public boolean contains(Point p) {
		Point rp = (rotationAngle != 0) ? reverseRotatePoint(p, getBounds()) : p;
		double a = width / 2.0;
		double b = height / 2.0;
		double cx = point.x + a;
		double cy = point.y + b;
		double nx = (rp.x - cx) / a;
		double ny = (rp.y - cy) / b;
		return (nx * nx + ny * ny) <= 1.0;
	}

	@Override
	public void resize(ControlPoint cp, int dx, int dy) {
		final int MIN = 10;
		int newW = width, newH = height;
		int newX = point.x, newY = point.y;

		switch (cp) {
		case TOP_LEFT -> {
			newW -= dx;
			newH -= dy;
			if (newW > MIN && newH > MIN) {
				newX += dx;
				newY += dy;
			}
		}
		case TOP -> {
			newH -= dy;
			if (newH > MIN)
				newY += dy;
		}
		case TOP_RIGHT -> {
			newW += dx;
			newH -= dy;
			if (newW > MIN && newH > MIN)
				newY += dy;
		}
		case RIGHT -> newW += dx;
		case BOTTOM_RIGHT -> {
			newW += dx;
			newH += dy;
		}
		case BOTTOM -> newH += dy;
		case BOTTOM_LEFT -> {
			newW -= dx;
			newH += dy;
			if (newW > MIN && newH > MIN)
				newX += dx;
		}
		case LEFT -> {
			newW -= dx;
			if (newW > MIN)
				newX += dx;
		}
		default -> {
		}
		}

		if (newW > MIN)
			width = newW;
		if (newH > MIN)
			height = newH;
		point.setLocation(newX, newY);
	}

	@Override
	public void setFromBounds(Rectangle bounds) {
		this.point = new Point(bounds.x, bounds.y);
		this.width = bounds.width;
		this.height = bounds.height;
	}
}
