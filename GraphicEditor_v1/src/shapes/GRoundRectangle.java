package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class GRoundRectangle extends GShape {
	private int width;
	private int height;
	private final int arcSize = 20; // 둥근 모서리의 크기

	public GRoundRectangle(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics g) {
		g.drawRoundRect(point.x, point.y, width, height, arcSize, arcSize);

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

	@Override
	public boolean contains(Point p) {
		// 기본적으로 사각형 영역 체크
		if (!getBounds().contains(p)) {
			return false;
		}

		// 모서리 부분은 더 정확한 계산이 필요할 수 있으나,
		// 단순화를 위해 사각형 영역으로 처리
		return true;
	}
}
