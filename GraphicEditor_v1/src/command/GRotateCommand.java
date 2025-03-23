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

		// 명령 객체 초기화
		if (this.shape == null) {
			this.shape = targetShape;
			this.originalAngle = stateManager.getOriginalRotationAngle();
		}

		Point startPoint = stateManager.getRotateStartPoint();
		Point currentPoint = stateManager.getRotateCurrentPoint();

		if (startPoint == null || currentPoint == null) {
			return;
		}

		// 도형 중심점 계산
		Rectangle bounds = targetShape.getBounds();
		int centerX = bounds.x + bounds.width / 2;
		int centerY = bounds.y + bounds.height / 2;

		// 각도 계산
		double startAngle = Math.atan2(startPoint.y - centerY, startPoint.x - centerX);
		double currentAngle = Math.atan2(currentPoint.y - centerY, currentPoint.x - centerX);
		double angleDelta = currentAngle - startAngle;

		// 현재 명령 실행 시 적용할 최종 각도
		newAngle = originalAngle + angleDelta;

		// 각도 적용
		targetShape.setRotationAngle(newAngle);

		// UI 갱신
		stateManager.notifyObservers();
	}

	@Override
	public void unexecute() {
		if (shape != null) {
			// 원래 회전 각도로 복원
			shape.setRotationAngle(originalAngle);

			// UI 갱신
			GDrawingStateManager.getInstance().notifyObservers();
		}
	}
}