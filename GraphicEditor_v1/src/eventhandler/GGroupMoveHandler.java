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

		eventManager.setCurrentPoint(point);
		drawingManager.findShapeAt(point);
		drawingManager.setDragStartPoint(point);
		drawingManager.setDraggingSelection(true);

		commandManager.execute(GMode.GROUP_MOVE);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		Point currentPoint = e.getPoint();

		eventManager.setCurrentPoint(currentPoint);

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

		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			commandManager.execute(GMode.GROUP_MOVE);
			commandManager.executeAndStore(GMode.GROUP_MOVE);
		}

		drawingManager.setSelectionArea(null);
		drawingManager.setDraggingSelection(false);
		drawingManager.setCurrentMode(GMode.DEFAULT);
	}
}
