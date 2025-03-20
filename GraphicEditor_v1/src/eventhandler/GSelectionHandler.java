package eventhandler;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import command.GCommandManager;
import state.GDrawingStateManager;
import type.GMode;

public class GSelectionHandler implements GMouseEventHandler {
	private Point startPoint;
	private GDrawingStateManager drawingStateManager;
	private GCommandManager commandManager;

	public GSelectionHandler(GCommandManager commandManager) {
		this.commandManager = commandManager;
		this.drawingStateManager = GDrawingStateManager.getInstance();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		startPoint = e.getPoint();

		// 기존 선택 초기화
		drawingStateManager.clearSelection();

		// 선택 영역 초기화
		drawingStateManager.setSelectionArea(new Rectangle(startPoint.x, startPoint.y, 0, 0));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (startPoint != null) {
			// 선택 영역 생성
			Rectangle selectionArea = createSelectionRectangle(e.getPoint());
			drawingStateManager.setSelectionArea(selectionArea);

			// 명령 실행 (선택 영역 내 도형 선택)
			commandManager.execute(GMode.SELECTION);

			// 선택 완료 후 기본 모드로 돌아가기
			drawingStateManager.setCurrentMode(GMode.DEFAULT);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (startPoint != null) {
			// 선택 영역 업데이트
			Rectangle selectionArea = createSelectionRectangle(e.getPoint());
			drawingStateManager.setSelectionArea(selectionArea);
		}
	}

	private Rectangle createSelectionRectangle(Point currentPoint) {
		int x = Math.min(startPoint.x, currentPoint.x);
		int y = Math.min(startPoint.y, currentPoint.y);
		int width = Math.abs(currentPoint.x - startPoint.x);
		int height = Math.abs(currentPoint.y - startPoint.y);

		return new Rectangle(x, y, width, height);
	}
}