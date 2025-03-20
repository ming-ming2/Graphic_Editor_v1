package state;

import java.util.ArrayList;
import java.util.List;

import command.DefaultCommand;
import command.GCommandManager;
import command.GShapeCommand;
import eventhandler.GMouseEventHandler;
import eventhandler.GMouseEventHandlerRegistry;
import shapes.GShape;
import type.GMode;
import type.GShapeType;

// 현재는 하나의 클래스에서 전부 관리. 추후 분리 예정
public class GDrawingStateManager implements GStateManager {
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
	// ..추가예정

	private GDrawingStateManager() {
		// 외부 생성 금지. 모든 컴포넌트가 동일한 상태를 공유한다
		this.commandManager.add(GMode.DEFAULT, new DefaultCommand());
		this.commandManager.add(GMode.SHAPE, new GShapeCommand());
		this.mouserEventHandlerRegistry = new GMouseEventHandlerRegistry(commandManager);
		this.currentMode = GMode.DEFAULT;
		this.currentShapeType = null;
		this.shapes = new ArrayList<>();
		this.previewShape = null;
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

	@Override
	public void addObserver(GObserver observer) {
		observers.add(observer);
	}

	@Override
	public void notifyObservers() {
		for (GObserver observer : observers) {
			observer.update();
		}
	}
}
