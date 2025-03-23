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

	private Map<GMode, GCommand> activeCommands = new HashMap<>();

	private interface CommandFactory {
		GCommand createCommand();
	}

	public GCommandManager() {
		// 모든 명령 팩토리를 한 곳에서 관리
		commandFactories.put(GMode.DEFAULT, () -> new DefaultCommand());
		commandFactories.put(GMode.SHAPE, () -> new GShapeCommand());

		// 도형 조작 관련 명령
		commandFactories.put(GMode.GROUP_MOVE, () -> {
			GCommand existingCommand = activeCommands.get(GMode.GROUP_MOVE);
			if (existingCommand != null) {
				return existingCommand;
			}

			GCommand newCommand = new GGroupMoveCommand();
			activeCommands.put(GMode.GROUP_MOVE, newCommand);
			return newCommand;
		});
		commandFactories.put(GMode.RESIZE, () -> {
			GCommand existingCommand = activeCommands.get(GMode.RESIZE);
			if (existingCommand != null) {
				return existingCommand;
			}

			GCommand newCommand = new GResizeCommand();
			activeCommands.put(GMode.RESIZE, newCommand);
			return newCommand;
		});
		commandFactories.put(GMode.ROTATE, () -> {
			GCommand existingCommand = activeCommands.get(GMode.ROTATE);
			if (existingCommand != null) {
				return existingCommand;
			}

			GCommand newCommand = new GRotateCommand();
			activeCommands.put(GMode.ROTATE, newCommand);
			return newCommand;
		});

		// 편집 명령
		commandFactories.put(GMode.CUT, () -> new GCutCommand());
		commandFactories.put(GMode.COPY, () -> new GCopyCommand());
		commandFactories.put(GMode.PASTE, () -> new GPasteCommand());
	}

	public GCommand createCommand(GMode mode) {
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
		GCommand command = null;

		// 모드에 따라 활성 명령 또는 새 명령 사용
		if (mode == GMode.GROUP_MOVE || mode == GMode.RESIZE || mode == GMode.ROTATE) {
			command = activeCommands.get(mode);
		}

		if (command == null) {
			command = createCommand(mode);
		}

		if (command != null) {
			command.execute();

			// 상태 변경이 있는 명령만 스택에 저장
			boolean shouldStore = true;

			if (mode == GMode.DEFAULT) {
				shouldStore = ((DefaultCommand) command).hasChanges();
			} else if (mode == GMode.COPY) {
				// COPY는 상태를 변경하지 않으므로 저장하지 않음
				shouldStore = false;
			}

			if (shouldStore) {
				undoStack.push(command);
				redoStack.clear();
				notifyObservers();
				System.out.println("명령 스택에 추가: " + command.getClass().getSimpleName());

				// 스택에 저장된 명령은 더 이상 활성 명령이 아님
				activeCommands.remove(mode);
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

	public void resetActiveCommands() {
		activeCommands.clear();
	}
}