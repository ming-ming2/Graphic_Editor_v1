package state;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

public class GEventStateMananger extends GStateManager {
	private static GEventStateMananger eventStateManager;
	private List<MouseEvent> mouseEvents;
	private Point currentPoint;
	private Point previousPoint;

	private GEventStateMananger() {
		mouseEvents = null;
		currentPoint = null;
		previousPoint = null;
	}

	public static GEventStateMananger getInstance() {
		if (eventStateManager == null) {
			eventStateManager = new GEventStateMananger();
		}
		return eventStateManager;
	}

	public void setMouseEvents(List<MouseEvent> mouseEvents) {
		this.mouseEvents = mouseEvents;
	}

	public List<MouseEvent> getMouseEvents() {
		return mouseEvents;
	}

	public void setCurrentPoint(Point point) {
		if (this.currentPoint != null) {
			this.previousPoint = new Point(this.currentPoint);
		} else {
			this.previousPoint = new Point(point);
		}
		this.currentPoint = new Point(point);
		notifyObservers();
	}

	public Point getCurrentPoint() {
		return currentPoint;
	}

	public Point getPreviousPoint() {
		return previousPoint;
	}

	public Point getDelta() {
		if (currentPoint != null && previousPoint != null) {
			return new Point(currentPoint.x - previousPoint.x, currentPoint.y - previousPoint.y);
		}
		return new Point(0, 0);
	}
}