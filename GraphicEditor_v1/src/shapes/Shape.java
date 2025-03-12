package shapes;

import java.awt.Graphics;
import java.awt.Point;

public abstract class Shape {
	Point point;
	
	abstract void draw(Graphics g);
}
