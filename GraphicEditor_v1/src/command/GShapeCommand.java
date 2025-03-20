package command;

import java.awt.event.MouseEvent;
import java.util.List;

import dto.GShapeCommandDTO;
import shapes.GShape;
import shapes.GShapeFactory;
import state.GDrawingStateManager;
import type.GShapeType;

public class GShapeCommand implements GCommand<GShapeCommandDTO> {
	@Override
	public void execute(GShapeCommandDTO dto) {
		List<MouseEvent> events = dto.getMouseEvents();
		GDrawingStateManager drawingStateManager = GDrawingStateManager.getInstance();
		GShapeType shapeType = drawingStateManager.getCurrentShapeType();

		if (events == null || events.size() < 2 || shapeType == null) {
			return; // 필요한 정보가 없으면 실행하지 않음
		}

		GShape shape = GShapeFactory.getShape(shapeType, events);

		if (shape != null) {
			MouseEvent lastEvent = events.get(events.size() - 1);
			if (lastEvent.getID() == MouseEvent.MOUSE_RELEASED) {
				drawingStateManager.addShape(shape);
				drawingStateManager.setPreviewShape(null);
			} else {
				// 드래그 중 미리보기 도형 업데이트
				drawingStateManager.setPreviewShape(shape);
			}
		}
	}
}
