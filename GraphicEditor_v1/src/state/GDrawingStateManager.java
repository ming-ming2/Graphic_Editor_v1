package state;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import command.DefaultCommand;
import command.GCommandManager;
import command.GGroupMoveCommand;
import command.GGroupSelectionCommand;
import command.GShapeCommand;
import eventhandler.DefaultHandler;
import eventhandler.GMouseEventHandler;
import eventhandler.GMouseEventHandlerRegistry;
import shapes.GShape;
import type.GMode;
import type.GShapeType;

public class GDrawingStateManager extends GStateManager {
	// 단일 객체
	private static GDrawingStateManager drawingStateManager;

	private List<GObserver> observers = new ArrayList<>();
	// commandManager 생성
	GCommandManager commandManager = new GCommandManager();
	private GMouseEventHandlerRegistry mouserEventHandlerRegistry;
	// DrawingPanel에 적용되는 state
	private GMouseEventHandler currentMouseEventHandler;
	private GMode currentMode;
	private GShapeType currentShapeType;
	private List<GShape> shapes;
	private GShape previewShape;

	// 그룹 선택 관련 필드 추가
	private Rectangle selectionArea;
	private List<GShape> selectedShapes;
	private Point dragStartPoint;
	private boolean isDraggingSelection = false;

	private GDrawingStateManager() {
		// 외부 생성 금지. 모든 컴포넌트가 동일한 상태를 공유한다
		this.commandManager.add(GMode.DEFAULT, new DefaultCommand());
		this.commandManager.add(GMode.SHAPE, new GShapeCommand());
		this.commandManager.add(GMode.SELECTION, new GGroupSelectionCommand());
		this.commandManager.add(GMode.GROUP_MOVE, new GGroupMoveCommand());

		// 초기화 순서 변경
		this.currentMode = GMode.DEFAULT;
		this.currentShapeType = null;
		this.shapes = new ArrayList<>();
		this.previewShape = null;
		this.selectedShapes = new ArrayList<>();

		// 순환 참조가 발생하지 않도록 마지막에 초기화
		this.mouserEventHandlerRegistry = new GMouseEventHandlerRegistry(commandManager);
		this.currentMouseEventHandler = new DefaultHandler(commandManager);
	}

	public static GDrawingStateManager getInstance() {
		if (drawingStateManager == null) {
			drawingStateManager = new GDrawingStateManager();
		}
		return drawingStateManager;
	}

	public GMouseEventHandler getCurrentMouseEventHandler() {
		return currentMouseEventHandler;
	}

	public void setCurrentMouseEventHandler(GMouseEventHandler mouseEventHandler) {
		this.currentMouseEventHandler = mouseEventHandler;
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
		setCurrentMouseEventHandler(mouserEventHandlerRegistry.get(mode));
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