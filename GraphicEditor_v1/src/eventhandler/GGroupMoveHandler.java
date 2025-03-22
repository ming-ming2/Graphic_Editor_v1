package eventhandler;

import java.awt.Point;
import java.awt.event.MouseEvent;

import command.GCommandManager;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GMode;

public class GGroupMoveHandler implements GMouseEventHandler {
	private GCommandManager commandManager;

	public GGroupMoveHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point point = e.getPoint();
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		// 현재 위치 저장
		eventManager.setCurrentPoint(point);

		drawingManager.findShapeAt(point);

		// 이동 시작 설정
		drawingManager.setDragStartPoint(point);
		drawingManager.setDraggingSelection(true);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		// 현재 위치 저장
		eventManager.setCurrentPoint(e.getPoint());

		// 이동 종료
		drawingManager.setDraggingSelection(false);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		// 현재 포인트 저장 (이전 포인트도 자동으로 저장됨)
		eventManager.setCurrentPoint(e.getPoint());

		// 도형 이동 중인지 확인
		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			// 명령 실행으로 이동 작업 위임
			commandManager.execute(GMode.GROUP_MOVE);
		}
	}
}