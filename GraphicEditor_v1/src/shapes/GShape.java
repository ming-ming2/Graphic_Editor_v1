package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.io.Serializable;

public abstract class GShape implements Serializable {
	private static final long serialVersionUID = 1L;
	protected Point point;
	protected boolean isSelected = false;
	private static final int CONTROL_POINT_SIZE = 6;
	// 회전 각도 속성 추가 (라디안 단위)
	protected double rotationAngle = 0.0;

	// 제어점의 위치를 나타내는 enum 추가
	public enum ControlPoint {
		TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, ROTATE, NONE
	}

	public abstract void draw(Graphics g);

	public abstract boolean isInside(Rectangle rect);

	public abstract void move(int dx, int dy);

	public abstract Rectangle getBounds();

	// 크기 조절 메서드 추가
	public abstract void resize(ControlPoint controlPoint, int dx, int dy);

	// 경계 사각형으로부터 속성 설정 메서드 추가
	public abstract void setFromBounds(Rectangle bounds);

	// 회전 각도 설정
	public void setRotationAngle(double angle) {
		this.rotationAngle = angle;
	}

	// 회전 각도 가져오기
	public double getRotationAngle() {
		return rotationAngle;
	}

	// 지정된 각도만큼 회전
	public void rotate(double angleDelta) {
		this.rotationAngle += angleDelta;
		// 2π로 정규화 (선택 사항)
		this.rotationAngle = this.rotationAngle % (2 * Math.PI);
	}

	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void drawSelectionBox(Graphics g) {
		if (!isSelected)
			return;

		Graphics2D g2d = (Graphics2D) g.create();
		Rectangle bounds = getBounds();

		// 회전 적용
		if (rotationAngle != 0) {
			int centerX = bounds.x + bounds.width / 2;
			int centerY = bounds.y + bounds.height / 2;

			g2d.translate(centerX, centerY);
			g2d.rotate(rotationAngle);
			g2d.translate(-centerX, -centerY);
		}

		Stroke originalStroke = g2d.getStroke();

		g2d.setColor(Color.BLACK);
		float[] dashPattern = { 5, 5 };
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
		g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

		g2d.setStroke(originalStroke);
		g2d.setColor(Color.WHITE);

		// 8개의 크기 조절 제어점 그리기
		drawControlPoint(g2d, bounds.x, bounds.y); // TOP_LEFT
		drawControlPoint(g2d, bounds.x + bounds.width / 2, bounds.y); // TOP
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y); // TOP_RIGHT
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y + bounds.height / 2); // RIGHT
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y + bounds.height); // BOTTOM_RIGHT
		drawControlPoint(g2d, bounds.x + bounds.width / 2, bounds.y + bounds.height); // BOTTOM
		drawControlPoint(g2d, bounds.x, bounds.y + bounds.height); // BOTTOM_LEFT
		drawControlPoint(g2d, bounds.x, bounds.y + bounds.height / 2); // LEFT

		// 회전 제어점 (상단 중앙 위)
		g2d.setColor(Color.WHITE);
		g2d.fillOval(bounds.x + bounds.width / 2 - CONTROL_POINT_SIZE / 2, bounds.y - 20 - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(bounds.x + bounds.width / 2 - CONTROL_POINT_SIZE / 2, bounds.y - 20 - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);

		g2d.drawLine(bounds.x + bounds.width / 2, bounds.y - 20, bounds.x + bounds.width / 2, bounds.y);

		g2d.dispose(); // 그래픽 컨텍스트 해제
	}

	private void drawControlPoint(Graphics2D g, int x, int y) {
		g.setColor(Color.WHITE);
		g.fillRect(x - CONTROL_POINT_SIZE / 2, y - CONTROL_POINT_SIZE / 2, CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(x - CONTROL_POINT_SIZE / 2, y - CONTROL_POINT_SIZE / 2, CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
	}

	public boolean contains(Point p) {
		return getBounds().contains(p);
	}

	// 현재 마우스 위치가 어떤 제어점 위에 있는지 확인하는 메서드
	public ControlPoint getControlPointAt(Point p) {
		if (!isSelected)
			return ControlPoint.NONE;

		Rectangle bounds = getBounds();
		int x = bounds.x;
		int y = bounds.y;
		int w = bounds.width;
		int h = bounds.height;

		// 회전이 적용된 경우, 클릭 포인트를 역회전시켜 확인
		Point checkPoint = p;
		if (rotationAngle != 0) {
			int centerX = x + w / 2;
			int centerY = y + h / 2;

			// 점을 역회전
			double cos = Math.cos(-rotationAngle);
			double sin = Math.sin(-rotationAngle);
			int dx = p.x - centerX;
			int dy = p.y - centerY;

			int rotatedX = (int) (dx * cos - dy * sin) + centerX;
			int rotatedY = (int) (dx * sin + dy * cos) + centerY;

			checkPoint = new Point(rotatedX, rotatedY);
		}

		// 역회전된 점을 기준으로 제어점 확인
		if (isInControlPoint(checkPoint, x, y))
			return ControlPoint.TOP_LEFT;
		if (isInControlPoint(checkPoint, x + w / 2, y))
			return ControlPoint.TOP;
		if (isInControlPoint(checkPoint, x + w, y))
			return ControlPoint.TOP_RIGHT;
		if (isInControlPoint(checkPoint, x + w, y + h / 2))
			return ControlPoint.RIGHT;
		if (isInControlPoint(checkPoint, x + w, y + h))
			return ControlPoint.BOTTOM_RIGHT;
		if (isInControlPoint(checkPoint, x + w / 2, y + h))
			return ControlPoint.BOTTOM;
		if (isInControlPoint(checkPoint, x, y + h))
			return ControlPoint.BOTTOM_LEFT;
		if (isInControlPoint(checkPoint, x, y + h / 2))
			return ControlPoint.LEFT;

		// 회전 제어점 확인 - 이것도 회전을 고려해야 함
		if (isInControlPoint(checkPoint, x + w / 2, y - 20))
			return ControlPoint.ROTATE;

		return ControlPoint.NONE;
	}

	private boolean isInControlPoint(Point p, int x, int y) {
		Rectangle controlRect = new Rectangle(x - CONTROL_POINT_SIZE / 2, y - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		return controlRect.contains(p);
	}
}