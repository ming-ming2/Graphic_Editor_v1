package command;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import shapes.GShape;
import state.GDrawingStateManager;
import state.GStateManager;
import type.GMode;

public class GCommandManager extends GStateManager {
	private final Map<GMode, CommandFactory> commandFactories = new HashMap<>();
	private Stack<GCommand> undoStack = new Stack<>();
	private Stack<GCommand> redoStack = new Stack<>();

	// 현재 실행 중인 이동 명령 참조 (드래그 중)
	private GGroupMoveCommand currentMoveCommand;

	private interface CommandFactory {
		GCommand createCommand();
	}

	public GCommandManager() {
		commandFactories.put(GMode.DEFAULT, () -> new DefaultCommand());
		commandFactories.put(GMode.SHAPE, () -> new GShapeCommand());
		commandFactories.put(GMode.GROUP_MOVE, () -> {
			// 이미 생성된 이동 명령이 있다면 재사용
			if (currentMoveCommand != null) {
				return currentMoveCommand;
			}
			// 없으면 새로 생성 및 초기화
			GDrawingStateManager drawingManager = GDrawingStateManager.getInstance();
			Map<GShape, Point> originalPositions = new HashMap<>();

			for (GShape shape : drawingManager.getSelectedShapes()) {
				originalPositions.put(shape, new Point(shape.getBounds().x, shape.getBounds().y));
			}

			currentMoveCommand = new GGroupMoveCommand(originalPositions);
			return currentMoveCommand;
		});
	}

	private GCommand createCommand(GMode mode) {
		CommandFactory factory = commandFactories.get(mode);
		if (factory != null) {
			return factory.createCommand();
		}
		System.err.println("알 수 없는 모드: " + mode);
		return null;
	}

	public void execute(GMode mode) {
		GCommand command = createCommand(mode);
		if (command != null) {
			command.execute();
		}
	}

	public void executeAndStore(GMode mode) {
		if (mode == GMode.GROUP_MOVE) {
			// 이동 명령 특별 처리
			if (currentMoveCommand != null && currentMoveCommand.hasChanged()) {
				// 현재 명령을 스택에 저장
				undoStack.push(currentMoveCommand);
				redoStack.clear();
				notifyObservers();
				System.out.println("명령 스택에 추가: GGroupMoveCommand");
			}
			// 다음 이동을 위해 현재 이동 명령 참조 초기화
			currentMoveCommand = null;
		} else {
			// 다른 모드는 기존 방식대로 처리
			GCommand command = createCommand(mode);
			if (command != null) {
				command.execute();
				if (mode != GMode.DEFAULT || (mode == GMode.DEFAULT && ((DefaultCommand) command).hasChanges())) {
					undoStack.push(command);
					redoStack.clear();
					notifyObservers();
					System.out.println("명령 스택에 추가: " + command.getClass().getSimpleName());
				}
			}
		}
	}

	public void pushCommand(GCommand command) {
		if (command != null) {
			undoStack.push(command);
			redoStack.clear();
			notifyObservers();
			System.out.println("명령 스택에 추가: " + command.getClass().getSimpleName());
		}
	}

	public boolean canUndo() {
		return !undoStack.isEmpty();
	}

	public boolean canRedo() {
		return !redoStack.isEmpty();
	}

	public void undo() {
		if (canUndo()) {
			GCommand command = undoStack.pop();
			command.unexecute();
			redoStack.push(command);
			notifyObservers();
			System.out.println("실행 취소: " + command.getClass().getSimpleName());
		}
	}

	public void redo() {
		if (canRedo()) {
			GCommand command = redoStack.pop();
			command.execute();
			undoStack.push(command);
			notifyObservers();
			System.out.println("다시 실행: " + command.getClass().getSimpleName());
		}
	}
}