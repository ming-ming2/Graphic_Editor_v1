package state;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import command.GCommandManager;
import eventhandler.GDefaultHandler;
import eventhandler.GMouseEventHandler;
import eventhandler.GMouseEventHandlerRegistry;
import type.GMode;

public class GEventStateMananger extends GStateManager {
	private static GEventStateMananger eventStateManager;
	private GCommandManager commandManager;
	private List<MouseEvent> mouseEvents;
	private Point currentPoint;
	private Point previousPoint;
	private Point selectionStartPoint;
	private Point selectionEndPoint;
	private boolean shiftDown;
	private boolean isMouseReleased;
	private GMouseEventHandler currentMouseEventHandler;
	private GMouseEventHandlerRegistry mouserEventHandlerRegistry;

	private GEventStateMananger() {
		this.commandManager = new GCommandManager();
		this.mouserEventHandlerRegistry = new GMouseEventHandlerRegistry(commandManager);
		this.currentMouseEventHandler = new GDefaultHandler(commandManager);
		mouseEvents = null;
		currentPoint = null;
		previousPoint = null;
		selectionStartPoint = null;
		selectionEndPoint = null;
		shiftDown = false;
		isMouseReleased = false;
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

	public GMouseEventHandler getCurrentMouseEventHandler() {
		return currentMouseEventHandler;
	}

	public void setCurrentMouseEventHandler(GMode mode) {
		this.currentMouseEventHandler = mouserEventHandlerRegistry.get(mode);
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

	public void setSelectionStartPoint(Point point) {
		this.selectionStartPoint = point;
	}

	public Point getSelectionStartPoint() {
		return selectionStartPoint;
	}

	public void setSelectionEndPoint(Point point) {
		this.selectionEndPoint = point;
		notifyObservers();
	}

	public Point getSelectionEndPoint() {
		return selectionEndPoint;
	}

	public void setShiftDown(boolean shiftDown) {
		this.shiftDown = shiftDown;
	}

	public boolean isShiftDown() {
		return shiftDown;
	}

	public void setMouseReleased(boolean released) {
		this.isMouseReleased = released;
	}

	public boolean isMouseReleased() {
		return isMouseReleased;
	}
}