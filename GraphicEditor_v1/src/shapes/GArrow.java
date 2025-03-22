package shapes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class GArrow extends GShape {
	private Point endPoint;
	private final int ARROW_SIZE = 10;

	public GArrow(Point startPoint, Point endPoint) {
		this.point = startPoint;
		this.endPoint = endPoint;
	}

	@Override
	public void draw(Graphics g) {
		// 선 그리기
		g.drawLine(point.x, point.y, endPoint.x, endPoint.y);

		// 화살표 머리 그리기
		double angle = Math.atan2(endPoint.y - point.y, endPoint.x - point.x);
		int x1 = (int) (endPoint.x - ARROW_SIZE * Math.cos(angle + Math.PI / 6));
		int y1 = (int) (endPoint.y - ARROW_SIZE * Math.sin(angle + Math.PI / 6));
		int x2 = (int) (endPoint.x - ARROW_SIZE * Math.cos(angle - Math.PI / 6));
		int y2 = (int) (endPoint.y - ARROW_SIZE * Math.sin(angle - Math.PI / 6));

		g.drawLine(endPoint.x, endPoint.y, x1, y1);
		g.drawLine(endPoint.x, endPoint.y, x2, y2);

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

		// 선의 경우 두께가 없으므로 최소 크기 보장
		if (width == 0)
			width = 1;
		if (height == 0)
			height = 1;

		return new Rectangle(x, y, width, height);
	}

	@Override
	public boolean contains(Point p) {
		// 선 영역에 포함되는지 계산 (선에서의 거리가 5픽셀 이내면 포함으로 간주)
		final int TOLERANCE = 5;

		// 두 끝점이 같은 경우
		if (point.x == endPoint.x && point.y == endPoint.y) {
			return p.distance(point) <= TOLERANCE;
		}

		// 선분의 길이 제곱
		double lineLength = point.distance(endPoint);

		// 두 점으로부터의 거리 합이 선분 길이와 가까우면 선 위에 있다고 간주
		double distanceSum = p.distance(point) + p.distance(endPoint);

		// 허용 오차를 추가한 비교
		return Math.abs(distanceSum - lineLength) <= TOLERANCE;
	}
}