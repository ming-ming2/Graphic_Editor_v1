package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class GTriangle extends GShape {
	private int width;
	private int height;
	private Polygon trianglePolygon;

	public GTriangle(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
		updatePolygon();
	}

	private void updatePolygon() {
		int[] xPoints = { point.x + width / 2, // 상단 중앙
				point.x + width, // 우측 하단
				point.x // 좌측 하단
		};

		int[] yPoints = { point.y, // 상단 중앙
				point.y + height, // 우측 하단
				point.y + height // 좌측 하단
		};

		trianglePolygon = new Polygon(xPoints, yPoints, 3);
	}

	@Override
	public void draw(Graphics g) {
		updatePolygon();
		g.drawPolygon(trianglePolygon);

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
		return trianglePolygon.contains(p);
	}
}
