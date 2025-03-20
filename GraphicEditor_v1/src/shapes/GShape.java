package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

public abstract class GShape {
	protected Point point;
	protected boolean isSelected = false;
	private static final int CONTROL_POINT_SIZE = 6;

	public abstract void draw(Graphics g);

	/**
	 * 도형이 주어진 사각형 영역 내에 있는지 확인
	 */
	public abstract boolean isInside(Rectangle rect);

	/**
	 * 도형을 이동
	 */
	public abstract void move(int dx, int dy);

	/**
	 * 도형의 경계 사각형을 반환
	 */
	public abstract Rectangle getBounds();

	/**
	 * 선택 상태 설정
	 */
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	/**
	 * 선택 상태 확인
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * 도형이 선택되었을 때 컨트롤 포인트 그리기
	 */
	public void drawSelectionMarkers(Graphics g) {
		if (!isSelected)
			return;

		Graphics2D g2d = (Graphics2D) g;
		Rectangle bounds = getBounds();

		// 원래 스트로크 저장
		Stroke originalStroke = g2d.getStroke();

		// 경계 사각형 그리기 (점선)
		g2d.setColor(Color.BLACK);
		float[] dashPattern = { 5, 5 };
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
		g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

		// 컨트롤 포인트 그리기
		g2d.setStroke(originalStroke);
		g2d.setColor(Color.WHITE);

		// 테두리 점들
		drawControlPoint(g2d, bounds.x, bounds.y); // 좌상단
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y); // 우상단
		drawControlPoint(g2d, bounds.x, bounds.y + bounds.height); // 좌하단
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y + bounds.height); // 우하단

		// 중간 점들
		drawControlPoint(g2d, bounds.x + bounds.width / 2, bounds.y); // 상단 중앙
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y + bounds.height / 2); // 우측 중앙
		drawControlPoint(g2d, bounds.x + bounds.width / 2, bounds.y + bounds.height); // 하단 중앙
		drawControlPoint(g2d, bounds.x, bounds.y + bounds.height / 2); // 좌측 중앙

		// 회전 포인트 (상단 중앙 위)
		g2d.setColor(Color.WHITE);
		g2d.fillOval(bounds.x + bounds.width / 2 - CONTROL_POINT_SIZE / 2, bounds.y - 20 - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(bounds.x + bounds.width / 2 - CONTROL_POINT_SIZE / 2, bounds.y - 20 - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);

		// 회전 아이콘에서 도형까지 선 연결
		g2d.drawLine(bounds.x + bounds.width / 2, bounds.y - 20, bounds.x + bounds.width / 2, bounds.y);
	}

	/**
	 * 컨트롤 포인트 그리기
	 */
	private void drawControlPoint(Graphics2D g, int x, int y) {
		g.setColor(Color.WHITE);
		g.fillRect(x - CONTROL_POINT_SIZE / 2, y - CONTROL_POINT_SIZE / 2, CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(x - CONTROL_POINT_SIZE / 2, y - CONTROL_POINT_SIZE / 2, CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
	}

	/**
	 * 특정 지점이 도형 위에 있는지 확인
	 */
	public boolean contains(Point p) {
		return getBounds().contains(p);
	}
}