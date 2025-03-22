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
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		List<MouseEvent> events = eventManager.getMouseEvents();

		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GShapeType shapeType = drawingManager.getCurrentShapeType();

		// 유효성 검사
		if (events == null || events.size() < 2 || shapeType == null) {
			return;
		}

		// 도형 생성
		GShape shape = GShapeFactory.getShape(shapeType, events);

		if (shape != null) {
			MouseEvent lastEvent = events.get(events.size() - 1);
			if (lastEvent.getID() == MouseEvent.MOUSE_RELEASED) {
				drawingManager.addShape(shape);
				drawingManager.setPreviewShape(null);
			} else {
				drawingManager.setPreviewShape(shape);
			}
		}
	}
}