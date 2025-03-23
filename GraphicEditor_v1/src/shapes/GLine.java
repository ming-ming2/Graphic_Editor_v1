package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class GLine extends GShape {
	private static final long serialVersionUID = 1L;

	public Point endPoint;

	public GLine(Point startPoint, Point endPoint) {
		this.point = startPoint;
		this.endPoint = endPoint;
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform oldTransform = null;

		if (rotationAngle != 0) {
			Rectangle bounds = getBounds();
			int centerX = bounds.x + bounds.width / 2;
			int centerY = bounds.y + bounds.height / 2;

			oldTransform = g2d.getTransform();
			g2d.translate(centerX, centerY);
			g2d.rotate(rotationAngle);
			g2d.translate(-centerX, -centerY);
		}

		g2d.drawLine(point.x, point.y, endPoint.x, endPoint.y);

		if (oldTransform != null) {
			g2d.setTransform(oldTransform);
		}

		if (isSelected) {
			drawSelectionBox(g);
		}
	}

	@Override
	public boolean isInside(Rectangle rect) {
		return rect.contains(point) && rect.contains(endPoint);
	}

	@Override
	public void move(int dx, int dy) {
		point.x += dx;
		point.y += dy;
		endPoint.x += dx;
		endPoint.y += dy;
	}

	@Override
	public Rectangle getBounds() {
		int x = Math.min(point.x, endPoint.x);
		int y = Math.min(point.y, endPoint.y);
		int width = Math.abs(endPoint.x - point.x);
		int height = Math.abs(endPoint.y - point.y);

		if (width == 0)
			width = 1;
		if (height == 0)
			height = 1;

		return new Rectangle(x, y, width, height);
	}

	@Override
	public boolean contains(Point p) {
		if (rotationAngle != 0) {
			Rectangle bounds = getBounds();
			int centerX = bounds.x + bounds.width / 2;
			int centerY = bounds.y + bounds.height / 2;

			double cos = Math.cos(-rotationAngle);
			double sin = Math.sin(-rotationAngle);
			int dx = p.x - centerX;
			int dy = p.y - centerY;

			int rotatedX = (int) (dx * cos - dy * sin) + centerX;
			int rotatedY = (int) (dx * sin + dy * cos) + centerY;

			Point rotatedPoint = new Point(rotatedX, rotatedY);
			return isOnLine(rotatedPoint);
		}

		return isOnLine(p);
	}

	private boolean isOnLine(Point p) {
		final int TOLERANCE = 5;

		if (point.x == endPoint.x && point.y == endPoint.y) {
			return p.distance(point) <= TOLERANCE;
		}

		double lineLength = point.distance(endPoint);
		double distanceSum = p.distance(point) + p.distance(endPoint);

		return Math.abs(distanceSum - lineLength) <= TOLERANCE;
	}

	@Override
	public void resize(ControlPoint controlPoint, int dx, int dy) {
		switch (controlPoint) {
		case TOP_LEFT:
			if (point.x <= endPoint.x && point.y <= endPoint.y) {
				point.x += dx;
				point.y += dy;
			} else if (point.x > endPoint.x && point.y > endPoint.y) {
				endPoint.x += dx;
				endPoint.y += dy;
			} else if (point.x <= endPoint.x && point.y > endPoint.y) {
				point.x += dx;
				endPoint.y += dy;
			} else {
				endPoint.x += dx;
				point.y += dy;
			}
			break;

		case TOP_RIGHT:
			if (point.x <= endPoint.x && point.y <= endPoint.y) {
				endPoint.x += dx;
				point.y += dy;
			} else if (point.x > endPoint.x && point.y > endPoint.y) {
				point.x += dx;
				endPoint.y += dy;
			} else if (point.x <= endPoint.x && point.y > endPoint.y) {
				endPoint.x += dx;
				endPoint.y += dy;
			} else {
				point.x += dx;
				point.y += dy;
			}
			break;

		case BOTTOM_LEFT:
			if (point.x <= endPoint.x && point.y <= endPoint.y) {
				point.x += dx;
				endPoint.y += dy;
			} else if (point.x > endPoint.x && point.y > endPoint.y) {
				endPoint.x += dx;
				point.y += dy;
			} else if (point.x > endPoint.x && point.y <= endPoint.y) {
				endPoint.x += dx;
				endPoint.y += dy;
			} else {
				point.x += dx;
				point.y += dy;
			}
			break;

		case BOTTOM_RIGHT:
			if (point.x <= endPoint.x && point.y <= endPoint.y) {
				endPoint.x += dx;
				endPoint.y += dy;
			} else if (point.x > endPoint.x && point.y > endPoint.y) {
				point.x += dx;
				point.y += dy;
			} else if (point.x > endPoint.x && point.y <= endPoint.y) {
				point.x += dx;
				endPoint.y += dy;
			} else {
				endPoint.x += dx;
				point.y += dy;
			}
			break;

		case TOP:
			if (point.y <= endPoint.y) {
				point.y += dy;
			} else {
				endPoint.y += dy;
			}
			break;

		case BOTTOM:
			if (point.y <= endPoint.y) {
				endPoint.y += dy;
			} else {
				point.y += dy;
			}
			break;

		case LEFT:
			if (point.x <= endPoint.x) {
				point.x += dx;
			} else {
				endPoint.x += dx;
			}
			break;

		case RIGHT:
			if (point.x <= endPoint.x) {
				endPoint.x += dx;
			} else {
				point.x += dx;
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void setFromBounds(Rectangle bounds) {
		Rectangle oldBounds = getBounds();

		boolean startIsLeft = point.x <= endPoint.x;
		boolean startIsTop = point.y <= endPoint.y;

		int dx = bounds.x - oldBounds.x;
		int dw = bounds.width - oldBounds.width;
		int dy = bounds.y - oldBounds.y;
		int dh = bounds.height - oldBounds.height;

		if (startIsLeft) {
			point.x += dx;
			endPoint.x = point.x + bounds.width;
		} else {
			endPoint.x += dx;
			point.x = endPoint.x + bounds.width;
		}

		if (startIsTop) {
			point.y += dy;
			endPoint.y = point.y + bounds.height;
		} else {
			endPoint.y += dy;
			point.y = endPoint.y + bounds.height;
		}
	}
}
