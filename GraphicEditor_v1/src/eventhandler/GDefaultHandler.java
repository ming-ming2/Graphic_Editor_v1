package eventhandler;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import command.GCommandManager;
import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GMode;

public class GDefaultHandler implements GMouseEventHandler {
	private Point startPoint;
	private GDrawingStateManager drawingStateManager;
	private GCommandManager commandManager;
	private boolean isMovingShape = false;
	private GShape clickedShape = null;

	public GDefaultHandler(GCommandManager commandManager) {
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

		for (GShape shape : drawingStateManager.getSelectedShapes()) {
			GShape.ControlPoint cp = shape.getControlPointAt(startPoint);
			if (cp != GShape.ControlPoint.NONE) {
				if (cp == GShape.ControlPoint.ROTATE) {
					drawingStateManager.setCurrentMode(GMode.ROTATE);
					eventManager.setCurrentMouseEventHandler(GMode.ROTATE);
					eventManager.getCurrentMouseEventHandler().mousePressed(e);
					return;
				} else {
					drawingStateManager.setCurrentMode(GMode.RESIZE);
					eventManager.setCurrentMouseEventHandler(GMode.RESIZE);
					eventManager.getCurrentMouseEventHandler().mousePressed(e);
					return;
				}
			}
		}

		clickedShape = drawingStateManager.findShapeAt(startPoint);

		if (clickedShape != null) {
			handleShapeClick(clickedShape, e.isShiftDown());
			isMovingShape = true;
			drawingStateManager.setDragStartPoint(startPoint);
			drawingStateManager.setDraggingSelection(true);
			drawingStateManager.setCurrentMode(GMode.GROUP_MOVE);
			commandManager.execute(GMode.GROUP_MOVE);
		} else if (!e.isShiftDown()) {
			drawingStateManager.clearSelection();
		}

		drawingStateManager.setSelectionArea(new Rectangle(startPoint.x, startPoint.y, 0, 0));
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
			commandManager.execute(GMode.GROUP_MOVE);
		} else {
			commandManager.execute(GMode.DEFAULT);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		eventManager.setSelectionEndPoint(e.getPoint());
		eventManager.setMouseReleased(true);

		if (isMovingShape) {
			commandManager.execute(GMode.GROUP_MOVE);
			drawingStateManager.setDraggingSelection(false);
			commandManager.executeAndStore(GMode.GROUP_MOVE);
			drawingStateManager.setCurrentMode(GMode.DEFAULT);
		} else if (startPoint != null) {
			commandManager.executeAndStore(GMode.DEFAULT);
		}

		drawingStateManager.setSelectionArea(null);
		isMovingShape = false;
		clickedShape = null;
		startPoint = null;
	}

	private void handleShapeClick(GShape shape, boolean isShiftDown) {
		if (!isShiftDown && !shape.isSelected()) {
			drawingStateManager.clearSelection();
		}
		if (shape.isSelected() && isShiftDown) {
			drawingStateManager.removeFromSelection(shape);
		} else {
			drawingStateManager.addToSelection(shape);
		}
	}
}
