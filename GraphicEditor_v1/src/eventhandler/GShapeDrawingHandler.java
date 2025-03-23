package eventhandler;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import command.GCommandManager;
import state.GEventStateMananger;
import type.GMode;

public class GShapeDrawingHandler implements GMouseEventHandler {
	private GCommandManager commandManager;
	private MouseEvent startEvent;

	public GShapeDrawingHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startEvent = e;
		GEventStateMananger.getInstance().setCurrentPoint(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (startEvent != null) {
			List<MouseEvent> events = createMouseEvents(e);
			GEventStateMananger.getInstance().setMouseEvents(events);
			GEventStateMananger.getInstance().setCurrentPoint(e.getPoint());
			commandManager.executeAndStore(GMode.SHAPE);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (startEvent != null) {
			List<MouseEvent> events = createMouseEvents(e);
			GEventStateMananger.getInstance().setMouseEvents(events);
			GEventStateMananger.getInstance().setCurrentPoint(e.getPoint());
			commandManager.execute(GMode.SHAPE);
		}
	}

	private List<MouseEvent> createMouseEvents(MouseEvent e) {
		List<MouseEvent> events = new ArrayList<>();
		events.add(startEvent);
		events.add(e);
		return events;
	}
}