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
	protected double rotationAngle = 0.0;
	private static final int CONTROL_POINT_SIZE = 6;

	public enum ControlPoint {
		TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, ROTATE, NONE
	}

	public abstract void draw(Graphics g);

	public abstract boolean isInside(Rectangle rect);

	public abstract void move(int dx, int dy);

	public abstract Rectangle getBounds();

	public abstract void resize(ControlPoint controlPoint, int dx, int dy);

	public abstract void setFromBounds(Rectangle bounds);

	public void setRotationAngle(double angle) {
		this.rotationAngle = angle;
	}

	public double getRotationAngle() {
		return rotationAngle;
	}

	public void rotate(double angleDelta) {
		this.rotationAngle = (this.rotationAngle + angleDelta) % (2 * Math.PI);
	}

	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	protected void applyRotationTransform(Graphics2D g2d, Rectangle bounds) {
		int centerX = bounds.x + bounds.width / 2;
		int centerY = bounds.y + bounds.height / 2;
		g2d.translate(centerX, centerY);
		g2d.rotate(rotationAngle);
		g2d.translate(-centerX, -centerY);
	}

	protected Point reverseRotatePoint(Point p, Rectangle bounds) {
		int centerX = bounds.x + bounds.width / 2;
		int centerY = bounds.y + bounds.height / 2;
		double cos = Math.cos(-rotationAngle);
		double sin = Math.sin(-rotationAngle);
		int dx = p.x - centerX;
		int dy = p.y - centerY;
		int rotatedX = (int) (dx * cos - dy * sin) + centerX;
		int rotatedY = (int) (dx * sin + dy * cos) + centerY;
		return new Point(rotatedX, rotatedY);
	}

	public void drawSelectionBox(Graphics g) {
		if (!isSelected)
			return;
		Graphics2D g2d = (Graphics2D) g.create();
		Rectangle bounds = getBounds();

		if (rotationAngle != 0)
			applyRotationTransform(g2d, bounds);

		Stroke originalStroke = g2d.getStroke();
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[] { 5, 5 }, 0));
		g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		g2d.setStroke(originalStroke);
		g2d.setColor(Color.WHITE);

		drawControlPoint(g2d, bounds.x, bounds.y);
		drawControlPoint(g2d, bounds.x + bounds.width / 2, bounds.y);
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y);
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y + bounds.height / 2);
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y + bounds.height);
		drawControlPoint(g2d, bounds.x + bounds.width / 2, bounds.y + bounds.height);
		drawControlPoint(g2d, bounds.x, bounds.y + bounds.height);
		drawControlPoint(g2d, bounds.x, bounds.y + bounds.height / 2);

		g2d.fillOval(bounds.x + bounds.width / 2 - CONTROL_POINT_SIZE / 2, bounds.y - 20 - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(bounds.x + bounds.width / 2 - CONTROL_POINT_SIZE / 2, bounds.y - 20 - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		g2d.drawLine(bounds.x + bounds.width / 2, bounds.y - 20, bounds.x + bounds.width / 2, bounds.y);
		g2d.dispose();
	}

	private void drawControlPoint(Graphics2D g, int x, int y) {
		g.setColor(Color.WHITE);
		g.fillRect(x - CONTROL_POINT_SIZE / 2, y - CONTROL_POINT_SIZE / 2, CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		g.setColor(Color.BLACK);
		g.drawRect(x - CONTROL_POINT_SIZE / 2, y - CONTROL_POINT_SIZE / 2, CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
	}

	public ControlPoint getControlPointAt(Point p) {
		if (!isSelected)
			return ControlPoint.NONE;
		Rectangle bounds = getBounds();
		Point checkPoint = (rotationAngle != 0) ? reverseRotatePoint(p, bounds) : p;
		int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
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
		if (isInControlPoint(checkPoint, x + w / 2, y - 20))
			return ControlPoint.ROTATE;
		return ControlPoint.NONE;
	}

	private boolean isInControlPoint(Point p, int x, int y) {
		Rectangle controlRect = new Rectangle(x - CONTROL_POINT_SIZE / 2, y - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		return controlRect.contains(p);
	}

	public boolean contains(Point p) {
		return getBounds().contains(p);
	}
}
