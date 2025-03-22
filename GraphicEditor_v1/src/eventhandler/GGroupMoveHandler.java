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
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		eventManager.setCurrentPoint(e.getPoint());

		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			commandManager.executeAndStore(GMode.GROUP_MOVE);
		}

		// 선택 영역 명시적으로 초기화
		drawingManager.setSelectionArea(null);
		drawingManager.setDraggingSelection(false);
		drawingManager.setCurrentMode(GMode.DEFAULT);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		eventManager.setCurrentPoint(e.getPoint());

		if (drawingManager.isDraggingSelection() && !drawingManager.getSelectedShapes().isEmpty()) {
			commandManager.execute(GMode.GROUP_MOVE);
		}
	}
}