package command;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;

public class GGroupMoveCommand implements GCommand {
	private Map<GShape, Point> originalPositions = new HashMap<>();
	private Map<GShape, Point> finalPositions = new HashMap<>();
	private boolean initialized = false;

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		if (!initialized) {
			for (GShape shape : drawingManager.getSelectedShapes()) {
				Point origPoint = new Point(shape.getBounds().x, shape.getBounds().y);
				originalPositions.put(shape, origPoint);
			}
			initialized = true;
		}

		if (!finalPositions.isEmpty()) {
			for (GShape shape : finalPositions.keySet()) {
				if (drawingManager.getShapes().contains(shape)) {
					moveShapeTo(shape, finalPositions.get(shape));
				}
			}
			drawingManager.notifyObservers();
			return;
		}

		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			GEventStateMananger eventManager = GEventStateMananger.getInstance();
			Point currentPoint = eventManager.getCurrentPoint();

			if (currentPoint != null) {
				drawingManager.moveSelectedShapesToPosition(currentPoint);
			}

			if (eventManager.isMouseReleased() && finalPositions.isEmpty()) {
				for (GShape shape : drawingManager.getSelectedShapes()) {
					if (originalPositions.containsKey(shape)) {
						finalPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
					}
				}
			}
		}
	}

	@Override
	public void unexecute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		for (GShape shape : originalPositions.keySet()) {
			if (drawingManager.getShapes().contains(shape)) {
				moveShapeTo(shape, originalPositions.get(shape));
			}
		}

		drawingManager.notifyObservers();
	}

	public boolean hasChanged() {
		if (finalPositions.isEmpty()) {
			return false;
		}

		for (GShape shape : originalPositions.keySet()) {
			Point original = originalPositions.get(shape);
			Point finalPos = finalPositions.get(shape);

			if (finalPos != null && (original.x != finalPos.x || original.y != finalPos.y)) {
				return true;
			}
		}

		return false;
	}

	private void moveShapeTo(GShape shape, Point targetPosition) {
		Point currentPos = new Point(shape.getBounds().x, shape.getBounds().y);
		int dx = targetPosition.x - currentPos.x;
		int dy = targetPosition.y - currentPos.y;
		shape.move(dx, dy);
	}
}
