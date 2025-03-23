package command;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;

public class GGroupMoveCommand implements GCommand {
	// 원본 위치 저장
	private Map<GShape, Point> originalPositions = new HashMap<>();
	// 최종 위치 저장
	private Map<GShape, Point> finalPositions = new HashMap<>();
	// 초기화 여부
	private boolean initialized = false;

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		// 첫 실행 시 원본 위치 저장
		if (!initialized) {
			for (GShape shape : drawingManager.getSelectedShapes()) {
				Point origPoint = new Point(shape.getBounds().x, shape.getBounds().y);
				originalPositions.put(shape, origPoint);
			}
			initialized = true;
		}

		// 이동 완료된 경우 (redo 등)
		if (!finalPositions.isEmpty()) {
			for (GShape shape : finalPositions.keySet()) {
				if (drawingManager.getShapes().contains(shape)) {
					moveShapeTo(shape, finalPositions.get(shape));
				}
			}
			drawingManager.notifyObservers();
			return;
		}

		// 드래그 중 이동 처리
		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			GEventStateMananger eventManager = GEventStateMananger.getInstance();
			Point currentPoint = eventManager.getCurrentPoint();
			if (currentPoint != null) {
				drawingManager.moveSelectedShapesToPosition(currentPoint);
			}

			// 마우스 릴리즈 시 최종 위치 저장
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
		// 원본 위치로 복원
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		for (GShape shape : originalPositions.keySet()) {
			if (drawingManager.getShapes().contains(shape)) {
				moveShapeTo(shape, originalPositions.get(shape));
			}
		}
		drawingManager.notifyObservers();
	}

	// 위치가 실제로 변경되었는지 체크
	public boolean hasChanged() {
		if (finalPositions.isEmpty()) {
			return false;
		}

		// 원본 위치와 최종 위치를 비교
		for (GShape shape : originalPositions.keySet()) {
			Point original = originalPositions.get(shape);
			Point finalPos = finalPositions.get(shape);

			if (finalPos != null && (original.x != finalPos.x || original.y != finalPos.y)) {
				return true;
			}
		}

		return false;
	}

	// 도형을 특정 위치로 이동
	private void moveShapeTo(GShape shape, Point targetPosition) {
		Point currentPos = new Point(shape.getBounds().x, shape.getBounds().y);
		int dx = targetPosition.x - currentPos.x;
		int dy = targetPosition.y - currentPos.y;
		shape.move(dx, dy);
	}
}