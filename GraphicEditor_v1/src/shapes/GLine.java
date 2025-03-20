package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class GLine extends GShape {
	private Point endPoint;

	public GLine(Point startPoint, Point endPoint) {
		this.point = startPoint;
		this.endPoint = endPoint;
	}

	@Override
	public void draw(Graphics g) {
		g.drawLine(point.x, point.y, endPoint.x, endPoint.y);

		if (isSelected) {
			drawSelectionMarkers(g);
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

		// 선의 경우 두께가 없으므로 최소 크기 보장
		if (width == 0)
			width = 1;
		if (height == 0)
			height = 1;

		return new Rectangle(x, y, width, height);
	}
}