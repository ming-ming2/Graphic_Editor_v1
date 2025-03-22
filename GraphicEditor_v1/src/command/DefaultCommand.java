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

		if (startPoint == null || endPoint == null) {
			return;
		}

		Rectangle selectionArea = createSelectionRectangle(startPoint, endPoint);
		drawingManager.setSelectionArea(selectionArea);

		if (eventManager.isMouseReleased()) {
			if (selectionArea.width > 5 && selectionArea.height > 5) {
				if (!eventManager.isShiftDown()) {
					drawingManager.clearSelection();
				}

				for (GShape shape : drawingManager.getShapes()) {
					if (shape.isInside(selectionArea)) {
						drawingManager.addToSelection(shape);
					}
				}
			}

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