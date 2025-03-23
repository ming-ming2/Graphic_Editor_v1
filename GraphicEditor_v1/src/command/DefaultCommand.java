package command;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;

public class DefaultCommand implements GCommand {
	private List<GShape> previouslySelectedShapes = new ArrayList<>();
	private List<GShape> newlySelectedShapes = new ArrayList<>();

	// 선택 영역 정보 저장
	private Point startPoint;
	private Point endPoint;
	private boolean shiftDown;

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		// 첫 실행 시 이전 선택 상태와 선택 정보 저장
		if (startPoint == null) {
			startPoint = eventManager.getSelectionStartPoint();
			endPoint = eventManager.getSelectionEndPoint();
			shiftDown = eventManager.isShiftDown();

			previouslySelectedShapes.clear();
			previouslySelectedShapes.addAll(drawingManager.getSelectedShapes());
		}

		if (startPoint == null || endPoint == null) {
			return;
		}

		Rectangle selectionArea = createSelectionRectangle(startPoint, endPoint);
		drawingManager.setSelectionArea(selectionArea);

		if (eventManager.isMouseReleased() && selectionArea.width > 5 && selectionArea.height > 5) {
			if (!shiftDown) {
				drawingManager.clearSelection();
			}

			newlySelectedShapes.clear();
			for (GShape shape : drawingManager.getShapes()) {
				if (shape.isInside(selectionArea) && !drawingManager.getSelectedShapes().contains(shape)) {
					drawingManager.addToSelection(shape);
					newlySelectedShapes.add(shape);
				}
			}

			drawingManager.setSelectionArea(null);
		}
	}

	@Override
	public void unexecute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		// 새로 선택된 도형들 선택 해제
		for (GShape shape : newlySelectedShapes) {
			drawingManager.removeFromSelection(shape);
		}

		// 이전에 선택되었던 도형들 복원
		drawingManager.clearSelection();
		for (GShape shape : previouslySelectedShapes) {
			drawingManager.addToSelection(shape);
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