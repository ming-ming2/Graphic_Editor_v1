package eventhandler;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import command.GCommandManager;
import dto.GShapeCommandDTO;
import state.GDrawingStateManager;
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
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (startEvent != null) {
			List<MouseEvent> events = createMouseEvents(e);
			commandManager.execute(GMode.SHAPE, new GShapeCommandDTO(events));
			GDrawingStateManager.getInstance().setPreviewShape(null);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (startEvent != null) {
			commandManager.execute(GMode.SHAPE, createDTO(e));
		}
	}

	private List<MouseEvent> createMouseEvents(MouseEvent e) {
		List<MouseEvent> events = new ArrayList<>();
		events.add(startEvent);
		events.add(e);
		return events;
	}

	private GShapeCommandDTO createDTO(MouseEvent e) {
		List<MouseEvent> events = createMouseEvents(e);
		return new GShapeCommandDTO(events);
	}

}
