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
	private Point originalDragStartPoint;

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			// 처음 실행 시 원래 위치와 시작점 저장
			if (originalPositions.isEmpty() && originalDragStartPoint == null) {
				originalDragStartPoint = new Point(drawingManager.getDragStartPoint());

				for (GShape shape : drawingManager.getSelectedShapes()) {
					originalPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
				}
			}

			Point currentPoint = eventManager.getCurrentPoint();
			if (currentPoint != null) {
				drawingManager.moveSelectedShapesToPosition(currentPoint);
			}
		}
	}

	@Override
	public void unexecute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		// 드래그 시작점 복원
		if (originalDragStartPoint != null) {
			drawingManager.setDragStartPoint(originalDragStartPoint);
		}

		// 각 도형을 원래 위치로 복원
		List<GShape> shapesToRestore = new ArrayList<>(originalPositions.keySet());
		for (GShape shape : shapesToRestore) {
			Point originalPos = originalPositions.get(shape);
			if (originalPos != null) {
				int dx = originalPos.x - shape.getBounds().x;
				int dy = originalPos.y - shape.getBounds().y;
				shape.move(dx, dy);
			}
		}

		drawingManager.notifyObservers();
	}
}