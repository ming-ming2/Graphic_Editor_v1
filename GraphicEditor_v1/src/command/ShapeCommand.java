package command;

import java.awt.event.MouseEvent;
import java.util.List;

import dto.ShapeCommandDTO;
import global.ShapeType;
import shapes.Shape;
import shapes.ShapeFactory;
import view.DrawingPanel;

public class ShapeCommand implements Command<ShapeCommandDTO> {
	@Override
	public void execute(ShapeCommandDTO dto) {
		List<MouseEvent> events = dto.getMouseEvents();
		ShapeType shapeType = dto.getShapeType();
		DrawingPanel drawingPanel = dto.getDrawingPanel();

		if (events == null || events.size() < 2 || shapeType == null || drawingPanel == null) {
			return; // 필요한 정보가 없으면 실행하지 않음
		}

		Shape shape = ShapeFactory.getShape(shapeType, events);

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
