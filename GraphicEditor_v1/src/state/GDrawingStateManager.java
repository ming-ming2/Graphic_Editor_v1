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
	private List<GShape> selectedShapes = new ArrayList<>();

	private Rectangle selectionArea;
	private Point dragStartPoint;
	private boolean isDraggingSelection = false;

	private File currentFile = null;

	private Map<GShape, Point> originalPositions = new HashMap<>();
	private Map<GShape, Rectangle> originalBounds = new HashMap<>();

	private GShape targetShapeForRotation;
	private Point rotateStartPoint;
	private Point rotateCurrentPoint;
	private double originalRotationAngle;

	private GShape targetShapeForResize;
	private GShape.ControlPoint activeControlPoint;

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

	public GShape getPreviewShape() {
		return previewShape;
	}

	public void setPreviewShape(GShape shape) {
		this.previewShape = shape;
		notifyObservers();
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

	public void addShape(GShape shape) {
		if (shape != null) {
			this.shapes.add(shape);
			notifyObservers();
		}
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

	public GShape findShapeAt(Point point) {
		for (int i = shapes.size() - 1; i >= 0; i--) {
			GShape shape = shapes.get(i);
			if (shape.contains(point)) {
				return shape;
			}
		}
		return null;
	}

	public Rectangle getSelectionArea() {
		return selectionArea;
	}

	public void setSelectionArea(Rectangle area) {
		this.selectionArea = area;
		notifyObservers();
	}

	public List<GShape> getSelectedShapes() {
		return selectedShapes;
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

	public void clearSelection() {
		for (GShape shape : selectedShapes) {
			shape.setSelected(false);
		}
		selectedShapes.clear();
		notifyObservers();
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

	public void moveSelectedShapesToPosition(Point newPosition) {
		if (dragStartPoint == null || selectedShapes.isEmpty())
			return;

		int dx = newPosition.x - dragStartPoint.x;
		int dy = newPosition.y - dragStartPoint.y;

		for (GShape shape : selectedShapes) {
			shape.move(dx, dy);
		}

		dragStartPoint = newPosition;
		notifyObservers();
	}

	public void saveOriginalPositions() {
		originalPositions.clear();
		originalBounds.clear();
		for (GShape shape : selectedShapes) {
			originalPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
			originalBounds.put(shape, new Rectangle(shape.getBounds()));
		}
	}

	public Map<GShape, Point> getOriginalPositions() {
		return new HashMap<>(originalPositions);
	}

	public Rectangle getOriginalBounds(GShape shape) {
		Rectangle bounds = originalBounds.get(shape);
		return bounds != null ? new Rectangle(bounds) : null;
	}

	public Map<GShape, Point> getCurrentPositions() {
		Map<GShape, Point> currentPositions = new HashMap<>();
		for (GShape shape : selectedShapes) {
			currentPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
		}
		return currentPositions;
	}

	public boolean hasPositionsChanged() {
		for (GShape shape : originalBounds.keySet()) {
			if (selectedShapes.contains(shape)) {
				Rectangle origBounds = originalBounds.get(shape);
				Rectangle currBounds = shape.getBounds();
				if (origBounds.x != currBounds.x || origBounds.y != currBounds.y || origBounds.width != currBounds.width
						|| origBounds.height != currBounds.height) {
					return true;
				}
			}
		}
		return false;
	}

	public void setTargetShapeForRotation(GShape shape) {
		this.targetShapeForRotation = shape;
		if (shape != null) {
			this.originalRotationAngle = shape.getRotationAngle();
		}
	}

	public GShape getTargetShapeForRotation() {
		return targetShapeForRotation;
	}

	public void setRotateStartPoint(Point point) {
		this.rotateStartPoint = point;
	}

	public Point getRotateStartPoint() {
		return rotateStartPoint;
	}

	public void setRotateCurrentPoint(Point point) {
		this.rotateCurrentPoint = point;
	}

	public Point getRotateCurrentPoint() {
		return rotateCurrentPoint;
	}

	public double getOriginalRotationAngle() {
		return originalRotationAngle;
	}

	public boolean hasRotationChanged() {
		if (targetShapeForRotation == null)
			return false;
		return Math.abs(originalRotationAngle - targetShapeForRotation.getRotationAngle()) > 0.001;
	}

	public void setTargetShapeForResize(GShape shape) {
		this.targetShapeForResize = shape;
	}

	public GShape getTargetShapeForResize() {
		return targetShapeForResize;
	}

	public void setActiveControlPoint(GShape.ControlPoint controlPoint) {
		this.activeControlPoint = controlPoint;
	}

	public GShape.ControlPoint getActiveControlPoint() {
		return activeControlPoint;
	}

	public void resetOperationStates() {
		targetShapeForRotation = null;
		rotateStartPoint = null;
		rotateCurrentPoint = null;
		targetShapeForResize = null;
		activeControlPoint = null;
	}
}
