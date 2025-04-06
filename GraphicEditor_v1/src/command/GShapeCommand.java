package command;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import shapes.GShape;
import shapes.GShapeFactory;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GShapeType;

public class GShapeCommand implements GCommand {
	private GShape createdShape;
	private GShape lastPreviewShape;

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		if (createdShape != null) {
			drawingManager.addShape(createdShape);
			return;
		}

		List<MouseEvent> events = eventManager.getMouseEvents();
		GShapeType shapeType = drawingManager.getCurrentShapeType();

		if (events == null || events.size() < 2 || shapeType == null) {
			return;
		}

		new ArrayList<>(events);
		GShape shape = GShapeFactory.getShape(shapeType, events);

		if (shape != null) {
			MouseEvent lastEvent = events.get(events.size() - 1);

			if (lastEvent.getID() == MouseEvent.MOUSE_RELEASED || eventManager.isMouseReleased()) {
				drawingManager.addShape(shape);
				lastPreviewShape = null;
				this.createdShape = shape;
			} else {
				lastPreviewShape = shape;
				drawingManager.notifyObservers();
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

	public GShape getLastPreviewShape() {
		return lastPreviewShape;
	}
}