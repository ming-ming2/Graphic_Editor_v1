package command;

import java.awt.Point;

import state.GDrawingStateManager;
import state.GEventStateMananger;

public class GGroupMoveCommand implements GCommand {
	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			Point currentPoint = eventManager.getCurrentPoint();

			if (currentPoint != null) {
				drawingManager.moveSelectedShapesToPosition(currentPoint);
			}
		}
	}
}