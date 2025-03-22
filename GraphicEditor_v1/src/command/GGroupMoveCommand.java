package command;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;

public class GGroupMoveCommand implements GCommand {
	private Map<GShape, Point> originalPositions = new HashMap<>();
	private int deltaX;
	private int deltaY;

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			Point currentPoint = eventManager.getCurrentPoint();
			Point previousPoint = eventManager.getPreviousPoint();

			if (currentPoint != null && previousPoint != null) {

				if (originalPositions.isEmpty()) {
					for (GShape shape : drawingManager.getSelectedShapes()) {
						originalPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
					}
				}

				deltaX = currentPoint.x - previousPoint.x;
				deltaY = currentPoint.y - previousPoint.y;

				for (GShape shape : drawingManager.getSelectedShapes()) {
					shape.move(deltaX, deltaY);
				}

				drawingManager.notifyObservers();
			}
		}
	}

	@Override
	public void unexecute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		List<GShape> shapesToRestore = new ArrayList<>(originalPositions.keySet());

		for (GShape shape : shapesToRestore) {
			Point originalPos = originalPositions.get(shape);
			Point currentPos = new Point(shape.getBounds().x, shape.getBounds().y);

			int dx = originalPos.x - currentPos.x;
			int dy = originalPos.y - currentPos.y;

			shape.move(dx, dy);
		}

		drawingManager.notifyObservers();
	}
}