package command;

import java.awt.event.MouseEvent;
import java.util.List;

import shapes.GShape;
import shapes.GShapeFactory;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GShapeType;

public class GShapeCommand implements GCommand {
	@Override
	public void execute() {
		List<MouseEvent> events = GEventStateMananger.getInstance().getMouseEvents();
		GDrawingStateManager drawingStateManager = GDrawingStateManager.getInstance();
		GShapeType shapeType = drawingStateManager.getCurrentShapeType();

		if (events == null || events.size() < 2 || shapeType == null) {
			return;
		}

		GShape shape = GShapeFactory.getShape(shapeType, events);

		if (shape != null) {
			MouseEvent lastEvent = events.get(events.size() - 1);
			if (lastEvent.getID() == MouseEvent.MOUSE_RELEASED) {
				drawingStateManager.addShape(shape);
				drawingStateManager.setPreviewShape(null);
			} else {
				drawingStateManager.setPreviewShape(shape);
			}
		}
	}
}
