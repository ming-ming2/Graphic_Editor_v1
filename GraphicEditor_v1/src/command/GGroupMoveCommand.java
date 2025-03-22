package command;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;

public class GGroupMoveCommand implements GCommand {
	private Map<GShape, Point> originalPositions = new HashMap<>();
	private boolean executed = false;

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			// 최초 실행 시에만 원래 위치 저장
			if (!executed) {
				// 선택된 도형들의 원래 위치 저장
				for (GShape shape : drawingManager.getSelectedShapes()) {
					originalPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
				}
				executed = true;
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

		// 각 도형을 원래 위치로 복원
		for (Map.Entry<GShape, Point> entry : originalPositions.entrySet()) {
			GShape shape = entry.getKey();
			Point originalPos = entry.getValue();

			// 현재 위치 구하기
			Point currentPos = new Point(shape.getBounds().x, shape.getBounds().y);

			// 원래 위치로 이동
			int dx = originalPos.x - currentPos.x;
			int dy = originalPos.y - currentPos.y;
			shape.move(dx, dy);
		}

		drawingManager.notifyObservers();
	}
}