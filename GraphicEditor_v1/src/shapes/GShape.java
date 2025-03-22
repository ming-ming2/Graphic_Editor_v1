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

	public abstract void draw(Graphics g);

	public abstract boolean isInside(Rectangle rect);

	public abstract void move(int dx, int dy);

	public abstract Rectangle getBounds();

	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void drawSelectionBox(Graphics g) {
		if (!isSelected)
			return;

		Graphics2D g2d = (Graphics2D) g;
		Rectangle bounds = getBounds();

		Stroke originalStroke = g2d.getStroke();

		g2d.setColor(Color.BLACK);
		float[] dashPattern = { 5, 5 };
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
		g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

		g2d.setStroke(originalStroke);
		g2d.setColor(Color.WHITE);

		drawControlPoint(g2d, bounds.x, bounds.y);
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y);
		drawControlPoint(g2d, bounds.x, bounds.y + bounds.height);
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y + bounds.height);

		drawControlPoint(g2d, bounds.x + bounds.width / 2, bounds.y);
		drawControlPoint(g2d, bounds.x + bounds.width, bounds.y + bounds.height / 2);
		drawControlPoint(g2d, bounds.x + bounds.width / 2, bounds.y + bounds.height);
		drawControlPoint(g2d, bounds.x, bounds.y + bounds.height / 2);

		g2d.setColor(Color.WHITE);
		g2d.fillOval(bounds.x + bounds.width / 2 - CONTROL_POINT_SIZE / 2, bounds.y - 20 - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);
		g2d.setColor(Color.BLACK);
		g2d.drawOval(bounds.x + bounds.width / 2 - CONTROL_POINT_SIZE / 2, bounds.y - 20 - CONTROL_POINT_SIZE / 2,
				CONTROL_POINT_SIZE, CONTROL_POINT_SIZE);

		g2d.drawLine(bounds.x + bounds.width / 2, bounds.y - 20, bounds.x + bounds.width / 2, bounds.y);
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
}