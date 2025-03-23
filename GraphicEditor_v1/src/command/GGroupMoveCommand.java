package command;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;

public class GGroupMoveCommand implements GCommand {
	// 원본 위치 저장
	private final Map<GShape, Point> originalPositions = new HashMap<>();

	// 최종 위치 (초기에는 비어있고 마우스 릴리즈 시 저장)
	private final Map<GShape, Point> finalPositions = new HashMap<>();

	// 이동 완료 여부
	private boolean moveFinished = false;

	/**
	 * 원본 위치를 받는 생성자
	 */
	public GGroupMoveCommand(Map<GShape, Point> originalPositions) {
		if (originalPositions != null) {
			this.originalPositions.putAll(originalPositions);
		}
	}

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		if (moveFinished) {
			// 이동이 완료된 상태 (다시 실행 시)
			for (GShape shape : finalPositions.keySet()) {
				if (drawingManager.getShapes().contains(shape)) {
					moveShapeTo(shape, finalPositions.get(shape));
				}
			}
			drawingManager.notifyObservers();
		} else {
			// 드래그 중 이동 처리
			GEventStateMananger eventManager = GEventStateMananger.getInstance();
			if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
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
					moveFinished = true;
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

	/**
	 * 위치가 실제로 변경되었는지 체크
	 */
	public boolean hasChanged() {
		if (!moveFinished || finalPositions.isEmpty()) {
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