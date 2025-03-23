package command;

import java.awt.Point;
import java.awt.Rectangle;

import shapes.GShape;
import state.GDrawingStateManager;

public class GRotateCommand implements GCommand {
	private GShape shape;
	private double originalAngle;
	private double newAngle;

	@Override
	public void execute() {
		GDrawingStateManager stateManager = GDrawingStateManager.getInstance();
		GShape targetShape = stateManager.getTargetShapeForRotation();

		if (targetShape == null) {
			return;
		}

		if (this.shape == null) {
			this.shape = targetShape;
			this.originalAngle = stateManager.getOriginalRotationAngle();
		}

		Point startPoint = stateManager.getRotateStartPoint();
		Point currentPoint = stateManager.getRotateCurrentPoint();

		if (startPoint == null || currentPoint == null) {
			return;
		}

		Rectangle bounds = targetShape.getBounds();
		int centerX = bounds.x + bounds.width / 2;
		int centerY = bounds.y + bounds.height / 2;

		double startAngle = Math.atan2(startPoint.y - centerY, startPoint.x - centerX);
		double currentAngle = Math.atan2(currentPoint.y - centerY, currentPoint.x - centerX);
		double angleDelta = currentAngle - startAngle;

		newAngle = originalAngle + angleDelta;
		targetShape.setRotationAngle(newAngle);

		stateManager.notifyObservers();
	}

	@Override
	public void unexecute() {
		if (shape != null) {
			shape.setRotationAngle(originalAngle);
			GDrawingStateManager.getInstance().notifyObservers();
		}
	}
}
