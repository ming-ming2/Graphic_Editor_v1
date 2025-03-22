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

	// commandManager 생성

	// DrawingPanel에 적용되는 state

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
		// 외부 생성 금지. 모든 컴포넌트가 동일한 상태를 공유한다this.commandManager = new GCommandManager();
		this.setCurrentMode(GMode.DEFAULT);
		this.setCurrentShapeType(null);
		this.setPreviewShape(null);
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

	// 선택 영역 관련 메서드
	public void setSelectionArea(Rectangle area) {
		this.selectionArea = area;
		notifyObservers();
	}

	public Rectangle getSelectionArea() {
		return selectionArea;
	}

	// 선택된 도형들 관리
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

	// 드래그 시작점 관리
	public void setDragStartPoint(Point point) {
		this.dragStartPoint = point;
	}

	public Point getDragStartPoint() {
		return dragStartPoint;
	}

	// 드래그 중인지 여부
	public boolean isDraggingSelection() {
		return isDraggingSelection;
	}

	public void setDraggingSelection(boolean dragging) {
		this.isDraggingSelection = dragging;
	}

	// 선택된 도형들 이동
	public void moveSelectedShapes(int dx, int dy) {
		for (GShape shape : selectedShapes) {
			shape.move(dx, dy);
		}
		notifyObservers();
	}

	// 도형 하나 찾기 (클릭 위치에 있는)
	public GShape findShapeAt(Point point) {
		// 역순으로 검색 (나중에 그려진 도형이 위에 있으므로)
		for (int i = shapes.size() - 1; i >= 0; i--) {
			GShape shape = shapes.get(i);
			if (shape.contains(point)) {
				return shape;
			}
		}
		return null;
	}
}