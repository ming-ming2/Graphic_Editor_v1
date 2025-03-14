package eventhandler;

import java.awt.event.MouseEvent;

import view.DrawingPanel;

public interface MouseEventHandler {
	void mousePressed(MouseEvent e, DrawingPanel panel);

	void mouseReleased(MouseEvent e, DrawingPanel panel);

	void mouseDragged(MouseEvent e, DrawingPanel panel);
}
