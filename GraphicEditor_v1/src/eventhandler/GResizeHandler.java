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

		// 선택된 도형들 중에서 제어점이 클릭된 도형 찾기
		for (GShape shape : drawingManager.getSelectedShapes()) {
			GShape.ControlPoint cp = shape.getControlPointAt(startPoint);
			if (cp != GShape.ControlPoint.NONE && cp != GShape.ControlPoint.ROTATE) {
				// 상태 정보 저장
				drawingManager.setTargetShapeForResize(shape);
				drawingManager.setActiveControlPoint(cp);

				// 원본 상태 저장
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
			// 현재 위치와 이동 거리 계산
			Point currentPoint = e.getPoint();
			int dx = currentPoint.x - startPoint.x;
			int dy = currentPoint.y - startPoint.y;

			// 리사이즈 명령 직접 실행
			targetShape.resize(activeCP, dx, dy);

			// 다음 드래그를 위해 시작점 업데이트
			startPoint = currentPoint;

			// UI 갱신
			drawingManager.notifyObservers();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GEventStateMananger eventManager = GEventStateMananger.getInstance();

		// 리사이즈 명령 생성 및 스택에 추가
		if (drawingManager.hasPositionsChanged()) {
			commandManager.executeAndStore(GMode.RESIZE);
		}

		// 상태 초기화
		startPoint = null;
		drawingManager.resetOperationStates();

		// 기본 모드로 돌아가기
		drawingManager.setCurrentMode(GMode.DEFAULT);
		eventManager.setCurrentMouseEventHandler(GMode.DEFAULT);
	}
}