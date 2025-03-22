package command;

import java.awt.event.MouseEvent;
import java.util.List;

import shapes.GShape;
import shapes.GShapeFactory;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GShapeType;

public class GShapeCommand implements GCommand {
	private GShape createdShape;
	private boolean executed = false;

	@Override
	public void execute() {
		if (executed && createdShape != null) {

			GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
			drawingManager.addShape(createdShape);
			return;
		}

		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		List<MouseEvent> events = eventManager.getMouseEvents();

		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GShapeType shapeType = drawingManager.getCurrentShapeType();

		if (events == null || events.size() < 2 || shapeType == null) {
			return;
		}

		GShape shape = GShapeFactory.getShape(shapeType, events);

		if (shape != null) {
			MouseEvent lastEvent = events.get(events.size() - 1);
			if (lastEvent.getID() == MouseEvent.MOUSE_RELEASED) {
				drawingManager.addShape(shape);
				drawingManager.setPreviewShape(null);
				createdShape = shape;
				executed = true;
			} else {
				drawingManager.setPreviewShape(shape);
			}
		}
	}

	@Override
	public void unexecute() {
		if (createdShape != null) {
			GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
			drawingManager.removeShape(createdShape);
		}
	}
}