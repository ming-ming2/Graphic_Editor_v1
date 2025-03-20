package shapes;

import java.awt.Graphics;
import java.awt.Point;

public class GRectangle extends GShape {
	private int width;
	private int height;

	public GRectangle(Point point, int width, int height) {
		this.point = point;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics g) {
		g.drawRect(point.x, point.y, width, height);
	}

}
