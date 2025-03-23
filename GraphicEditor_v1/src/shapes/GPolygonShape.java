package shapes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public abstract class GPolygonShape extends GShape {
	protected int width;
	protected int height;
	protected Polygon polygon;

	public GPolygonShape(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
		updatePolygon();
	}

	protected abstract void updatePolygon();

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create(); // 새 그래픽 컨텍스트 생성

		// 회전 변환 적용
		if (rotationAngle != 0) {
			Rectangle bounds = getBounds();
			int centerX = bounds.x + bounds.width / 2;
			int centerY = bounds.y + bounds.height / 2;

			// 회전 변환 적용
			g2d.translate(centerX, centerY);
			g2d.rotate(rotationAngle);
			g2d.translate(-centerX, -centerY);
		}

		updatePolygon();
		g2d.drawPolygon(polygon);

		// 그래픽 컨텍스트 해제
		g2d.dispose();

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
			return polygon.contains(rotatedPoint);
		}

		return polygon.contains(p);
	}

	@Override
	public void resize(ControlPoint controlPoint, int dx, int dy) {
		// 최소 크기 설정
		final int MIN_SIZE = 10;

		switch (controlPoint) {
		case TOP_LEFT:
			int newWidth = width - dx;
			int newHeight = height - dy;
			if (newWidth > MIN_SIZE && newHeight > MIN_SIZE) {
				point.x += dx;
				point.y += dy;
				width = newWidth;
				height = newHeight;
			}
			break;
		case TOP:
			newHeight = height - dy;
			if (newHeight > MIN_SIZE) {
				point.y += dy;
				height = newHeight;
			}
			break;
		case TOP_RIGHT:
			newWidth = width + dx;
			newHeight = height - dy;
			if (newWidth > MIN_SIZE && newHeight > MIN_SIZE) {
				point.y += dy;
				width = newWidth;
				height = newHeight;
			}
			break;
		case RIGHT:
			newWidth = width + dx;
			if (newWidth > MIN_SIZE) {
				width = newWidth;
			}
			break;
		case BOTTOM_RIGHT:
			newWidth = width + dx;
			newHeight = height + dy;
			if (newWidth > MIN_SIZE && newHeight > MIN_SIZE) {
				width = newWidth;
				height = newHeight;
			}
			break;
		case BOTTOM:
			newHeight = height + dy;
			if (newHeight > MIN_SIZE) {
				height = newHeight;
			}
			break;
		case BOTTOM_LEFT:
			newWidth = width - dx;
			newHeight = height + dy;
			if (newWidth > MIN_SIZE && newHeight > MIN_SIZE) {
				point.x += dx;
				width = newWidth;
				height = newHeight;
			}
			break;
		case LEFT:
			newWidth = width - dx;
			if (newWidth > MIN_SIZE) {
				point.x += dx;
				width = newWidth;
			}
			break;
		default:
			break;
		}

		// 다각형 업데이트
		updatePolygon();
	}

	@Override
	public void setFromBounds(Rectangle bounds) {
		this.point.x = bounds.x;
		this.point.y = bounds.y;
		this.width = bounds.width;
		this.height = bounds.height;
		updatePolygon();
	}
}