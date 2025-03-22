package command;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import state.GStateManager;
import type.GMode;

public class GCommandManager extends GStateManager {
	private final Map<GMode, Class<? extends GCommand>> commandClasses = new HashMap<>();
	private Stack<GCommand> undoStack = new Stack<>();
	private Stack<GCommand> redoStack = new Stack<>();

	public GCommandManager() {
		// 커맨드 클래스 등록
		commandClasses.put(GMode.DEFAULT, DefaultCommand.class);
		commandClasses.put(GMode.SHAPE, GShapeCommand.class);
		commandClasses.put(GMode.GROUP_MOVE, GGroupMoveCommand.class);
	}

	// 모드에 맞는 새 명령 객체 생성
	private GCommand createCommand(GMode mode) {
		try {
			return commandClasses.get(mode).newInstance();
		} catch (Exception e) {
			System.err.println("명령 객체 생성 실패: " + e.getMessage());
			return null;
		}
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