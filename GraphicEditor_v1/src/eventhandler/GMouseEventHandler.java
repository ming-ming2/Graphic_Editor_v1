package eventhandler;

import java.awt.event.MouseEvent;

public interface GMouseEventHandler {
	void mousePressed(MouseEvent e);

	void mouseReleased(MouseEvent e);

	void mouseDragged(MouseEvent e);
}
