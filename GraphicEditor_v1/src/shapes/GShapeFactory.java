package shapes;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import global.GShapeType;

public class GShapeFactory {
	public static GShape getShape(GShapeType type, List<MouseEvent> events) {
		if (events == null || events.isEmpty()) {
			return null;
		}
		switch (type) {
		case Rectangle:
			return createRectangle(events);
		// 추가 예정
		default:
			return null;
		}

	}

	private static GShape createRectangle(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GRectangle(new Point(x, y), width, height);
	}

}
