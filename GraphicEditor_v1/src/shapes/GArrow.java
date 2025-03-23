package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class GArrow extends GShape {
	private static final long serialVersionUID = 1L;
	public Point endPoint;
	private final int ARROW_SIZE = 10;

	public GArrow(Point startPoint, Point endPoint) {
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

			// 회전 변환 적용
			g2d.translate(centerX, centerY);
			g2d.rotate(rotationAngle);
			g2d.translate(-centerX, -centerY);
		}

		// 선 그리기
		g2d.drawLine(point.x, point.y, endPoint.x, endPoint.y);

		// 화살표 머리 그리기
		double angle = Math.atan2(endPoint.y - point.y, endPoint.x - point.x);
		int x1 = (int) (endPoint.x - ARROW_SIZE * Math.cos(angle + Math.PI / 6));
		int y1 = (int) (endPoint.y - ARROW_SIZE * Math.sin(angle + Math.PI / 6));
		int x2 = (int) (endPoint.x - ARROW_SIZE * Math.cos(angle - Math.PI / 6));
		int y2 = (int) (endPoint.y - ARROW_SIZE * Math.sin(angle - Math.PI / 6));

		g2d.drawLine(endPoint.x, endPoint.y, x1, y1);
		g2d.drawLine(endPoint.x, endPoint.y, x2, y2);

		// 변환 복원
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

		// 선의 경우 두께가 없으므로 최소 크기 보장
		if (width == 0)
			width = 1;
		if (height == 0)
			height = 1;

		return new Rectangle(x, y, width, height);
	}

	@Override
	public boolean contains(Point p) {
		if (rotationAngle != 0) {
			// 회전이 적용된 경우 점 p를 역회전시켜 확인
			Rectangle bounds = getBounds();
			int centerX = bounds.x + bounds.width / 2;
			int centerY = bounds.y + bounds.height / 2;

			// p를 회전 중심 기준으로 역회전
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

	@Override
	public void resize(ControlPoint controlPoint, int dx, int dy) {
		switch (controlPoint) {
		case TOP_LEFT:
			// 시작점이 오른쪽 아래인 경우
			if (point.x <= endPoint.x && point.y <= endPoint.y) {
				point.x += dx;
				point.y += dy;
			}
			// 시작점이 왼쪽 위인 경우
			else if (point.x > endPoint.x && point.y > endPoint.y) {
				endPoint.x += dx;
				endPoint.y += dy;
			}
			// 시작점이 오른쪽 위인 경우
			else if (point.x <= endPoint.x && point.y > endPoint.y) {
				point.x += dx;
				endPoint.y += dy;
			}
			// 시작점이 왼쪽 아래인 경우
			else {
				endPoint.x += dx;
				point.y += dy;
			}
			break;

		case TOP_RIGHT:
			// 시작점이 왼쪽 아래인 경우
			if (point.x <= endPoint.x && point.y <= endPoint.y) {
				endPoint.x += dx;
				point.y += dy;
			}
			// 시작점이 오른쪽 위인 경우
			else if (point.x > endPoint.x && point.y > endPoint.y) {
				point.x += dx;
				endPoint.y += dy;
			}
			// 시작점이 왼쪽 위인 경우
			else if (point.x <= endPoint.x && point.y > endPoint.y) {
				endPoint.x += dx;
				endPoint.y += dy;
			}
			// 시작점이 오른쪽 아래인 경우
			else {
				point.x += dx;
				point.y += dy;
			}
			break;

		case BOTTOM_LEFT:
			// 시작점이 오른쪽 위인 경우
			if (point.x <= endPoint.x && point.y <= endPoint.y) {
				point.x += dx;
				endPoint.y += dy;
			}
			// 시작점이 왼쪽 아래인 경우
			else if (point.x > endPoint.x && point.y > endPoint.y) {
				endPoint.x += dx;
				point.y += dy;
			}
			// 시작점이 왼쪽 위인 경우
			else if (point.x > endPoint.x && point.y <= endPoint.y) {
				endPoint.x += dx;
				endPoint.y += dy;
			}
			// 시작점이 오른쪽 아래인 경우
			else {
				point.x += dx;
				point.y += dy;
			}
			break;

		case BOTTOM_RIGHT:
			// 시작점이 왼쪽 위인 경우
			if (point.x <= endPoint.x && point.y <= endPoint.y) {
				endPoint.x += dx;
				endPoint.y += dy;
			}
			// 시작점이 오른쪽 아래인 경우
			else if (point.x > endPoint.x && point.y > endPoint.y) {
				point.x += dx;
				point.y += dy;
			}
			// 시작점이 오른쪽 위인 경우
			else if (point.x > endPoint.x && point.y <= endPoint.y) {
				point.x += dx;
				endPoint.y += dy;
			}
			// 시작점이 왼쪽 아래인 경우
			else {
				endPoint.x += dx;
				point.y += dy;
			}
			break;

		case TOP:
			// y축 방향으로만 리사이즈
			if (point.y <= endPoint.y) {
				point.y += dy;
			} else {
				endPoint.y += dy;
			}
			break;

		case BOTTOM:
			// y축 방향으로만 리사이즈
			if (point.y <= endPoint.y) {
				endPoint.y += dy;
			} else {
				point.y += dy;
			}
			break;

		case LEFT:
			// x축 방향으로만 리사이즈
			if (point.x <= endPoint.x) {
				point.x += dx;
			} else {
				endPoint.x += dx;
			}
			break;

		case RIGHT:
			// x축 방향으로만 리사이즈
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
		// 원래 선의 형태 및 방향 보존
		Rectangle oldBounds = getBounds();

		// 선의 방향 확인
		boolean startIsLeft = point.x <= endPoint.x;
		boolean startIsTop = point.y <= endPoint.y;

		// X축 처리
		int dx = bounds.x - oldBounds.x;
		int dw = bounds.width - oldBounds.width;

		// Y축 처리
		int dy = bounds.y - oldBounds.y;
		int dh = bounds.height - oldBounds.height;

		// 방향에 따라 시작점과 끝점 조정
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