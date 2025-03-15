package eventhandler;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import command.CommandManager;
import dto.ShapeCommandDTO;
import global.Mode;
import view.DrawingPanel;

public class ShapeDrawingHandler implements MouseEventHandler {
	private CommandManager commandManager;
	private MouseEvent startEvent;
	private DrawingPanel panel;

	public ShapeDrawingHandler(CommandManager commandManager, DrawingPanel panel) {
		this.commandManager = commandManager;
		this.panel = panel;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startEvent = e;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (startEvent != null) {
			List<MouseEvent> events = createMouseEvents(e);
			commandManager.execute(Mode.SHAPE, new ShapeCommandDTO(events, panel.getCurrentShapeType(), panel));
			panel.setPreviewShape(null);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (startEvent != null) {
			commandManager.execute(Mode.SHAPE, createDTO(e, panel));
		}
	}

	private List<MouseEvent> createMouseEvents(MouseEvent e) {
		List<MouseEvent> events = new ArrayList<>();
		events.add(startEvent);
		events.add(e);
		return events;
	}

	private ShapeCommandDTO createDTO(MouseEvent e, DrawingPanel panel) {
		List<MouseEvent> events = createMouseEvents(e);
		return new ShapeCommandDTO(events, panel.getCurrentShapeType(), panel);
	}

}
