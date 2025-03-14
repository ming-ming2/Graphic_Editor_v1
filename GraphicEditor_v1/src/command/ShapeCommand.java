package command;

import java.awt.event.MouseEvent;
import java.util.List;

import global.ContextKey;
import global.ShapeType;
import shapes.Shape;
import shapes.ShapeFactory;
import view.DrawingPanel;

public class ShapeCommand implements Command {
	@Override
	public void execute(CommandContext context) {
		List<MouseEvent> events = context.get(ContextKey.MOUSE_EVENTS, List.class);
		ShapeType shapeType = context.get(ContextKey.SHAPE_TYPE, ShapeType.class);
		DrawingPanel drawingPanel = context.get(ContextKey.DRAWING_PANEL, DrawingPanel.class);

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
