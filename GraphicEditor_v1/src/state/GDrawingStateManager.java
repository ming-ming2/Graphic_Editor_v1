package state;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import shapes.GShape;
import type.GMode;
import type.GShapeType;

public class GDrawingStateManager extends GStateManager {
	// 단일 객체
	private static GDrawingStateManager drawingStateManager;

	private GMode currentMode;
	private GShapeType currentShapeType;
	private GShape previewShape;
	private List<GShape> shapes = new ArrayList<>();
	// 그룹 선택 관련 필드 추가
	private Rectangle selectionArea;
	private List<GShape> selectedShapes = new ArrayList<>();
	private Point dragStartPoint;
	private boolean isDraggingSelection = false;

	private GDrawingStateManager() {
		// 외부 생성 금지. 모든 컴포넌트가 동일한 상태를 공유한다
		this.currentMode = GMode.DEFAULT;
		this.currentMode = null;
		this.previewShape = null;
	}

	public static GDrawingStateManager getInstance() {
		if (drawingStateManager == null) {
			drawingStateManager = new GDrawingStateManager();
		}
		return drawingStateManager;
	}

	public GMode getCurrentMode() {
		return currentMode;
	}

	public List<GShape> getShapes() {
		return shapes;
	}

	public GShape getPreviewShape() {
		return previewShape;
	}

	public void setCurrentMode(GMode mode) {
		this.currentMode = mode;
		GEventStateMananger.getInstance().setCurrentMouseEventHandler(mode);
	}

	public GShapeType getCurrentShapeType() {
		return currentShapeType;
	}

	public void setCurrentShapeType(GShapeType shapeType) {
		this.currentShapeType = shapeType;
	}

	public void addShape(GShape shape) {
		this.shapes.add(shape);
		notifyObservers();
	}

	public void setPreviewShape(GShape shape) {
		this.previewShape = shape;
		notifyObservers();
	}

	public void setSelectionArea(Rectangle area) {
		this.selectionArea = area;
		notifyObservers();
	}

	public Rectangle getSelectionArea() {
		return selectionArea;
	}

	public List<GShape> getSelectedShapes() {
		return selectedShapes;
	}

	public void clearSelection() {
		for (GShape shape : selectedShapes) {
			shape.setSelected(false);
		}
		selectedShapes.clear();
		notifyObservers();
	}

	public void addToSelection(GShape shape) {
		if (!selectedShapes.contains(shape)) {
			shape.setSelected(true);
			selectedShapes.add(shape);
			notifyObservers();
		}
	}

	public void removeFromSelection(GShape shape) {
		if (selectedShapes.contains(shape)) {
			shape.setSelected(false);
			selectedShapes.remove(shape);
			notifyObservers();
		}
	}

	public void selectShapesInArea() {
		if (selectionArea == null)
			return;

		clearSelection();

		for (GShape shape : shapes) {
			if (shape.isInside(selectionArea)) {
				addToSelection(shape);
			}
		}
	}

	public void setDragStartPoint(Point point) {
		this.dragStartPoint = point;
	}

	public Point getDragStartPoint() {
		return dragStartPoint;
	}

	public boolean isDraggingSelection() {
		return isDraggingSelection;
	}

	public void setDraggingSelection(boolean dragging) {
		this.isDraggingSelection = dragging;
	}

	public void moveSelectedShapes(int dx, int dy) {
		for (GShape shape : selectedShapes) {
			shape.move(dx, dy);
		}
		notifyObservers();
	}

	public GShape findShapeAt(Point point) {
		for (int i = shapes.size() - 1; i >= 0; i--) {
			GShape shape = shapes.get(i);
			if (shape.contains(point)) {
				return shape;
			}
		}
		return null;
	}

	public void moveSelectedShapesToPosition(Point newPosition) {
		if (dragStartPoint == null || selectedShapes.isEmpty()) {
			return;
		}

		int dx = newPosition.x - dragStartPoint.x;
		int dy = newPosition.y - dragStartPoint.y;

		for (GShape shape : selectedShapes) {
			shape.move(dx, dy);
		}
		dragStartPoint = newPosition;
		notifyObservers();
	}
}