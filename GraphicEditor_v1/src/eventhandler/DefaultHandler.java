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
	private GEventStateMananger eventStateManager;
	private GCommandManager commandManager;
	private boolean isMovingShape = false;
	private GShape clickedShape = null;

	// 인자 없는 생성자
	public DefaultHandler() {
		this.commandManager = null;
		// 지연 초기화를 위해 여기서는 drawingStateManager를 설정하지 않음
	}

	// 명령 관리자를 받는 생성자
	public DefaultHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
		// 지연 초기화를 위해 여기서는 drawingStateManager를 설정하지 않음
	}

	// drawingStateManager 지연 초기화
	private GDrawingStateManager getDrawingStateManager() {
		if (drawingStateManager == null) {
			drawingStateManager = GDrawingStateManager.getInstance();
		}
		return drawingStateManager;
	}

	// eventStateManager 지연 초기화
	private GEventStateMananger getEventStateManager() {
		if (eventStateManager == null) {
			eventStateManager = GEventStateMananger.getInstance();
		}
		return eventStateManager;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startPoint = e.getPoint();

		// 클릭 위치에 도형이 있는지 확인
		clickedShape = getDrawingStateManager().findShapeAt(startPoint);

		if (clickedShape != null) {
			// 도형을 클릭한 경우
			if (!e.isShiftDown()) {
				// Shift 키가 눌리지 않았다면 기존 선택 해제
				if (!clickedShape.isSelected()) {
					getDrawingStateManager().clearSelection();
				}
			}

			// 클릭한 도형 선택 상태 토글
			if (clickedShape.isSelected()) {
				if (e.isShiftDown()) {
					// Shift 키가 눌렸으면 선택 해제
					getDrawingStateManager().removeFromSelection(clickedShape);
				}
			} else {
				// 도형 선택
				getDrawingStateManager().addToSelection(clickedShape);
			}

			isMovingShape = true;
			// 그룹 이동을 위한 시작점 설정
			getDrawingStateManager().setDragStartPoint(startPoint);
			getDrawingStateManager().setDraggingSelection(true);
		} else {
			// 빈 공간 클릭 - 선택 영역 드래그 시작
			if (!e.isShiftDown()) {
				// Shift 키가 눌리지 않았다면 기존 선택 해제
				getDrawingStateManager().clearSelection();
			}

			// 선택 영역 표시 시작
			getDrawingStateManager().setSelectionArea(new Rectangle(startPoint.x, startPoint.y, 0, 0));
			isMovingShape = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isMovingShape) {
			// 도형 이동 종료
			getDrawingStateManager().setDraggingSelection(false);
		} else if (startPoint != null) {
			// 선택 영역이 생성된 경우
			Rectangle selectionArea = createSelectionRectangle(e.getPoint());

			// 미니멈 크기 체크 (너무 작은 영역은 무시)
			if (selectionArea.width > 5 && selectionArea.height > 5) {
				// 선택 영역 내 도형 선택 (명령 실행)
				getDrawingStateManager().setSelectionArea(selectionArea);
				if (commandManager != null) {
					commandManager.execute(GMode.SELECTION);
				}
			}

			// 선택 영역 표시 제거
			getDrawingStateManager().setSelectionArea(null);
		}

		isMovingShape = false;
		clickedShape = null;
		startPoint = null;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (startPoint == null)
			return;

		Point currentPoint = e.getPoint();

		if (isMovingShape && !getDrawingStateManager().getSelectedShapes().isEmpty()) {
			// 선택된 도형 이동 (명령 실행)
			if (commandManager != null) {
				commandManager.execute(GMode.GROUP_MOVE);
			}
		} else {
			// 선택 영역 업데이트
			Rectangle selectionArea = createSelectionRectangle(currentPoint);
			getDrawingStateManager().setSelectionArea(selectionArea);
		}
	}

	private Rectangle createSelectionRectangle(Point currentPoint) {
		int x = Math.min(startPoint.x, currentPoint.x);
		int y = Math.min(startPoint.y, currentPoint.y);
		int width = Math.abs(currentPoint.x - startPoint.x);
		int height = Math.abs(currentPoint.y - startPoint.y);

		return new Rectangle(x, y, width, height);
	}
}