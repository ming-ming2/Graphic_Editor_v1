package command;

import java.awt.event.MouseEvent;
import java.util.List;

import shapes.GShape;
import shapes.GShapeFactory;
import state.GDrawingStateManager;
import state.GEventStateMananger;
import type.GShapeType;

public class GShapeCommand implements GCommand {
	@Override
	public void execute() {
		// 이벤트 상태 관리자로부터 필요한 정보 가져오기
		GEventStateMananger eventManager = GEventStateMananger.getInstance();
		List<MouseEvent> events = eventManager.getMouseEvents();

		GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
		GShapeType shapeType = drawingManager.getCurrentShapeType();

		// 유효성 검사
		if (events == null || events.size() < 2 || shapeType == null) {
			return;
		}

		// 도형 생성
		GShape shape = GShapeFactory.getShape(shapeType, events);

		if (shape != null) {
			MouseEvent lastEvent = events.get(events.size() - 1);
			if (lastEvent.getID() == MouseEvent.MOUSE_RELEASED) {
				// 마우스 릴리즈 시 실제 도형 추가
				drawingManager.addShape(shape);
				drawingManager.setPreviewShape(null);
			} else {
				// 드래그 중에는 미리보기 도형 설정
				drawingManager.setPreviewShape(shape);
			}
		}
	}
}