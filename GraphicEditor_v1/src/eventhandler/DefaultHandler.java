package eventhandler;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import command.GCommandManager;
import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GMode;

public class DefaultHandler implements GMouseEventHandler {
	private Point startPoint;
	private GDrawingStateManager drawingStateManager;
	private GCommandManager commandManager;
	private boolean isMovingShape = false;
	private GShape clickedShape = null;

	public DefaultHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
		this.drawingStateManager = GDrawingStateManager.getInstance();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		startPoint = e.getPoint();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		eventManager.setSelectionStartPoint(startPoint);
		eventManager.setShiftDown(e.isShiftDown());
		eventManager.setMouseReleased(false);

		// 클릭 위치에 도형이 있는지 확인
		clickedShape = drawingStateManager.findShapeAt(startPoint);

		if (clickedShape != null) {
			// 도형 클릭 처리
			handleShapeClick(clickedShape, e.isShiftDown());
			isMovingShape = true;

			// 그룹 이동 시작
			drawingStateManager.setDragStartPoint(startPoint);
			drawingStateManager.setDraggingSelection(true);
		} else {
			// 빈 공간 클릭 - 선택 영역 드래그 시작
			if (!e.isShiftDown()) {
				// Shift 키가 눌리지 않았다면 기존 선택 해제
				drawingStateManager.clearSelection();
			}

			// 선택 영역 표시 시작 (크기 0으로 초기화)
			drawingStateManager.setSelectionArea(new Rectangle(startPoint.x, startPoint.y, 0, 0));
			isMovingShape = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		eventManager.setSelectionEndPoint(e.getPoint());
		eventManager.setMouseReleased(true);

		if (isMovingShape) {
			// 도형 이동 종료
			drawingStateManager.setDraggingSelection(false);
		} else if (startPoint != null) {
			// 선택 영역 선택 명령 실행
			commandManager.execute(GMode.DEFAULT);
		}

		isMovingShape = false;
		clickedShape = null;
		startPoint = null;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (startPoint == null)
			return;

		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		Point currentPoint = e.getPoint();
		eventManager.setCurrentPoint(currentPoint);
		eventManager.setSelectionEndPoint(currentPoint);
		eventManager.setMouseReleased(false);

		if (isMovingShape && !drawingStateManager.getSelectedShapes().isEmpty()) {
			// 선택된 도형 이동 명령 실행
			commandManager.execute(GMode.GROUP_MOVE);
		} else {
			// 선택 영역 프리뷰 업데이트
			commandManager.execute(GMode.DEFAULT);
		}
	}

	private void handleShapeClick(GShape shape, boolean isShiftDown) {
		// Shift 키가 눌리지 않았고, 클릭한 도형이 아직 선택되지 않았다면
		if (!isShiftDown && !shape.isSelected()) {
			// 기존 선택 모두 해제
			drawingStateManager.clearSelection();
		}

		// 클릭한 도형의 선택 상태 토글
		if (shape.isSelected()) {
			if (isShiftDown) {
				// Shift 키가 눌렸을 때만 선택 해제 (다중 선택 모드)
				drawingStateManager.removeFromSelection(shape);
			}
			// Shift가 안 눌렸으면 이미 선택된 도형은 선택 유지
		} else {
			// 도형 선택 추가
			drawingStateManager.addToSelection(shape);
		}
	}
}