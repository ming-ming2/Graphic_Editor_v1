package state;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shapes.GShape;
import type.GMode;
import type.GShapeType;

public class GDrawingStateManager extends GStateManager {
	private static GDrawingStateManager drawingStateManager;
	private GMode currentMode;
	private GShapeType currentShapeType;
	private GShape previewShape;
	private List<GShape> shapes = new ArrayList<>();
	private Rectangle selectionArea;
	private List<GShape> selectedShapes = new ArrayList<>();
	private Point dragStartPoint;
	private boolean isDraggingSelection = false;
	private File currentFile = null;
	// 도형의 원본 위치 저장 맵 추가
	private Map<GShape, Point> originalPositions = new HashMap<>();

	/**
	 * 현재 선택된 도형들의 원본 위치를 저장합니다. 드래그 시작 시 호출됩니다.
	 */
	public void saveOriginalPositions() {
		originalPositions.clear();
		for (GShape shape : selectedShapes) {
			originalPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
		}
	}

	/**
	 * 저장된 원본 위치 맵의 복사본을 반환합니다.
	 */
	public Map<GShape, Point> getOriginalPositions() {
		return new HashMap<>(originalPositions);
	}

	/**
	 * 현재 선택된 도형들의 현재 위치 맵을 생성해 반환합니다.
	 */
	public Map<GShape, Point> getCurrentPositions() {
		Map<GShape, Point> currentPositions = new HashMap<>();
		for (GShape shape : selectedShapes) {
			currentPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
		}
		return currentPositions;
	}

	/**
	 * 원본 위치와 현재 위치를 비교하여 변경 여부를 반환합니다.
	 */
	public boolean hasPositionsChanged() {
		for (GShape shape : originalPositions.keySet()) {
			if (selectedShapes.contains(shape)) {
				Point original = originalPositions.get(shape);
				Point current = new Point(shape.getBounds().x, shape.getBounds().y);
				if (original.x != current.x || original.y != current.y) {
					return true;
				}
			}
		}
		return false;
	}

	private GDrawingStateManager() {
		this.currentMode = GMode.DEFAULT;
		this.currentShapeType = null;
		this.previewShape = null;
	}

	public static GDrawingStateManager getInstance() {
		if (drawingStateManager == null) {
			drawingStateManager = new GDrawingStateManager();
		}
		return drawingStateManager;
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(File file) {
		this.currentFile = file;
	}

	public GMode getCurrentMode() {
		return currentMode;
	}

	public List<GShape> getShapes() {
		return shapes;
	}

	public void setShapes(List<GShape> shapes) {
		this.shapes = new ArrayList<>(shapes);
		List<GShape> shapesToRemove = new ArrayList<>();
		for (GShape selected : selectedShapes) {
			if (!shapes.contains(selected)) {
				shapesToRemove.add(selected);
			}
		}
		for (GShape shape : shapesToRemove) {
			selectedShapes.remove(shape);
		}
		notifyObservers();
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
		if (shape != null) {
			this.shapes.add(shape);
			notifyObservers();
		}
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

	public void removeShape(GShape shape) {
		if (shapes.contains(shape)) {
			if (shape.isSelected()) {
				removeFromSelection(shape);
			}
			shapes.remove(shape);
			notifyObservers();
		}
	}

	public void addToSelection(GShape shape) {
		if (!selectedShapes.contains(shape) && shapes.contains(shape)) {
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
