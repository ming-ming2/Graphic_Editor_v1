package command;

import java.awt.event.MouseEvent;
import java.util.List;

import dto.GShapeCommandDTO;
import global.GShapeType;
import shapes.GShape;
import shapes.GShapeFactory;
import view.GDrawingPanel;

public class GShapeCommand implements GCommand<GShapeCommandDTO> {
	@Override
	public void execute(GShapeCommandDTO dto) {
		List<MouseEvent> events = dto.getMouseEvents();
		GShapeType shapeType = dto.getShapeType();
		GDrawingPanel drawingPanel = dto.getDrawingPanel();

		if (events == null || events.size() < 2 || shapeType == null || drawingPanel == null) {
			return; // 필요한 정보가 없으면 실행하지 않음
		}

		GShape shape = GShapeFactory.getShape(shapeType, events);

		if (shape != null) {
			MouseEvent lastEvent = events.get(events.size() - 1);
			if (lastEvent.getID() == MouseEvent.MOUSE_RELEASED) {
				drawingPanel.addShape(shape);
				drawingPanel.setPreviewShape(null);
			} else {
				// 드래그 중 미리보기 도형 업데이트
				drawingPanel.setPreviewShape(shape);
			}
		}
	}
}
