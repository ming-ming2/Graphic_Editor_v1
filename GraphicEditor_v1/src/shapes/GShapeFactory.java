package shapes;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import type.GShapeType;

public class GShapeFactory {
	public static GShape getShape(GShapeType type, List<MouseEvent> events) {
		if (events == null || events.isEmpty()) {
			return null;
		}

		switch (type) {
		case Line:
			return createLine(events);
		case Arrow:
			return createArrow(events);
		case Rectangle:
			return createRectangle(events);
		case RoundRectangle:
			return createRoundRectangle(events);
		case Oval:
			return createOval(events);
		case Diamond:
			return createDiamond(events);
		case Triangle:
			return createTriangle(events);
		case RightTriangle:
			return createRightTriangle(events);
		case Pentagon:
			return createPentagon(events);
		case Hexagon:
			return createHexagon(events);
		case Star:
			return createStar(events);
		default:
			return null;
		}
	}

	private static GShape createLine(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		return new GLine(startPoint, endPoint);
	}

	private static GShape createArrow(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		return new GArrow(startPoint, endPoint);
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

	private static GShape createRoundRectangle(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GRoundRectangle(new Point(x, y), width, height);
	}

	private static GShape createOval(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GOval(new Point(x, y), width, height);
	}

	private static GShape createDiamond(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GDiamond(new Point(x, y), width, height);
	}

	private static GShape createTriangle(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GTriangle(new Point(x, y), width, height);
	}

	private static GShape createRightTriangle(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GRightTriangle(new Point(x, y), width, height);
	}

	private static GShape createPentagon(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GPentagon(new Point(x, y), width, height);
	}

	private static GShape createHexagon(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GHexagon(new Point(x, y), width, height);
	}

	private static GShape createStar(List<MouseEvent> events) {
		MouseEvent startEvent = events.get(0);
		MouseEvent endEvent = events.get(events.size() - 1);

		Point startPoint = startEvent.getPoint();
		Point endPoint = endEvent.getPoint();

		int x = Math.min(startPoint.x, endPoint.x);
		int y = Math.min(startPoint.y, endPoint.y);
		int width = Math.abs(endPoint.x - startPoint.x);
		int height = Math.abs(endPoint.y - startPoint.y);

		return new GStar(new Point(x, y), width, height);
	}
}