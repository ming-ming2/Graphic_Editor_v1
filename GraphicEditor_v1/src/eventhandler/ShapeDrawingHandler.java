package eventhandler;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import command.CommandContext;
import command.CommandManager;
import global.ContextKey;
import global.Mode;
import view.DrawingPanel;

public class ShapeDrawingHandler implements MouseEventHandler {
	private CommandManager commandManager;
	private MouseEvent startEvent;

	public ShapeDrawingHandler(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void mousePressed(MouseEvent e, DrawingPanel panel) {
		startEvent = e;
	}

	@Override
	public void mouseReleased(MouseEvent e, DrawingPanel panel) {
		if (startEvent != null) {
			CommandContext cmdContext = createContext(startEvent, e, panel);
			commandManager.execute(Mode.SHAPE, cmdContext);
			panel.setPreviewShape(null);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e, DrawingPanel panel) {
		if (startEvent != null) {
			CommandContext previewContext = createContext(startEvent, e, panel);
			commandManager.execute(Mode.SHAPE, previewContext);
		}
	}

	private CommandContext createContext(MouseEvent start, MouseEvent current, DrawingPanel panel) {
		CommandContext context = new CommandContext();
		List<MouseEvent> events = new ArrayList<>();
		events.add(start);
		events.add(current);
		context.put(ContextKey.MOUSE_EVENTS, events);
		context.put(ContextKey.SHAPE_TYPE, panel.getCurrentShapeType());
		context.put(ContextKey.DRAWING_PANEL, panel);
		return context;
	}
}
