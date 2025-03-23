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

		// 기본 설정
		eventManager.setCurrentPoint(point);
		drawingManager.findShapeAt(point);
		drawingManager.setDragStartPoint(point);
		drawingManager.setDraggingSelection(true);

		// 명령 객체가 생성되고 초기화됩니다 (CommandFactory에서)
		// 명령 객체는 내부적으로 원본 위치를 저장합니다
		commandManager.execute(GMode.GROUP_MOVE);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		Point currentPoint = e.getPoint();
		eventManager.setCurrentPoint(currentPoint);

		// 명령 객체를 통해 도형 이동
		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			commandManager.execute(GMode.GROUP_MOVE);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		eventManager.setCurrentPoint(e.getPoint());
		eventManager.setMouseReleased(true);

		// 마지막으로 명령 실행 (이 때 최종 위치가 저장됨)
		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			commandManager.execute(GMode.GROUP_MOVE);
			// 명령을 스택에 저장 (변경 사항이 있는 경우에만)
			commandManager.executeAndStore(GMode.GROUP_MOVE);
		}

		// 상태 초기화
		drawingManager.setSelectionArea(null);
		drawingManager.setDraggingSelection(false);
		drawingManager.setCurrentMode(GMode.DEFAULT);
	}
}