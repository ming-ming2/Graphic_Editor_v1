package eventhandler;

import java.awt.Point;
import java.awt.event.MouseEvent;

import command.GCommandManager;
import shapes.GShape;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GMode;

public class GRotateHandler implements GMouseEventHandler {
	private GCommandManager commandManager;

	public GRotateHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		Point clickPoint = e.getPoint();

		drawingManager.setRotateStartPoint(clickPoint);

		for (GShape shape : drawingManager.getSelectedShapes()) {
			GShape.ControlPoint cp = shape.getControlPointAt(clickPoint);
			if (cp == GShape.ControlPoint.ROTATE) {
				drawingManager.setTargetShapeForRotation(shape);
				commandManager.execute(GMode.ROTATE);
				break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GShape targetShape = drawingManager.getTargetShapeForRotation();

		if (targetShape != null) {
			drawingManager.setRotateCurrentPoint(e.getPoint());
			commandManager.execute(GMode.ROTATE);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		drawingManager.setRotateCurrentPoint(e.getPoint());

		if (drawingManager.hasRotationChanged()) {
			commandManager.executeAndStore(GMode.ROTATE);
		}

		drawingManager.resetOperationStates();
		drawingManager.setCurrentMode(GMode.DEFAULT);
		eventManager.setCurrentMouseEventHandler(GMode.DEFAULT);
	}
}
