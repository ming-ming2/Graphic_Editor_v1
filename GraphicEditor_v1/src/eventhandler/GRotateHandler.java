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

		// 상태 정보 저장
		drawingManager.setRotateStartPoint(clickPoint);

		// 선택된 도형들 중에서 회전 제어점이 클릭된 도형 찾기
		for (GShape shape : drawingManager.getSelectedShapes()) {
			GShape.ControlPoint cp = shape.getControlPointAt(clickPoint);
			if (cp == GShape.ControlPoint.ROTATE) {
				drawingManager.setTargetShapeForRotation(shape);

				// 초기화 명령 실행 (rotationAngle 초기값 저장 등)
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
			// 현재 지점 업데이트
			drawingManager.setRotateCurrentPoint(e.getPoint());

			// 회전 명령 실행
			commandManager.execute(GMode.ROTATE);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		// 현재 지점 최종 업데이트
		drawingManager.setRotateCurrentPoint(e.getPoint());

		// 변경 사항이 있을 경우만 명령 스택에 추가
		if (drawingManager.hasRotationChanged()) {
			commandManager.executeAndStore(GMode.ROTATE);
		}

		// 상태 초기화
		drawingManager.resetOperationStates();

		// 기본 모드로 돌아가기
		drawingManager.setCurrentMode(GMode.DEFAULT);
		eventManager.setCurrentMouseEventHandler(GMode.DEFAULT);
	}
}