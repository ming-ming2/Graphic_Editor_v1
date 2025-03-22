package command;

import java.awt.Point;
import java.awt.Rectangle;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;

public class DefaultCommand implements GCommand {
	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		Point startPoint = eventManager.getSelectionStartPoint();
		Point endPoint = eventManager.getSelectionEndPoint();

		// 선택 영역 드래그 중이 아니면 아무것도 하지 않음
		if (startPoint == null || endPoint == null) {
			return;
		}

		// 선택 영역 계산
		Rectangle selectionArea = createSelectionRectangle(startPoint, endPoint);

		// 선택 영역 표시 업데이트
		drawingManager.setSelectionArea(selectionArea);

		// 마우스 릴리즈 이벤트인 경우 도형 선택 수행
		if (eventManager.isMouseReleased()) {
			// 최소 크기 확인
			if (selectionArea.width > 5 && selectionArea.height > 5) {
				// 실제 도형 선택 수행
				if (!eventManager.isShiftDown()) {
					drawingManager.clearSelection();
				}

				// 선택 영역 내 도형 선택
				for (GShape shape : drawingManager.getShapes()) {
					if (shape.isInside(selectionArea)) {
						drawingManager.addToSelection(shape);
					}
				}
			}

			// 선택 작업 완료 후 선택 영역 표시 제거
			drawingManager.setSelectionArea(null);
		}
	}

	private Rectangle createSelectionRectangle(Point startPoint, Point endPoint) {
		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new Rectangle(x, y, width, height);
	}
}