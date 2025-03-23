package eventhandler;

import java.awt.Point;
import java.awt.event.MouseEvent;

import command.GCommandManager;
import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GMode;

public class GResizeHandler implements GMouseEventHandler {
	private GCommandManager commandManager;
	private Point startPoint;

	public GResizeHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		startPoint = e.getPoint();

		for (GShape shape : drawingManager.getSelectedShapes()) {
			GShape.ControlPoint cp = shape.getControlPointAt(startPoint);
			if (cp != GShape.ControlPoint.NONE && cp != GShape.ControlPoint.ROTATE) {
				drawingManager.setTargetShapeForResize(shape);
				drawingManager.setActiveControlPoint(cp);
				drawingManager.saveOriginalPositions();
				break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GShape targetShape = drawingManager.getTargetShapeForResize();
		GShape.ControlPoint activeCP = drawingManager.getActiveControlPoint();

		if (targetShape != null && activeCP != null) {
			Point currentPoint = e.getPoint();
			int dx = currentPoint.x - startPoint.x;
			int dy = currentPoint.y - startPoint.y;

			targetShape.resize(activeCP, dx, dy);
			startPoint = currentPoint;

			drawingManager.notifyObservers();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		if (drawingManager.hasPositionsChanged()) {
			commandManager.executeAndStore(GMode.RESIZE);
		}

		startPoint = null;
		drawingManager.resetOperationStates();
		drawingManager.setCurrentMode(GMode.DEFAULT);
		eventManager.setCurrentMouseEventHandler(GMode.DEFAULT);
	}
}
