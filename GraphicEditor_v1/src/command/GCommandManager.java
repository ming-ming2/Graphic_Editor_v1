package command;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import state.GStateManager;
import type.GMode;

public class GCommandManager extends GStateManager {
	private final Map<GMode, CommandFactory> commandFactories = new HashMap<>();
	private Stack<GCommand> undoStack = new Stack<>();
	private Stack<GCommand> redoStack = new Stack<>();

	// 커맨드 팩토리 인터페이스
	private interface CommandFactory {
		GCommand createCommand();
	}

	public GCommandManager() {
		// 커맨드 팩토리 등록
		commandFactories.put(GMode.DEFAULT, () -> new DefaultCommand());
		commandFactories.put(GMode.SHAPE, () -> new GShapeCommand());
		commandFactories.put(GMode.GROUP_MOVE, () -> new GGroupMoveCommand());
	}

	// 모드에 맞는 새 명령 객체 생성
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
		GCommand command = createCommand(mode);
		if (command != null) {
			command.execute();
			undoStack.push(command);
			redoStack.clear();
			notifyObservers();
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
		}
	}

	public void redo() {
		if (canRedo()) {
			GCommand command = redoStack.pop();
			command.execute();
			undoStack.push(command);
			notifyObservers();
		}
	}
}