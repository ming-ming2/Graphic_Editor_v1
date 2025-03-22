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
	private boolean executed = false;

	@Override
	public void execute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		Point startPoint = eventManager.getSelectionStartPoint();
		Point endPoint = eventManager.getSelectionEndPoint();

		if (startPoint == null || endPoint == null) {
			return;
		}

		Rectangle selectionArea = createSelectionRectangle(startPoint, endPoint);
		drawingManager.setSelectionArea(selectionArea);

		if (eventManager.isMouseReleased()) {
			if (selectionArea.width > 5 && selectionArea.height > 5) {
				// 최초 실행 시에만 이전 선택 상태 저장
				if (!executed) {
					previouslySelectedShapes.clear();
					previouslySelectedShapes.addAll(drawingManager.getSelectedShapes());
					executed = true;
				}

				if (!eventManager.isShiftDown()) {
					drawingManager.clearSelection();
				}

				newlySelectedShapes.clear();
				for (GShape shape : drawingManager.getShapes()) {
					if (shape.isInside(selectionArea) && !drawingManager.getSelectedShapes().contains(shape)) {
						drawingManager.addToSelection(shape);
						newlySelectedShapes.add(shape);
					}
				}
			}

			drawingManager.setSelectionArea(null);
		}
	}

	@Override
	public void unexecute() {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();

		// 새로 선택된 도형들의 선택 해제
		for (GShape shape : newlySelectedShapes) {
			drawingManager.removeFromSelection(shape);
		}

		// 이전에 선택되었던 도형들 중 현재 선택되지 않은 것들 다시 선택
		for (GShape shape : previouslySelectedShapes) {
			if (!drawingManager.getSelectedShapes().contains(shape)) {
				drawingManager.addToSelection(shape);
			}
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