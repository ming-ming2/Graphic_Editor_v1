package command;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;

public class GGroupMoveCommand implements GCommand {
	private Map<GShape, Point> originalPositions = new HashMap<>();
	private Map<GShape, Point> newPositions = new HashMap<>();

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		// 첫 실행 시 원래 위치 저장
		if (originalPositions.isEmpty()) {
			for (GShape shape : drawingManager.getSelectedShapes()) {
				originalPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
			}
		}

		Point currentPoint = eventManager.getCurrentPoint();
		if (currentPoint != null && drawingManager.isDraggingSelection()
				&& !drawingManager.getSelectedShapes().isEmpty()) {
			drawingManager.moveSelectedShapesToPosition(currentPoint);

			// 실행 후 새 위치 저장
			newPositions.clear();
			for (GShape shape : drawingManager.getSelectedShapes()) {
				newPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
			}
		}
	}

	@Override
	public void unexecute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		for (GShape shape : originalPositions.keySet()) {
			Point originalPos = originalPositions.get(shape);
			Point currentPos = new Point(shape.getBounds().x, shape.getBounds().y);

			int dx = originalPos.x - currentPos.x;
			int dy = originalPos.y - currentPos.y;
			shape.move(dx, dy);
		}

		drawingManager.notifyObservers();
	}

	// redo를 위한 추가 메서드 (필요 시 사용)
	private void restoreNewPositions() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		for (GShape shape : newPositions.keySet()) {
			Point newPos = newPositions.get(shape);
			Point currentPos = new Point(shape.getBounds().x, shape.getBounds().y);

			int dx = newPos.x - currentPos.x;
			int dy = newPos.y - currentPos.y;
			shape.move(dx, dy);
		}

		drawingManager.notifyObservers();
	}
}