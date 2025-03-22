package command;

import java.awt.Point;

import state.GDrawingStateManager;
import state.GEventStateMananger;

public class GGroupMoveCommand implements GCommand {
	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		// 도형 이동 중인지 확인
		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			// 현재 마우스 위치와 이전 위치의 차이 계산
			Point delta = eventManager.getDelta();

			// 이동이 있을 경우만 처리
			if (delta.x != 0 || delta.y != 0) {
				// 선택된 도형들 이동
				drawingManager.moveSelectedShapes(delta.x, delta.y);
			}
		}
	}
}