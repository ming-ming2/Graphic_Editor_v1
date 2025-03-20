package eventhandler;

import java.awt.Point;
import java.awt.event.MouseEvent;

import command.GCommandManager;
import shapes.GShape;
import state.GDrawingStateManager;

public class GGroupMoveHandler implements GMouseEventHandler {
	private GDrawingStateManager drawingStateManager;
	private GCommandManager commandManager;
	private Point lastPoint;
	private GShape selectedShape; // 선택된 도형 중 드래그 중인 도형

	public GGroupMoveHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
		this.drawingStateManager = GDrawingStateManager.getInstance();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point point = e.getPoint();

		// 클릭 위치에 있는 도형 찾기
		selectedShape = drawingStateManager.findShapeAt(point);

		// 선택된 도형이 없거나 선택된 도형들 중에 없다면
		if (selectedShape == null || !selectedShape.isSelected()) {
			// 새로운 선택 시작
			drawingStateManager.clearSelection();

			if (selectedShape != null) {
				// 클릭한 도형만 선택
				drawingStateManager.addToSelection(selectedShape);
			}

			// 이동 시작점 설정
			drawingStateManager.setDragStartPoint(point);
			drawingStateManager.setDraggingSelection(true);
		} else {
			// 이미 선택된 도형을 클릭한 경우, 그룹 이동 시작
			drawingStateManager.setDragStartPoint(point);
			drawingStateManager.setDraggingSelection(true);
		}

		lastPoint = point;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawingStateManager.setDraggingSelection(false);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (drawingStateManager.isDraggingSelection() && !drawingStateManager.getSelectedShapes().isEmpty()) {
			Point currentPoint = e.getPoint();

			// 이전 위치와의 차이 계산
			int dx = currentPoint.x - lastPoint.x;
			int dy = currentPoint.y - lastPoint.y;

			// 선택된 모든 도형 이동
			drawingStateManager.moveSelectedShapes(dx, dy);

			// 마지막 위치 업데이트
			lastPoint = currentPoint;
		}
	}
}